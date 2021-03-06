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

public class FetchScheduler {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	RetailersDAO retaildao;
	MySQLAccess myaccess;
	LinkedList<String> retaildomains;
	FetchHistoryDAO fetchhistorydao;
	FetchHistoryORM fetchorm;
	AccountDAO accountdao;
	AccountORM account;
	ServiceProviderDAO providerdao;
	ServiceProviderORM provider;
	InboxReader inboxreader;

	public FetchScheduler(String mysqlhost, String dbport, String dbuser,
			String dbpassword, String db) throws Exception {
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
		String TASK_QUEUE_NAME = props
				.getProperty("com.lumlate.midas.rmq.scheduler.queue");
		String mysqlhost = props.getProperty("com.lumlate.midas.mysql.host");
		String dbport = props.getProperty("com.lumlate.midas.mysql.port");
		String dbuser = props.getProperty("com.lumlate.midas.mysql.user");
		String dbpassword = props
				.getProperty("com.lumlate.midas.mysql.password");
		String db = props.getProperty("com.lumlate.midas.mysql.database");

		FetchScheduler fetchscheduler = null;
		try {
			fetchscheduler = new FetchScheduler(mysqlhost, dbport, dbuser,
					dbpassword, db);
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

		LinkedList<AccountORM> accountlists = fetchscheduler.accountdao
				.getAllAccounts(); // TODO not scalable. Change it to iterative
									// select and call the actual email fetch in
									// seperate thread.
		for (AccountORM acc : accountlists) {
			if (acc.isActive() == false || acc.isRegisteredEmail() == false) {
				continue;
			}
			String emailaddr = acc.getEmail();
			String accesstoken = acc.getDllrAccessToken();
			String tokensecret = acc.getDllrTokenSecret();
			/*
			 * String pass = acc.getPassword();
			if (pass != null) {
				pass = Utility.decrypt(pass);
			} else {
				continue;
			}*/
			if (emailaddr==null || accesstoken==null || tokensecret==null) {
				System.out.println("Incomplete dataset for Account id:"
						+ acc.getId());
				continue;
			}
			if (emailaddr.isEmpty() || accesstoken.isEmpty() || tokensecret.isEmpty()) {
				System.out.println("Incomplete dataset for Account id:"
						+ acc.getId());
				continue;
			}
			fetchscheduler.provider.setId(acc.getProvider_id());
			String providername = fetchscheduler.providerdao
					.getProviderbyId(fetchscheduler.provider).getName()
					.toLowerCase();
			
			props.setProperty("com.lumlate.midas.provider", providername);
			
			/*
			String protocol = props.getProperty("com.lumlate.midas.mail.store."
					+ providername + ".protocol");
			String host = props.getProperty("com.lumlate.midas.mail."
					+ protocol + "." + providername + ".host");
			props.setProperty(
					"mail." + protocol + ".connectiontimeout",
					props.getProperty("com.lumlate.midas.mail." + protocol
							+ ".connectiontimeout"));
			props.setProperty(
					"mail." + protocol + ".timeout",
					props.getProperty("com.lumlate.midas.mail." + protocol
							+ ".timeout"));
			props.setProperty("mail.imap.connectiontimeout", "5000");
			props.setProperty("mail.imap.timeout", "5000");
			props.setProperty("mail.store.protocol", protocol);
			props.setProperty("mail." + protocol + ".host", host);
			props.setProperty(
					"mail." + protocol + ".port",
					props.getProperty("com.lumlate.midas.mail." + protocol
							+ "." + providername + ".port"));
			props.setProperty("mail." + protocol + ".socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail." + protocol + ".socketFactory.fallback",
					"false");
			*/
			String scope=null;
			if(providername.equalsIgnoreCase("gmail")){
				scope=props.getProperty("com.lumlate.midas.oauth.gmail.scope");
			}else{
				System.out.println("Other Providers Not Supported");
				continue;
			}
			fetchscheduler.account.clear();
			fetchscheduler.account=acc;
			fetchscheduler.account.setEmail(emailaddr);
			fetchscheduler.account = fetchscheduler.accountdao
					.getIDfromEmail(fetchscheduler.account);
			Date lastfetchdate = null;
			Date date = new Date();
			fetchscheduler.fetchorm.setFetchStartTime(fetchscheduler.formatter
					.format(date));
			String lastfetch = fetchscheduler.fetchhistorydao
					.getLastFetchTime(fetchscheduler.account.getUserid());
			if (lastfetch != null) {
				try {
					lastfetchdate = fetchscheduler.formatter.parse(lastfetch);
					if (System.currentTimeMillis() - lastfetchdate.getTime() < 2000000) {
						continue;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			fetchscheduler.fetchorm.setFetchEndTime(fetchscheduler.formatter
					.format(date));
			fetchscheduler.fetchorm.setUserid(fetchscheduler.account
					.getUserid());
			fetchscheduler.fetchorm.setFetchStatus("inprogress");
			fetchscheduler.fetchorm.setFetchErrorMessage("");
			fetchscheduler.fetchorm = fetchscheduler.fetchhistorydao
					.insert(fetchscheduler.fetchorm);
			try {
				//fetchscheduler.inboxreader.readInbox(props, protocol, host,
				//		emailaddr, pass, lastfetchdate, TASK_QUEUE_NAME);
				fetchscheduler.inboxreader.readOauthInbox(props, scope, emailaddr, accesstoken, tokensecret, lastfetchdate, TASK_QUEUE_NAME,"scheduler");
				date = new Date();
				fetchscheduler.fetchorm
						.setFetchEndTime(fetchscheduler.formatter.format(date));
				fetchscheduler.fetchorm.setFetchStatus("success");
				fetchscheduler.fetchorm.setFetchErrorMessage("");
				fetchscheduler.fetchhistorydao.update(fetchscheduler.fetchorm);
			} catch (Exception e) {
				date = new Date();
				fetchscheduler.fetchorm
						.setFetchEndTime(fetchscheduler.formatter.format(date));
				fetchscheduler.fetchorm.setFetchStatus("error");
				fetchscheduler.fetchorm.setFetchErrorMessage(e.getMessage());
				fetchscheduler.fetchhistorydao.update(fetchscheduler.fetchorm);
				System.out.println("Error fetching emails for user: "
						+ fetchscheduler.account.getId());
				e.printStackTrace();
			}
			System.out.println("============= " + new Date() + " "
					+ acc.getEmail());
			Thread.sleep(30000);
			System.out.println("============= " + new Date() + " "
					+ acc.getEmail());
		}
		fetchscheduler.clear();
		System.exit(0);
	}

}
