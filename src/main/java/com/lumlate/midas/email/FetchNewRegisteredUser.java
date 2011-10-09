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
import com.lumlate.midas.db.orm.AccountORM;
import com.lumlate.midas.db.orm.FetchHistoryORM;
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
	private String rmqserver;
	private String TASK_QUEUE_NAME;
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private Gson gson;
	
	public FetchNewRegisteredUser(String mysqlhost, String dbport, String dbuser,
			String dbpassword, String db, Properties props) throws Exception {
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
		inboxreader = new InboxReader();
		rmqserver=props.getProperty("com.lumlate.midas.rmq.server");
		TASK_QUEUE_NAME=props.getProperty("com.lumlate.midas.rmq.register.queue");
		factory = new ConnectionFactory();
		factory.setHost(rmqserver);
		factory.setUsername(props.getProperty("com.lumlate.midas.rmq.username"));
		factory.setPassword(props.getProperty("com.lumlate.midas.rmq.password"));
		connection = factory.newConnection();
		channel = connection.createChannel();
		gson=new Gson();
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
			fetchscheduler = new FetchNewRegisteredUser(mysqlhost, dbport, dbuser,
					dbpassword, db, props);
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
		
		fetchscheduler.channel.queueDeclare(fetchscheduler.TASK_QUEUE_NAME, true, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		fetchscheduler.channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(fetchscheduler.channel);
		fetchscheduler.channel.basicConsume(fetchscheduler.TASK_QUEUE_NAME, false, consumer);

		while (true) {
			fetchscheduler.account.clear();
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			//String[] newuser = fetchscheduler.gson.fromJson(new String(delivery.getBody()),String[].class);
			String newuser=new String(delivery.getBody());
			String[] newuser_arr=newuser.split(",");
			
			for(String s:newuser_arr){
				String k=s.split("=")[0].replace("^[", "");
				String v=s.split("=")[1].replace("]$", "");
				if(k.contains("email"))fetchscheduler.account.setEmail(v);
			}
			
			fetchscheduler.account=fetchscheduler.accountdao.getIDfromEmail(fetchscheduler.account);
			String emailaddr = fetchscheduler.account.getEmail();
			if(emailaddr.isEmpty() || fetchscheduler.account.getPassword()==null || fetchscheduler.account.getPassword().isEmpty()){
				System.out.println("Incomplete dataset for Account id:"+fetchscheduler.account.getId());
				continue;
			}
			String pass = Utility.decrypt(fetchscheduler.account.getPassword()); //TODO Change to OAUTH
			String[] providersplit = emailaddr.split("@")[1].split("\\.");
			String provider = providersplit[providersplit.length-2];

			String protocol = props.getProperty("com.lumlate.midas.mail.store."
					+ provider + ".protocol");
			String host = props.getProperty("com.lumlate.midas.mail."
					+ protocol + "." + provider + ".host");
			props.setProperty(
					"mail." + protocol + ".connectiontimeout",
					props.getProperty("com.lumlate.midas.mail." + protocol
							+ ".connectiontimeout"));
			props.setProperty(
					"mail." + protocol + ".timeout",
					props.getProperty("com.lumlate.midas.mail." + protocol
							+ ".timeout"));
			props.setProperty("mail.store.protocol", protocol);
			props.setProperty("mail." + protocol + ".host", host);
			props.setProperty(
					"mail." + protocol + ".port",
					props.getProperty("com.lumlate.midas.mail." + protocol
							+ "." + provider + ".port"));
			props.setProperty("mail." + protocol + ".socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail." + protocol + ".socketFactory.fallback",
					"false");

			Date lastfetchdate = null;
			Date date = new Date();
			fetchscheduler.fetchorm.setFetchStartTime(fetchscheduler.formatter
					.format(date));
			String lastfetch = fetchscheduler.fetchhistorydao
					.getLastFetchTime(fetchscheduler.account.getUserid());
			if (lastfetch != null) {
				try {
					lastfetchdate = fetchscheduler.formatter.parse(lastfetch);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			fetchscheduler.fetchorm.setFetchEndTime(fetchscheduler.formatter
					.format(date));
			fetchscheduler.fetchorm.setUserid(fetchscheduler.account
					.getUserid());
			fetchscheduler.fetchorm.setFetchStatus("In Progress");
			fetchscheduler.fetchorm = fetchscheduler.fetchhistorydao
					.insert(fetchscheduler.fetchorm);
			try {
				fetchscheduler.inboxreader.readInbox(props, protocol, host,
						emailaddr, pass, lastfetchdate);
				date = new Date();
				fetchscheduler.fetchorm
						.setFetchEndTime(fetchscheduler.formatter.format(date));
				fetchscheduler.fetchorm.setFetchStatus("Success");
				fetchscheduler.fetchhistorydao.update(fetchscheduler.fetchorm);
			} catch (Exception e) {
				e.printStackTrace();
				date = new Date();
				fetchscheduler.fetchorm
						.setFetchEndTime(fetchscheduler.formatter.format(date));
				fetchscheduler.fetchorm.setFetchStatus("Error");
				fetchscheduler.fetchorm.setFetchErrorMessage(e.getMessage());
				fetchscheduler.fetchhistorydao.update(fetchscheduler.fetchorm);
			}
		}
	}
}
