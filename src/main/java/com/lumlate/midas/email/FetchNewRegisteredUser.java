package com.lumlate.midas.email;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;

import com.google.gson.Gson;
import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.dao.AccountDAO;
import com.lumlate.midas.db.dao.FetchHistoryDAO;
import com.lumlate.midas.db.dao.RetailersDAO;
import com.lumlate.midas.db.dao.ServiceProviderDAO;
import com.lumlate.midas.db.orm.AccountORM;
import com.lumlate.midas.db.orm.FetchHistoryORM;
import com.lumlate.midas.db.orm.ServiceProviderORM;
import com.lumlate.midas.email.InboxReader;
import com.lumlate.midas.utils.Utility;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class FetchNewRegisteredUser {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	RetailersDAO retaildao;
	MySQLAccess myaccess;
	LinkedList<String> retaildomains;
	FetchHistoryDAO fetchhistorydao;
	FetchHistoryORM fetchorm;
	AccountDAO accountdao;
	AccountORM account;
	InboxReader inboxreader;
	ServiceProviderDAO providerdao;
	ServiceProviderORM provider;
	private String rmqserver;
	private String TASK_QUEUE_NAME;
	private String NEW_USER_QUEUE_NAME;
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private Gson gson;

	public FetchNewRegisteredUser(String mysqlhost, String dbport,
			String dbuser, String dbpassword, String db, Properties props)
			throws Exception {
		super();
		retaildao = new RetailersDAO();
		myaccess = new MySQLAccess(mysqlhost, dbport, dbuser, dbpassword, db);
		retaildao.setStmt(myaccess.getConn().createStatement());
		retaildomains = retaildao.getallRetailerDomains();
		fetchhistorydao = new FetchHistoryDAO();
		fetchorm = new FetchHistoryORM();
		fetchhistorydao.setAccess(myaccess);
		accountdao = new AccountDAO();
		account = new AccountORM();
		accountdao.setAccess(myaccess);
		providerdao = new ServiceProviderDAO();
		providerdao.setAccess(myaccess);
		provider = new ServiceProviderORM();
		inboxreader = new InboxReader();
		rmqserver = props.getProperty("com.lumlate.midas.rmq.server");
		TASK_QUEUE_NAME = props
				.getProperty("com.lumlate.midas.rmq.register.queue");
		NEW_USER_QUEUE_NAME = props
				.getProperty("com.lumlate.midas.rmq.register.publish.queue");
		factory = new ConnectionFactory();
		factory.setHost(rmqserver);
		factory.setUsername(props.getProperty("com.lumlate.midas.rmq.username"));
		factory.setPassword(props.getProperty("com.lumlate.midas.rmq.password"));
		connection = factory.newConnection();
		channel = connection.createChannel();
		gson = new Gson();
	}

	public void clear() throws Exception {
		this.myaccess.Dissconnect();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println(String.format("Usage: <properties>"));
		}

		Properties props = new Properties();
		try {
			props.load(new FileInputStream(args[0]));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String mysqlhost = props.getProperty("com.lumlate.midas.mysql.host");
		String dbport = props.getProperty("com.lumlate.midas.mysql.port");
		String dbuser = props.getProperty("com.lumlate.midas.mysql.user");
		String dbpassword = props
				.getProperty("com.lumlate.midas.mysql.password");
		String db = props.getProperty("com.lumlate.midas.mysql.database");

		FetchNewRegisteredUser fetchscheduler = null;
		try {
			fetchscheduler = new FetchNewRegisteredUser(mysqlhost, dbport,
					dbuser, dbpassword, db, props);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		LinkedList<SearchTerm> searchterms = new LinkedList<SearchTerm>();
		for (String retaildomain : fetchscheduler.retaildomains) {
			SearchTerm retailsearchterm = new FromStringTerm(retaildomain);
			searchterms.add(retailsearchterm);
		}
		fetchscheduler.inboxreader.setSearchterms(searchterms);

		fetchscheduler.channel.queueDeclare(fetchscheduler.TASK_QUEUE_NAME,
				true, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		fetchscheduler.channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(fetchscheduler.channel);
		fetchscheduler.channel.basicConsume(fetchscheduler.TASK_QUEUE_NAME,
				false, consumer);

		while (true) {
			fetchscheduler.account.clear();
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String newuser = new String(delivery.getBody());
			String[] newuser_arr = newuser.split(",");
			String providername = "";
			for (String s : newuser_arr) {
				if (s.contains("email"))
					fetchscheduler.account.setEmail(s.split("email=")[1]);
				/*
				 * else if (s.contains("password"))
				 * fetchscheduler.account.setPassword(s.split("password=")[1]);
				 */
				else if (s.contains("dllrAccessToken")) {
					fetchscheduler.account.setDllrAccessToken(s
							.split("dllrAccessToken=")[1]);
				} else if (s.contains("dllrTokenSecret")) {
					fetchscheduler.account.setDllrTokenSecret(s
							.split("dllrTokenSecret=")[1]);
				} else if (s.contains("serviceProviderName"))
					providername = s.split("serviceProviderName=")[1]
							.toLowerCase();
				else if (s.contains("userId")) {
					fetchscheduler.account.setUserid(Long.parseLong(s
							.split("userId=")[1].replace("]", "")));
				}
			}

			String emailaddr = fetchscheduler.account.getEmail();
			String dllrAccessToken = fetchscheduler.account
					.getDllrAccessToken();
			String dllrTokenSecret = fetchscheduler.account
					.getDllrTokenSecret();
			/*
			 * String pass = fetchscheduler.account.getPassword(); if (pass !=
			 * null) { pass = Utility.decrypt(pass); } else { continue; }
			 */
			if (emailaddr.isEmpty() || dllrAccessToken.isEmpty()
					|| dllrTokenSecret.isEmpty() || providername.isEmpty()) {
				System.out.println("Insufficient Information about user: "
						+ fetchscheduler.account.getUserid());
				continue;
			}
			props.setProperty("com.lumlate.midas.provider", providername);
			/*
			 * String protocol =
			 * props.getProperty("com.lumlate.midas.mail.store." + providername
			 * + ".protocol"); String host =
			 * props.getProperty("com.lumlate.midas.mail." + protocol + "." +
			 * providername + ".host"); props.setProperty( "mail." + protocol +
			 * ".connectiontimeout", props.getProperty("com.lumlate.midas.mail."
			 * + protocol + ".connectiontimeout")); props.setProperty( "mail." +
			 * protocol + ".timeout",
			 * props.getProperty("com.lumlate.midas.mail." + protocol +
			 * ".timeout")); props.setProperty("mail.imap.connectiontimeout",
			 * "5000"); props.setProperty("mail.imap.timeout", "5000");
			 * props.setProperty("mail.store.protocol", protocol);
			 * props.setProperty("mail." + protocol + ".host", host);
			 * props.setProperty( "mail." + protocol + ".port",
			 * props.getProperty("com.lumlate.midas.mail." + protocol + "." +
			 * providername + ".port")); props.setProperty("mail." + protocol +
			 * ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			 * props.setProperty("mail." + protocol + ".socketFactory.fallback",
			 * "false"); fetchscheduler.NEW_USER_QUEUE_NAME
			 */
			String scope = null;
			if (providername.equalsIgnoreCase("gmail")) {
				scope = props
						.getProperty("com.lumlate.midas.oauth.gmail.scope");
			} else {
				System.out.println("Other Providers Not Supported");
				continue;
			}
			fetchscheduler.account = fetchscheduler.accountdao
					.getIDfromEmail(fetchscheduler.account);
			Date lastfetchdate = null;
			Date date = new Date();
			fetchscheduler.fetchorm.setFetchStartTime(fetchscheduler.formatter
					.format(date));
			fetchscheduler.fetchorm.setFetchEndTime(fetchscheduler.formatter
					.format(date));
			fetchscheduler.fetchorm.setUserid(fetchscheduler.account
					.getUserid());
			fetchscheduler.fetchorm.setFetchStatus("In Progress");
			fetchscheduler.fetchorm.setFetchErrorMessage("");
			fetchscheduler.fetchorm = fetchscheduler.fetchhistorydao
					.insert(fetchscheduler.fetchorm);
			try {
				fetchscheduler.inboxreader.readOauthInbox(props, scope,
						emailaddr, dllrAccessToken, dllrTokenSecret,
						lastfetchdate, fetchscheduler.NEW_USER_QUEUE_NAME);
				date = new Date();
				fetchscheduler.fetchorm
						.setFetchEndTime(fetchscheduler.formatter.format(date));
				fetchscheduler.fetchorm.setFetchStatus("Success");
				fetchscheduler.fetchorm.setFetchErrorMessage("");
				fetchscheduler.fetchhistorydao.update(fetchscheduler.fetchorm);
			} catch (Exception e) {
				date = new Date();
				fetchscheduler.fetchorm
						.setFetchEndTime(fetchscheduler.formatter.format(date));
				fetchscheduler.fetchorm.setFetchStatus("Error");
				fetchscheduler.fetchorm.setFetchErrorMessage(e.getMessage());
				fetchscheduler.fetchhistorydao.update(fetchscheduler.fetchorm);
				System.out.println("Error fetching emails for user: "
						+ fetchscheduler.account.getId());
				e.printStackTrace();
			}
		}
	}
}
