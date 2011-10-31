package com.lumlate.midas.email;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.google.gson.Gson;

import com.lumlate.midas.coupon.Coupon;
import com.lumlate.midas.coupon.CouponBuilder;
import com.lumlate.midas.db.PersistData;
import com.lumlate.midas.email.Email;
import com.lumlate.midas.email.EmailParser;
import com.lumlate.midas.ml.EmailClassifier;
import com.lumlate.midas.utils.HtmlParser;
import com.lumlate.midas.utils.UrlParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class EmailProcessor {
	private String receivingHost;
	private EmailParser parser = new EmailParser();
	private EmailClassifier emailclassifier = new EmailClassifier();
	private UrlParser urlparser = new UrlParser();
	private PersistData persistdata;
	private String dealregexfile;
	private String dateregexfile;
	private String productfile;
	private String rmqserver;
	private String TASK_QUEUE_NAME;
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private Gson gson;
	
	public EmailProcessor(Properties props) throws Exception{
		gson = new Gson();
		rmqserver=props.getProperty("com.lumlate.midas.rmq.server");
		factory = new ConnectionFactory();
		factory.setHost(rmqserver);
		factory.setUsername(props.getProperty("com.lumlate.midas.rmq.username"));
		factory.setPassword(props.getProperty("com.lumlate.midas.rmq.password"));
		connection = factory.newConnection();
		channel = connection.createChannel();
		persistdata=new PersistData(props.getProperty("com.lumlate.midas.mysql.host"), props.getProperty("com.lumlate.midas.mysql.port"), props.getProperty("com.lumlate.midas.mysql.user"), props.getProperty("com.lumlate.midas.mysql.password"), props.getProperty("com.lumlate.midas.mysql.database"));
	}

	public void readImapInbox(Session session) throws Exception {
		CouponBuilder cb = new CouponBuilder(dealregexfile, dateregexfile, productfile);

		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			ByteArrayInputStream in = new ByteArrayInputStream(
					delivery.getBody());
			MimeMessage msg = new MimeMessage(session, in);

			try {
				this.parser.setMsg(msg);
				// message[i].setFlag(Flags.Flag.SEEN, true);
				Email email = this.parser.parser(msg);
				HtmlParser htmlparser = new HtmlParser();
				if (email.getContent() == null)
					continue;
				boolean parseflag = false; // for tracking if the parsing was
											// successfull
				if (email.isIs_html() && !email.getContent().isEmpty()) {
					parseflag = true;
					htmlparser.parsehtml(email.getContent());
				} else if (email.isIs_plaintext()
						&& !email.getContent().isEmpty()) {
					parseflag = true;
					htmlparser.setRawtext(email.getContent().toLowerCase());
				} else if (!email.getContent().isEmpty()) {
					parseflag = true;
					htmlparser.parsehtml(email.getContent());
				}

				if (htmlparser != null) {
					email.setHtml(htmlparser);
				}

				String category = this.emailclassifier.classifyEmail(email);

				if (!category.isEmpty()) {
					email.setCategory(category);
				}
				if (category.equalsIgnoreCase("deal")
						|| category.equalsIgnoreCase("subscription")) { 
					try {
						Coupon coupon = cb.BuildCoupon(email);
						if (coupon != null) {
							this.persistdata.persist(email, coupon);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("NOT CATEGORIZED " + email.getSubject());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}

	}

	public String getProductfile() {
		return productfile;
	}

	public void setProductfile(String productfile) {
		this.productfile = productfile;
	}

	public String getReceivingHost() {
		return receivingHost;
	}

	public void setReceivingHost(String receivingHost) {
		this.receivingHost = receivingHost;
	}

	public String getDealregexfile() {
		return dealregexfile;
	}

	public void setDealregexfile(String dealregexfile) {
		this.dealregexfile = dealregexfile;
	}

	public String getDateregexfile() {
		return dateregexfile;
	}

	public void setDateregexfile(String dateregexfile) {
		this.dateregexfile = dateregexfile;
	}
	public String getTASK_QUEUE_NAME() {
		return TASK_QUEUE_NAME;
	}

	public void setTASK_QUEUE_NAME(String tASK_QUEUE_NAME) {
		TASK_QUEUE_NAME = tASK_QUEUE_NAME;
	}

	public static void main(String[] args) {
		if(args.length<4){
			System.out.println("Usage: dealregex dateregex productfile fetchtype properties");
		}
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(args[4]));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EmailProcessor newGmailClient = null;
		try {
			newGmailClient = new EmailProcessor(props);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(true);
		newGmailClient.setDealregexfile(args[0]);
		newGmailClient.setDateregexfile(args[1]);
		newGmailClient.setProductfile(args[2]);
		if(args[3].equalsIgnoreCase("scheduler"))
			newGmailClient.setTASK_QUEUE_NAME(props.getProperty("com.lumlate.midas.rmq.scheduler.queue"));
		else if(args[3].equalsIgnoreCase("newuser"))
			newGmailClient.setTASK_QUEUE_NAME(props.getProperty("com.lumlate.midas.rmq.register.publish.queue"));
		else{
			System.out.println("Dont understand the fetchtype. FetchType can only be scheduler or newuser");
			System.exit(0);
		}
		
		try {
			newGmailClient.readImapInbox(session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
