package com.lumlate.midas.email;

import java.util.*;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.mail.search.RecipientStringTerm;
import javax.mail.search.SearchTerm;

import com.google.gson.Gson;

import com.lumlate.midas.coupon.Coupon;
import com.lumlate.midas.coupon.CouponBuilder;
import com.lumlate.midas.db.TempOP;
import com.lumlate.midas.email.Email;
import com.lumlate.midas.email.EmailParser;
import com.lumlate.midas.ml.EmailClassifier;
import com.lumlate.midas.utils.HtmlParser;
import com.lumlate.midas.utils.UrlParser;

public class InboxReader {
	private String username;
	private String password;
	private String receivingHost;
	private EmailParser parser=new EmailParser();
	private EmailClassifier emailclassifier=new EmailClassifier();
	private UrlParser urlparser = new UrlParser();
	private String dealregexfile;
	private String dateregexfile;
	
	public void readImapInbox(String imaphost,String imapport,String connectiontimeout,String imaptimeout){
		CouponBuilder cb=new CouponBuilder(this.dealregexfile,this.dateregexfile);
		
		Properties props=System.getProperties();
		//props.setProperty("mail.store.protocol", "imap");  //for lumlate mail server
		props.setProperty("mail.store.protocol", "imaps");   //for gmail
		props.setProperty("mail.imap.host", imaphost);
		props.setProperty("mail.imap.port", imapport);
		props.setProperty("mail.imap.connectiontimeout", connectiontimeout);
		props.setProperty("mail.imap.timeout", imaptimeout);
		props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		Gson gson = new Gson();
		Session session=Session.getDefaultInstance(props, null);
		try {
			//Store store=session.getStore("imap"); //for lumlate email server
			
			Store store=session.getStore("imaps");  //for gmail.com
			store.connect(this.receivingHost,this.username, this.password);
			Folder folder=store.getFolder("INBOX");//get inbox
			folder.open(Folder.READ_WRITE);//open folder only to read
			FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			//SearchTerm st=new RecipientStringTerm(Message.RecipientType.TO, "lumlatedeals@gmail.com");
			//Message message[]=folder.search(st);
			Message message[] = folder.search(ft);
		    for(int i=0;i<message.length;i++){
				this.parser.setMsg(message[i]);
				//message[i].setFlag(Flags.Flag.SEEN, true);
				Email email = this.parser.parser(message[i]);
				HtmlParser htmlparser = new HtmlParser();
				if(email.getContent()==null)continue;
				boolean parseflag=false; // for tracking if the parsing was successfull
				if(email.isIs_html() && !email.getContent().isEmpty()){
					parseflag=true;
					htmlparser.parsehtml(email.getContent());
				}else if(email.isIs_plaintext() && !email.getContent().isEmpty()){
					parseflag=true;
					htmlparser.setRawtext(email.getContent());
				}else if(!email.getContent().isEmpty()){
					parseflag=true;
					htmlparser.parsehtml(email.getContent());
				}
				
				if(htmlparser!=null){
					email.setHtml(htmlparser);
				}
				
				String category=this.emailclassifier.classifyEmail(email);
				
				if(!category.isEmpty()){
					email.setCategory(category);
				}
				//System.out.println(gson.toJson(email));
				if(category.equalsIgnoreCase("deal") || category.equalsIgnoreCase("subscription")){ //TODO convert category from string to enum
					try {
						Coupon coupon=cb.BuildCoupon(email);
						if(coupon!=null){
							TempOP tempop=new TempOP();
							tempop.setCoupon(coupon);
							tempop.setEmail(email);
							System.out.println(gson.toJson(tempop));
							message[i].setFlag(Flags.Flag.SEEN, true);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				else{
					//System.out.println("NOT CATEGORIZED "+email.getSubject());
				}
			  }
//TODO		persist email // can and probably should be asynchronous using a queue
			folder.close(true);
			store.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReceivingHost() {
		return receivingHost;
	}

	public void setReceivingHost(String receivingHost) {
		this.receivingHost = receivingHost;
	}
	
	public String getDealregexfile() {
		return dealregexfile;
	}

	public void setDealregexfile(String dealregexfile) {
		this.dealregexfile = dealregexfile;
	}

	public String getDateregexfile() {
		return dateregexfile;
	}

	public void setDateregexfile(String dateregexfile) {
		this.dateregexfile = dateregexfile;
	}

	public static void main(String[] args) {
		InboxReader newGmailClient=new InboxReader();
		/*
		newGmailClient.setUsername("lumlatedeals@lumlate.com");
		newGmailClient.setPassword("latelumdeals");
		newGmailClient.setReceivingHost("mail.lumlate.com");
		newGmailClient.readImapInbox("mail.lumlate.com","143","5000","5000");
		*/
		newGmailClient.setDealregexfile(args[0]);
		newGmailClient.setDateregexfile(args[1]);
		
		newGmailClient.setUsername("lumlatedeals@gmail.com");
		newGmailClient.setPassword("latelumdeals");
		newGmailClient.setReceivingHost("imap.gmail.com");
		newGmailClient.readImapInbox("imap.gmail.com","993","5000","5000");

	}

}
