package com.lumlate.midas.email;

import java.io.IOException;
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
import javax.mail.internet.MimeUtility;

import com.lumlate.midas.ml.EmailClassifier;

public class EmailParser {
	private Message msg;
	Pattern frompattern;
	Pattern receivedpattern;
	boolean is_text;
	boolean is_html;
	boolean is_attachment;
	
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
		//to
		for(Address t:to){
			if(t.toString().contains("lumlate")){//TODO dont hard code domain
				email.setTo(t.toString());
			}
		}
		//sender
		String sender = null;
		for(Address f:from){
			sender=f.toString();
		}//TODO use the last sender if multiple hops. verify that the list is sorted by hops?
		Matcher matcher = this.frompattern.matcher(sender);
		if(matcher.matches()){
			email.setFromname(matcher.group(1));
			email.setFromemail(matcher.group(2));
		}
		//date
		email.setRecieveddate(msg.getReceivedDate());
		email.setSentdate(msg.getSentDate());
		//subject
		email.setSubject(MimeUtility.decodeText(msg.getSubject()));

		//headers
		Enumeration<Header> e= msg.getAllHeaders();
		while(e.hasMoreElements()){
			Header h = e.nextElement();
			if(h.getName().equalsIgnoreCase("Received")){
				String value=MimeUtility.decodeText(h.getValue());
				if(value.startsWith("from")){
					Matcher rmatcher=this.receivedpattern.matcher(value);
					if(rmatcher.matches()){
						//get sender ip
					}
				}
			}
			if(h.getName().equalsIgnoreCase("Received-SPF")){
				String value=MimeUtility.decodeText(h.getValue());
				String[] temp=value.split(" ");
				email.setSpf_result(temp[0]);
			}
		}		
		
		//body
		try{
			Object body = msg.getContent();
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
