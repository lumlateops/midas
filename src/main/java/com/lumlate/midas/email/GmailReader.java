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

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.BASE64EncoderStream;

import net.oauth.OAuthConsumer;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Hashtable;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.search.FlagTerm;


/**
 * Performs XOAUTH authentication.
 *
 * <p>Before using this class, you must call {@code initialize} to install the
 * XOAUTH SASL provider.
 */
public class GmailReader {
	public static final class XoauthProvider extends Provider {
		public XoauthProvider() {
			super("Google Xoauth Provider", 1.0,
			"Provides the Xoauth experimental SASL Mechanism");
			put("SaslClientFactory.XOAUTH",
			"com.lumlate.midas.email.XoauthSaslClientFactory");
		}
	}

	/**
	 * Generates a new OAuthConsumer with token and secret of
	 * "anonymous"/"anonymous". This can be used for testing.
	 */
	public static OAuthConsumer getAnonymousConsumer() {
		return new OAuthConsumer(null, "anonymous", "anonymous", null);
	}

	/**
	 * Installs the XOAUTH SASL provider. This must be called exactly once before
	 * calling other methods on this class.
	 */
	public static void initialize() {
		Security.addProvider(new XoauthProvider());
	}

	/**
	 * Connects and authenticates to an IMAP server with XOAUTH. You must have
	 * called {@code initialize}.
	 *
	 * @param host Hostname of the imap server, for example {@code
	 *     imap.googlemail.com}.
	 * @param port Port of the imap server, for example 993.
	 * @param userEmail Email address of the user to authenticate, for example
	 *     {@code xoauth@gmail.com}.
	 * @param oauthToken The user's OAuth token.
	 * @param oauthTokenSecret The user's OAuth token secret.
	 * @param consumer The application's OAuthConsumer. For testing, use
	 *     {@code getAnonymousConsumer()}.
	 * @param debug Whether to enable debug logging on the IMAP connection.
	 *
	 * @return An authenticated IMAPSSLStore that can be used for IMAP operations.
	 */
	public static IMAPSSLStore connectToImap(String host,
			int port,
			String userEmail,
			String oauthToken,
			String oauthTokenSecret,
			OAuthConsumer consumer,
			boolean debug) throws Exception {
		Properties props = new Properties();
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH");
		props.put(XoauthSaslClientFactory.OAUTH_TOKEN_PROP,
				oauthToken);
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
	 * @param host Hostname of the smtp server, for example {@code
	 *     smtp.googlemail.com}.
	 * @param port Port of the smtp server, for example 587.
	 * @param userEmail Email address of the user to authenticate, for example
	 *     {@code xoauth@gmail.com}.
	 * @param oauthToken The user's OAuth token.
	 * @param oauthTokenSecret The user's OAuth token secret.
	 * @param consumer The application's OAuthConsumer. For testing, use
	 *     {@code getAnonymousConsumer()}.
	 * @param debug Whether to enable debug logging on the connection.
	 *
	 * @return An authenticated SMTPTransport that can be used for SMTP
	 *     operations.
	 */
	/*
 public static SMTPTransport connectToSmtp(String host,
                                           int port,
                                           String userEmail,
                                           String oauthToken,
                                           String oauthTokenSecret,
                                           OAuthConsumer consumer,
                                           boolean debug) throws Exception {
   Properties props = new Properties();
   props.put("mail.smtp.ehlo", "true");
   props.put("mail.smtp.auth", "false");
   props.put("mail.smtp.starttls.enable", "true");
   props.put("mail.smtp.starttls.required", "true");
   props.put("mail.smtp.sasl.enable", "false");
   Session session = Session.getInstance(props);
   session.setDebug(debug);

   final URLName unusedUrlName = null;
   SMTPTransport transport = new SMTPTransport(session, unusedUrlName);
   // If the password is non-null, SMTP tries to do AUTH LOGIN.
   final String emptyPassword = null;
   transport.connect(host, port, userEmail, emptyPassword);

   /*
	 * I couldn't get the SASL infrastructure to work with JavaMail 1.4.3;
	 * I don't think it was ready yet in that release. So we'll construct the
	 * AUTH command manually.

   XoauthSaslResponseBuilder builder = new XoauthSaslResponseBuilder();
   byte[] saslResponse = builder.buildResponse(userEmail,
                                               XoauthProtocol.SMTP,
                                               oauthToken,
                                               oauthTokenSecret,
                                               consumer);
   saslResponse = BASE64EncoderStream.encode(saslResponse);
   transport.issueCommand("AUTH XOAUTH " + new String(saslResponse),235);
   return transport;
 }*/

	/**
	 * Authenticates to IMAP with parameters passed in on the commandline.
	 */
	public static void main(String args[]) throws Exception {

		String email = "lumlatedeals@gmail.com";
		String oauthToken = "1/tLS_lJLsE6TyPUD6_Z1WnjFoEKJgmAHZjXU1RDHh3VY";
		String oauthTokenSecret = "nq5U3HXmnWLmSJdyULZMUa_9";
		String TASK_QUEUE_NAME="gmail_oauth";
		String rmqserver="rmq01.deallr.com";
		
		initialize();
		IMAPSSLStore imapSslStore = connectToImap("imap.googlemail.com",993,email,oauthToken,oauthTokenSecret,getAnonymousConsumer(),true);
		
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(rmqserver);
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

		Folder folder=imapSslStore.getFolder("INBOX");//get inbox
		folder.open(Folder.READ_WRITE);//open folder only to read
		FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		//SearchTerm st=new RecipientStringTerm(Message.RecipientType.TO, "lumlatedeals@gmail.com");
		//Message message[]=folder.search(st);
		Message message[] = folder.search(ft);
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
	    channel.close();
	    connection.close();
	}
}