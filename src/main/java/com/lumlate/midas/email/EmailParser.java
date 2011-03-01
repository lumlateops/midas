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
import javax.mail.internet.MimeUtility;

public class EmailParser {
	private Message msg;
	Pattern frompattern;
	Pattern receivedpattern;


	public EmailParser(){
		this.frompattern=Pattern.compile("(.*)(<.*>)");
		this.receivedpattern=Pattern.compile("(.*\\[)(.*)(\\].*)");
	}

	public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}

	private String getText(Part p) throws MessagingException, IOException {
		boolean textIsHtml = false;
		if (p.isMimeType("text/*")) {
			String s = (String)p.getContent();
			textIsHtml = p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart)p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
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
			if(t.toString().contains("lumlate.com")){
				email.setTo(t.toString());
			}
		}

		//sender
		String sender = null;
		for(Address f:from){
			sender=f.toString();
		}//use the last sender if multiple hops. verify that the list is sorted by hops?
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
		Object body = msg.getContent();
		String email_content="";
		if (body instanceof String) {
			email_content=body.toString()+"\n";
		}
		else if (body instanceof Multipart) {
			Multipart mp = (Multipart)body;
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				BodyPart b = mp.getBodyPart(i);
				email_content+=this.getText(b)+"\n";
			}
		}
		email.setContent(email_content);
		return email;
	}

}
