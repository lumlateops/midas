package com.lumlate.midas.email;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MailDateFormat;
import javax.mail.internet.MimeUtility;

import com.google.gson.Gson;
import com.lumlate.midas.ml.EmailClassifier;

public class EmailParser {
	private Message msg;
	Pattern frompattern;
	Pattern receivedpattern;
	boolean is_text;
	boolean is_html;
	boolean is_attachment;
	private static SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public EmailParser(){
		this.is_text=false;
		this.is_attachment=false;
		this.is_html=false;
		this.frompattern=Pattern.compile("(.*)(<.*>)");
		this.receivedpattern=Pattern.compile("(.*\\[)(.*)(\\].*)");
	}

	public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}

	private String getText(Part p) throws MessagingException, IOException, Exception {
		if (p.isMimeType("text/*")) {
			String s = (String)p.getContent();
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart)p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null){
						is_text=true;
						text = getText(bp);
					}
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						is_html=true;
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart)p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					this.is_attachment=true;
					return s;
			}
		}
		return null;
	}


	public Email parser(Message msg) throws Exception{
		Address[] from=msg.getFrom();
		Address[] to=msg.getAllRecipients();
		Email email=new Email();
		email.setSubject(MimeUtility.decodeText(msg.getSubject()));

		//headers
		Enumeration<Header> e= msg.getAllHeaders();
		while(e.hasMoreElements()){
			Header h = e.nextElement();
			//System.out.println(h.getName()+"\t"+h.getValue());
			if(h.getName().equalsIgnoreCase("Received")){
				String value=MimeUtility.decodeText(h.getValue());
				if(value.startsWith("From")){
					Matcher rmatcher=this.receivedpattern.matcher(value);
					if(rmatcher.matches()){
						//get sender ip
					}
				}
			}
			if(h.getName().equalsIgnoreCase("From")){
				String value=MimeUtility.decodeText(h.getValue());
				Matcher rmatcher=this.frompattern.matcher(value);
				if(rmatcher.matches()){
					email.setFromname(rmatcher.group(1));
					String fromemail = rmatcher.group(2).replace(">", "").replace("<", "");
					email.setFromemail(fromemail);
				}
			}
			if(h.getName().equalsIgnoreCase("Delivered-To")){
				String value=MimeUtility.decodeText(h.getValue());
				email.setToemail(value);
			}
			if(h.getName().equalsIgnoreCase("To")){
				String value=MimeUtility.decodeText(h.getValue());
				email.setToname(value);
			}
			if(h.getName().equalsIgnoreCase("Received-SPF")){
				String value=MimeUtility.decodeText(h.getValue());
				String[] temp=value.split(" ");
				email.setSpf_result(temp[0]);
			}
			if(h.getName().equalsIgnoreCase("Date")){
				MailDateFormat md=new MailDateFormat();
				Date s = md.parse(h.getValue());
				Date ss=new Date(s.getTime());
				email.setRecieveddate(formatter.format(ss).toString());
				email.setSentdate(formatter.format(ss).toString());
			}
		}		
		
		//body
		try{
			String email_content="";
			email_content=this.getText(msg);
			if(!(email_content==null)){
				email.setContent(email_content);
			}
			email.setIs_html(this.is_html);
			email.setIs_attachment(this.is_attachment);
			email.setIs_plaintext(this.is_text);
		}catch (Exception err){
			err.printStackTrace();
			
		}
		return email;
	}

}
