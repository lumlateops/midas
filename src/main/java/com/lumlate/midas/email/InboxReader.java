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

import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.dao.AccountDAO;
import com.lumlate.midas.db.dao.FetchHistoryDAO;
import com.lumlate.midas.db.dao.RetailersDAO;
import com.lumlate.midas.db.orm.AccountORM;
import com.lumlate.midas.db.orm.FetchHistoryORM;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

public class InboxReader {
	private LinkedList<SearchTerm> searchterms;

	public LinkedList<SearchTerm> getSearchterms() {
		return searchterms;
	}

	public void setSearchterms(LinkedList<SearchTerm> searchterms) {
		this.searchterms = searchterms;
	}

	public void readInbox(Properties props, String protocol, String hostname,
			String username, String password, Date lastfetch) throws Exception {
		String TASK_QUEUE_NAME = props
				.getProperty("com.lumlate.midas.rmq.scheduler.queue");
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
		try {
			Store store = session.getStore(protocol);
			store.connect(props.getProperty(hostname), username, password);
			Folder folder = store.getFolder("INBOX");// get inbox
			folder.open(Folder.READ_ONLY);// open folder only to read
			SearchTerm flagterm = new FlagTerm(new Flags(Flags.Flag.SEEN),
					false);

			SearchTerm[] starray = new SearchTerm[searchterms.size()];
			searchterms.toArray(starray);
			SearchTerm st = new OrTerm(starray); // TODO add date to this as
													// well so
			// that fetch is since last time
			if (lastfetch != null) {

				SearchTerm newerThen = new ReceivedDateTerm(ComparisonTerm.GT,
						lastfetch);
				st = new AndTerm(st, flagterm);
				st = new AndTerm(st, newerThen);
			}
			Message message[] = folder.search(st);
			for (int i = 0; i < message.length; i++) {
				Message msg = message[i];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				msg.writeTo(bos);
				byte[] buf = bos.toByteArray();
				channel.basicPublish("", TASK_QUEUE_NAME,
						MessageProperties.PERSISTENT_TEXT_PLAIN, buf);
			}
			channel.close();
			connection.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.out.println(String.format("Usage: <properties>"));
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Properties props = new Properties();
		props.load(new FileInputStream(args[0]));

		String mysqlhost = props.getProperty("com.lumlate.midas.mysql.host");
		String dbport = props.getProperty("com.lumlate.midas.mysql.port");
		String dbuser = props.getProperty("com.lumlate.midas.mysql.user");
		String dbpassword = props
				.getProperty("com.lumlate.midas.mysql.password");
		String db = props.getProperty("com.lumlate.midas.mysql.database");
		System.out.println(mysqlhost + " " + dbport + " " + dbuser + " "
				+ dbpassword + " " + db);
		MySQLAccess myaccess = new MySQLAccess(mysqlhost, dbport, dbuser,
				dbpassword, db);

		RetailersDAO retaildao = new RetailersDAO();
		retaildao.setStmt(myaccess.getConn().createStatement());
		LinkedList<String> retaildomains = retaildao.getallRetailerDomains();
		LinkedList<SearchTerm> searchterms = new LinkedList<SearchTerm>();
		for (String retaildomain : retaildomains) {
			SearchTerm retailsearchterm = new FromStringTerm(retaildomain);
			searchterms.add(retailsearchterm);
		}

		FetchHistoryDAO fetchhistorydao = new FetchHistoryDAO();
		FetchHistoryORM fetchorm = new FetchHistoryORM();
		fetchhistorydao.setAccess(myaccess);

		props.setProperty("mail.imap.connectiontimeout", "5000");
		props.setProperty("mail.imap.timeout", "5000");
		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.imap.host", "imap.gmail.com");
		props.setProperty("mail.imap.port", "993");
		props.setProperty("mail.imap.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		InboxReader inboxreader = new InboxReader();
		inboxreader.setSearchterms(searchterms);

		String user = "lumlatedeals@gmail.com";
		String pass = "latelumdeals";

		AccountDAO accountdao = new AccountDAO();
		AccountORM account = new AccountORM();
		accountdao.setAccess(myaccess);
		account.setEmail(user);
		account = accountdao.getIDfromEmail(account);
		Date lastfetchdate = null;
		Date date = new Date();
		fetchorm.setFetchStartTime(formatter.format(date));
		String lastfetch = fetchhistorydao
				.getLastFetchTime(account.getUserid());
		if (lastfetch != null) {
			lastfetchdate = formatter.parse(lastfetch);
		}
		fetchorm.setFetchEndTime(formatter.format(date));
		fetchorm.setUserid(account.getUserid());
		fetchorm.setFetchStatus("In Progress");
		fetchorm = fetchhistorydao.insert(fetchorm);
		try {
			inboxreader.readInbox(props, "imaps", "mail.imap.host", user, pass,
					lastfetchdate);
			date = new Date();
			fetchorm.setFetchEndTime(formatter.format(date));
			fetchorm.setFetchStatus("Success");
			fetchhistorydao.update(fetchorm);
		} catch (Exception e) {
			e.printStackTrace();
			date = new Date();
			fetchorm.setFetchEndTime(formatter.format(date));
			fetchorm.setFetchStatus("Error");
			fetchorm.setFetchErrorMessage(e.getMessage());
			fetchhistorydao.update(fetchorm);
		}
		myaccess.Dissconnect();
	}
}