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
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;

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

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
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
public class CopyOfGmailReader {
	/**
	 * Authenticates to IMAP with parameters passed in on the commandline.
	 */
	public static void main(String args[]) throws Exception {

		String mysqlhost = "localhost";
		String mysqlport = "3306";
		String mysqluser = "lumlate";
		String mysqlpassword = "lumlate$";
		String database = "lumlate";

		String email = "sharmavipul@gmail.com";
		String oauthToken = "1/h1u_K6kgi6rnL4z00dCW6y-vkGy-8im-G3bVaETJKBQ";
		String oauthTokenSecret = "3sp5dlRElbR1ZmTvvZ7b3fdJ";

		String TASK_QUEUE_NAME = "gmail_oauth";
		String rmqserver = "rmq01.deallr.com";
		XoauthAuthenticator.initialize();
		String callback = "http://dev.deallr.com/account/upgradeEmailToken/"
				+ "4" + "/gmail/" + "sharmavipul@gmail.com";
		Store imapSslStore = XoauthAuthenticator.connectToImap(
				"imap.googlemail.com", 993, email, oauthToken,
				oauthTokenSecret, new OAuthConsumer(callback, "deallr.com",
						"f_yk4d2GkQljJ38JQrcRJBPr", null), true);
		System.out.println("Successfully authenticated to IMAP.\n");
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rmqserver);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		
		Folder folder = imapSslStore.getFolder("INBOX");// get inbox
		System.out.println("Yes I am gere"+imapSslStore);
		System.out.println("XXXX " + imapSslStore.isConnected());
		System.out.println("ZZZZZZ " + folder.getFullName());
		System.out.println("PPPPP " + folder.getName());
		folder.open(Folder.READ_ONLY);// open folder only to read
		FetchProfile fp = new FetchProfile();     
	    fp.add("X-Mailer");

		FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		System.out.println("CCCCC " + imapSslStore.isConnected());
		MySQLAccess myaccess = new MySQLAccess(mysqlhost, mysqlport, mysqluser,
				mysqlpassword, database);
		RetailersORM retailorm = new RetailersORM();
		retailorm.setStmt(myaccess.getStmt());
		LinkedList<String> retaildomains = retailorm.getallRetailerDomains();
		LinkedList<SearchTerm> searchterms = new LinkedList<SearchTerm>();
		for (String retaildomain : retaildomains) {
			SearchTerm retailsearchterm = new FromStringTerm(retaildomain);
			searchterms.add(retailsearchterm);
		}
		SearchTerm[] starray = new SearchTerm[searchterms.size()];
		searchterms.toArray(starray);
		SearchTerm st = new OrTerm(starray); // add date to this as well so that
												// fetch is since last time
		Message message[] = folder.search(st);
		for (int i = 0; i < message.length; i++) {
			Message msg = message[i];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			msg.writeTo(bos);
			// ObjectOutput out = new ObjectOutputStream(bos);
			// out.writeObject(bos);
			// out.close();
			byte[] buf = bos.toByteArray();
			channel.basicPublish("", TASK_QUEUE_NAME,
					MessageProperties.PERSISTENT_TEXT_PLAIN, buf);
		}
		myaccess.Dissconnect();
		channel.close();
		connection.close();
	}
}