package com.lumlate.midas.email;

import java.util.*;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class InboxReader {
	private String username;
	private String password;
	private String receivingHost;

	public void readImapInbox(String imaphost,String imapport,String connectiontimeout,String imaptimeout){

		Properties props=System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.imap.host", imaphost);
		props.setProperty("mail.imap.port", imapport);
		props.setProperty("mail.imap.connectiontimeout", connectiontimeout);
		props.setProperty("mail.imap.timeout", imaptimeout);
		props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");

		Session session=Session.getDefaultInstance(props, null);

		try {

			Store store=session.getStore("imaps");
			store.connect(this.receivingHost,this.username, this.password);
			Folder folder=store.getFolder("INBOX");//get inbox
			folder.open(Folder.READ_ONLY);//open folder only to read
			Message message[]=folder.getMessages();

			for(int i=0;i<message.length;i++){
				System.out.println(message[i]);
			}

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

	public static void main(String[] args) {
		InboxReader newGmailClient=new InboxReader();
		newGmailClient.setUsername("lumlatedeals@gmail.com");
		newGmailClient.setPassword("latelumdeals");
		newGmailClient.setReceivingHost("imap.gmail.com");
		newGmailClient.readImapInbox("imap.gmail.com","993","5000","5000");

	}

}
