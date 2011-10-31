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

import com.google.code.samples.XoauthSaslClientFactory;
import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.dao.RetailersDAO;
import com.lumlate.midas.db.orm.RetailersORM;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.sun.mail.imap.IMAPSSLStore;

import net.oauth.OAuthConsumer;

import java.io.ByteArrayOutputStream;
import java.security.Provider;
import java.security.Security;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;

/**
 * Performs XOAUTH authentication.
 * 
 * <p>
 * Before using this class, you must call {@code initialize} to install the
 * XOAUTH SASL provider.
 */
public class GmailReader {
	public static final class XoauthProvider extends Provider {
		public XoauthProvider() {
			super("Google Xoauth Provider", 1.0,
					"Provides the Xoauth experimental SASL Mechanism");
			put("SaslClientFactory.XOAUTH",
					"com.google.code.samples.XoauthSaslClientFactory");
		}
	}

	/**
	 * Generates a new OAuthConsumer with token and secret of
	 * "anonymous"/"anonymous". This can be used for testing.
	 */
	public static OAuthConsumer getDeallrConsumer() {
		 return new OAuthConsumer(null, "deallr.com","f_yk4d2GkQljJ38JQrcRJBPr", null);
	}

	/**
	 * Installs the XOAUTH SASL provider. This must be called exactly once
	 * before calling other methods on this class.
	 */
	public static void initialize() {
		Security.addProvider(new XoauthProvider());
	}

	/**
	 * Connects and authenticates to an IMAP server with XOAUTH. You must have
	 * called {@code initialize}.
	 * 
	 * @param host
	 *            Hostname of the imap server, for example
	 *            {@code imap.googlemail.com}.
	 * @param port
	 *            Port of the imap server, for example 993.
	 * @param userEmail
	 *            Email address of the user to authenticate, for example
	 *            {@code xoauth@gmail.com}.
	 * @param oauthToken
	 *            The user's OAuth token.
	 * @param oauthTokenSecret
	 *            The user's OAuth token secret.
	 * @param consumer
	 *            The application's OAuthConsumer. For testing, use
	 *            {@code getAnonymousConsumer()}.
	 * @param debug
	 *            Whether to enable debug logging on the IMAP connection.
	 * 
	 * @return An authenticated IMAPSSLStore that can be used for IMAP
	 *         operations.
	 */
	public static IMAPSSLStore connectToImap(String host, int port,
			String userEmail, String oauthToken, String oauthTokenSecret,
			OAuthConsumer consumer, boolean debug) throws Exception {
		Properties props = new Properties();
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH");
		props.put(XoauthSaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);
		props.put(XoauthSaslClientFactory.OAUTH_TOKEN_SECRET_PROP,
				oauthTokenSecret);
		props.put(XoauthSaslClientFactory.CONSUMER_KEY_PROP,
				consumer.consumerKey);
		props.put(XoauthSaslClientFactory.CONSUMER_SECRET_PROP,
				consumer.consumerSecret);
		Session session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		IMAPSSLStore store = new IMAPSSLStore(session, unusedUrlName);
		final String emptyPassword = "";
		store.connect(host, port, userEmail, emptyPassword);
		return store;
	}

	/**
	 * Authenticates to IMAP with parameters passed in on the commandline.
	 */
	public static void main(String args[]) throws Exception {

		String mysqlhost="localhost";
		String mysqlport="3306";
		String mysqluser="lumlate";
		String mysqlpassword="lumlate$";
		String database="lumlate";
		MySQLAccess myaccess = new MySQLAccess(mysqlhost,mysqlport,mysqluser,mysqlpassword,database);
		
		String email = "sharmavipul@gmail.com";
		String oauthToken = "1/co5CyT8QrJXyJagK5wzWNGZk2RTA0FU_dF9IhwPvlBs";
		String oauthTokenSecret = "aK5LCYOFrUweFPfMcPYNXWzg";
		String TASK_QUEUE_NAME="gmail_oauth";
		String rmqserver="rmq01.deallr.com";
		
		initialize();
		IMAPSSLStore imapSslStore = connectToImap("imap.googlemail.com",993,email,oauthToken,oauthTokenSecret,getDeallrConsumer() ,true);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rmqserver);
		factory.setUsername("lumlate");
		factory.setPassword("lumlate$");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

		Folder folder=imapSslStore.getFolder("INBOX");//get inbox
		folder.open(Folder.READ_WRITE);//open folder only to read
		SearchTerm flagterm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		
		RetailersDAO retaildao = new RetailersDAO();
		retaildao.setStmt(myaccess.getConn().createStatement());
		LinkedList<String>retaildomains=retaildao.getallRetailerDomains();
		LinkedList<SearchTerm> searchterms = new LinkedList<SearchTerm>();
		for(String retaildomain:retaildomains){
			SearchTerm retailsearchterm=new FromStringTerm(retaildomain);
			searchterms.add(retailsearchterm);
		}
		SearchTerm[] starray= new SearchTerm[searchterms.size()];
		searchterms.toArray(starray);
		SearchTerm st=new OrTerm(starray); //TODO add date to this as well so that fetch is since last time
		SearchTerm searchterm = new AndTerm(st,flagterm);
		
		Message message[]=folder.search(searchterm);
		for(int i=0;i<message.length;i++){
			Message msg = message[i];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			msg.writeTo(bos);
			byte[] buf = bos.toByteArray();
			channel.basicPublish("", TASK_QUEUE_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,buf);
		}
		myaccess.Dissconnect();
		channel.close();
		connection.close();
	}
}