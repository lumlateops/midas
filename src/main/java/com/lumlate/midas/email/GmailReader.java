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
import com.lumlate.midas.db.RetailersORM;
import com.lumlate.midas.utils.MySQLAccess;
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
	public static OAuthConsumer getAnonymousConsumer() {
		return new OAuthConsumer(null, "anonymous", "anonymous", null);
		 //return new OAuthConsumer(null, "deallr.com","f_yk4d2GkQljJ38JQrcRJBPr", null);
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
	 * Connects and authenticates to an SMTP server with XOAUTH. You must have
	 * called {@code initialize}.
	 * 
	 * @param host
	 *            Hostname of the smtp server, for example
	 *            {@code smtp.googlemail.com}.
	 * @param port
	 *            Port of the smtp server, for example 587.
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
	 *            Whether to enable debug logging on the connection.
	 * 
	 * @return An authenticated SMTPTransport that can be used for SMTP
	 *         operations.
	 */
	/*
	 * public static SMTPTransport connectToSmtp(String host, int port, String
	 * userEmail, String oauthToken, String oauthTokenSecret, OAuthConsumer
	 * consumer, boolean debug) throws Exception { Properties props = new
	 * Properties(); props.put("mail.smtp.ehlo", "true");
	 * props.put("mail.smtp.auth", "false");
	 * props.put("mail.smtp.starttls.enable", "true");
	 * props.put("mail.smtp.starttls.required", "true");
	 * props.put("mail.smtp.sasl.enable", "false"); Session session =
	 * Session.getInstance(props); session.setDebug(debug);
	 * 
	 * final URLName unusedUrlName = null; SMTPTransport transport = new
	 * SMTPTransport(session, unusedUrlName); // If the password is non-null,
	 * SMTP tries to do AUTH LOGIN. final String emptyPassword = null;
	 * transport.connect(host, port, userEmail, emptyPassword);
	 * 
	 * /* I couldn't get the SASL infrastructure to work with JavaMail 1.4.3; I
	 * don't think it was ready yet in that release. So we'll construct the AUTH
	 * command manually.
	 * 
	 * XoauthSaslResponseBuilder builder = new XoauthSaslResponseBuilder();
	 * byte[] saslResponse = builder.buildResponse(userEmail,
	 * XoauthProtocol.SMTP, oauthToken, oauthTokenSecret, consumer);
	 * saslResponse = BASE64EncoderStream.encode(saslResponse);
	 * transport.issueCommand("AUTH XOAUTH " + new String(saslResponse),235);
	 * return transport; }
	 */

	/**
	 * Authenticates to IMAP with parameters passed in on the commandline.
	 */
	public static void main(String args[]) throws Exception {

		String mysqlhost="localhost";
		String mysqlport="3306";
		String mysqluser="lumlate";
		String mysqlpassword="lumlate$";
		String database="lumlate";
		
		String email = "sharmavipul@gmail.com";
		String oauthToken = "1/h1u_K6kgi6rnL4z00dCW6y-vkGy-8im-G3bVaETJKBQ";
		String oauthTokenSecret = "3sp5dlRElbR1ZmTvvZ7b3fdJ";
		/*
		String email = "sharmavipul@gmail.com";
		String oauthToken = "1/2Qt9NwPBHi4SicISYpTw9_VXHFfpAr-kBencUV71YTQ";
		String oauthTokenSecret = "v57fxlZIo1v_O69wJreGQsk_";*/
		String TASK_QUEUE_NAME="gmail_oauth";
		String rmqserver="rmq01.deallr.com";
		
		initialize();
		String callback = "http://dev.deallr.com/account/upgradeEmailToken/" + "4" + "/gmail/" + "sharmavipul@gmail.com";
		//IMAPSSLStore imapSslStore = connectToImap("imap.googlemail.com",993,email,oauthToken,oauthTokenSecret, new OAuthConsumer(callback, "deallr.com", "f_yk4d2GkQljJ38JQrcRJBPr", null),true);
		IMAPSSLStore imapSslStore = connectToImap("imap.googlemail.com",993,email,oauthToken,oauthTokenSecret,getAnonymousConsumer() ,true);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rmqserver);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

		Folder folder=imapSslStore.getFolder("INBOX");//get inbox
		folder.open(Folder.READ_WRITE);//open folder only to read
		FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		
		MySQLAccess myaccess = new MySQLAccess(mysqlhost,mysqlport,mysqluser,mysqlpassword,database);
		RetailersORM retailorm = new RetailersORM();
		retailorm.setStmt(myaccess.getStmt());
		LinkedList<String>retaildomains=retailorm.getallRetailerDomains();
		LinkedList<SearchTerm> searchterms = new LinkedList<SearchTerm>();
		for(String retaildomain:retaildomains){
			SearchTerm retailsearchterm=new FromStringTerm(retaildomain);
			searchterms.add(retailsearchterm);
		}
		SearchTerm[] starray= new SearchTerm[searchterms.size()];
		searchterms.toArray(starray);
		SearchTerm st=new OrTerm(starray); //add date to this as well so that fetch is since last time
		Message message[]=folder.search(st);
		for(int i=0;i<message.length;i++){
			Message msg = message[i];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			msg.writeTo(bos);
			//ObjectOutput out = new ObjectOutputStream(bos);
			//out.writeObject(bos);
			//out.close();
			byte[] buf = bos.toByteArray();
			channel.basicPublish("", TASK_QUEUE_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,buf);
		}
		myaccess.Dissconnect();
		channel.close();
		connection.close();
	}
}