package com.lumlate.midas.email;

/* Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.code.samples.XoauthAuthenticator;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

public class InboxReader {
	private LinkedList<SearchTerm> searchterms;
	private SimpleDateFormat dateformat;
	private Calendar cal;
	private Date fetchsince;

	public InboxReader() {
		dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -15);
		fetchsince = cal.getTime();
		XoauthAuthenticator.initialize();
	}

	public LinkedList<SearchTerm> getSearchterms() {
		return searchterms;
	}

	public void setSearchterms(LinkedList<SearchTerm> searchterms) {
		this.searchterms = searchterms;
	}

	public Boolean readInbox(Properties props, String protocol,
			String hostname, String username, String password, Date lastfetch,
			String TASK_QUEUE_NAME) throws Exception {
		String rmqserver = props.getProperty("com.lumlate.midas.rmq.server");
		String rmqusername = props
				.getProperty("com.lumlate.midas.rmq.username");
		String rmqpassword = props
				.getProperty("com.lumlate.midas.rmq.password");

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(rmqusername);
		factory.setPassword(rmqpassword);
		factory.setHost(rmqserver);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

		Session session = Session.getDefaultInstance(props, null);
		// session.setDebug(true);
		// session.setDebugOut(System.out);
		Store store = session.getStore(protocol);
		store.connect(hostname, username, password);
		// Folder folder = store.getFolder("Inbox");// get inbox
		Folder folder = store.getDefaultFolder();
		folder.open(Folder.READ_WRITE);
		Folder deallrfolder = store.getFolder("Deallr");
		// if (!deallrfolder.exists()) {
		try {
			deallrfolder.create(Folder.HOLDS_MESSAGES);
		} catch (Exception err) {
			System.out.println("Cannot create Folder");
			err.printStackTrace();
		}
		// }

		// SearchTerm flagterm = new FlagTerm(new Flags(Flags.Flag.SEEN),
		// false);

		SearchTerm[] starray = new SearchTerm[searchterms.size()];
		searchterms.toArray(starray);
		SearchTerm st = new OrTerm(starray); // TODO add date to this as
		SearchTerm newerThen; // well so
		// that fetch is since last time
		if (lastfetch != null) {
			newerThen = new ReceivedDateTerm(ComparisonTerm.GT, lastfetch);
			// st = new AndTerm(st, flagterm);
			st = new AndTerm(st, newerThen);
		} else {
			newerThen = new ReceivedDateTerm(ComparisonTerm.GT, fetchsince);
			// st = new AndTerm(st, flagterm);
			st = new AndTerm(st, newerThen);
		}
		try {
			Message message[] = folder.search(st);
			for (int i = 0; i < message.length; i++) {
				Message msg = message[i];
				folder.copyMessages(new Message[] { msg }, deallrfolder);
				msg.setFlag(Flag.SEEN, true);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				msg.writeTo(bos);
				byte[] buf = bos.toByteArray();
				channel.basicPublish("", TASK_QUEUE_NAME,
						MessageProperties.PERSISTENT_TEXT_PLAIN, buf);
			}
		} catch (Exception err) {
			err.printStackTrace();
			throw new Exception(err.getMessage());
		}

		folder.close(true);
		store.close();
		channel.close();
		connection.close();
		return true;
	}

	public Boolean readOauthInbox(Properties props, String scope, String email,
			String oauthToken, String oauthTokenSecret, Date lastfetch,
			String TASK_QUEUE_NAME, String fetchtype) throws Exception {
		String rmqserver = props.getProperty("com.lumlate.midas.rmq.server");
		String rmqusername = props
				.getProperty("com.lumlate.midas.rmq.username");
		String rmqpassword = props
				.getProperty("com.lumlate.midas.rmq.password");

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(rmqusername);
		factory.setPassword(rmqpassword);
		factory.setHost(rmqserver);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

		Store imapSslStore = XoauthAuthenticator
				.connectToImap("imap.googlemail.com", 993, email, oauthToken,
						oauthTokenSecret,
						XoauthAuthenticator.getDeallrConsumer(), true);

		SearchTerm[] starray = new SearchTerm[searchterms.size()];
		searchterms.toArray(starray);
		SearchTerm st = new OrTerm(starray); // TODO add date to this as
		SearchTerm newerThen; 
		// that fetch is since last time
		if (lastfetch != null) {
			newerThen = new ReceivedDateTerm(ComparisonTerm.GT, lastfetch);
			st = new AndTerm(st, newerThen);
		} else {
			newerThen = new ReceivedDateTerm(ComparisonTerm.GT, fetchsince);
			st = new AndTerm(st, newerThen);
		}

		Folder folder = imapSslStore.getFolder("[Gmail]/All Mail");// get inbox
		folder.open(Folder.READ_WRITE);
		//Folder deallrfolder=null;
		//if(fetchtype.equalsIgnoreCase("scheduler")){
			//deallrfolder = imapSslStore.getFolder("Deallr");
			//if (!deallrfolder.exists())
			//	deallrfolder.create(Folder.HOLDS_MESSAGES);			
		//}
		try {
			Message message[] = folder.search(st);
			for (int i = 0; i < message.length; i++) {
				Message msg = message[i];
				msg.setFlag(Flag.SEEN, true);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				msg.writeTo(bos);
				byte[] buf = bos.toByteArray();
				channel.basicPublish("", TASK_QUEUE_NAME,
						MessageProperties.PERSISTENT_TEXT_PLAIN, buf);
				//if(deallrfolder!=null){
				//	folder.copyMessages(new Message[] { msg }, deallrfolder);
				//}
			}
		} catch (Exception err) {
			err.printStackTrace();
			throw new Exception(err.getMessage());
		}

		folder.close(true);
		//deallrfolder.close(true);
		imapSslStore.close();
		channel.close();
		connection.close();
		return true;
	}
}