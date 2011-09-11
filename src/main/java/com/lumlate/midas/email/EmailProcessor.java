package com.lumlate.midas.email;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.*;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.google.gson.Gson;

import com.lumlate.midas.coupon.Coupon;
import com.lumlate.midas.coupon.CouponBuilder;
import com.lumlate.midas.db.TempOP;
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
	private EmailParser parser=new EmailParser();
	private EmailClassifier emailclassifier=new EmailClassifier();
	private UrlParser urlparser = new UrlParser();
	private String dealregexfile;
	private String dateregexfile;

	public void readImapInbox(Session session) throws Exception{
		CouponBuilder cb=new CouponBuilder(dealregexfile,dateregexfile);		
		Gson gson = new Gson();
		String rmqserver="rmq01.deallr.com";
		String TASK_QUEUE_NAME="gmail_oauth";
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rmqserver);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			ByteArrayInputStream in = new ByteArrayInputStream(delivery.getBody());
			MimeMessage msg=new MimeMessage(session, in);
			//Message msg = in.read();
			
			System.out.println(" [x] Received '" + msg.getSubject() + "'");
			
			try {
				this.parser.setMsg(msg);
				//message[i].setFlag(Flags.Flag.SEEN, true);
				Email email = this.parser.parser(msg);
				HtmlParser htmlparser = new HtmlParser();
				if(email.getContent()==null)continue;
				boolean parseflag=false; // for tracking if the parsing was successfull
				if(email.isIs_html() && !email.getContent().isEmpty()){
					parseflag=true;
					htmlparser.parsehtml(email.getContent());
				}else if(email.isIs_plaintext() && !email.getContent().isEmpty()){
					parseflag=true;
					htmlparser.setRawtext(email.getContent());
				}else if(!email.getContent().isEmpty()){
					parseflag=true;
					htmlparser.parsehtml(email.getContent());
				}

				if(htmlparser!=null){
					email.setHtml(htmlparser);
				}

				String category=this.emailclassifier.classifyEmail(email);

				if(!category.isEmpty()){
					email.setCategory(category);
				}
				//System.out.println(gson.toJson(email));
				if(category.equalsIgnoreCase("deal") || category.equalsIgnoreCase("subscription")){ //TODO convert category from string to enum
					try {
						Coupon coupon=cb.BuildCoupon(email);
						if(coupon!=null){
							
							System.out.println(gson.toJson(tempop));
							//msg.setFlag(Flags.Flag.SEEN, true);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				else{
					//System.out.println("NOT CATEGORIZED "+email.getSubject());
				}
				//TODO		persist email // can and probably should be asynchronous using a queue
			} catch (Exception e) {
				System.out.println(e.toString());
				}
			System.out.println(" [x] Done" );
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}

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

	public static void main(String[] args) {
		EmailProcessor newGmailClient=new EmailProcessor();
		Properties props=System.getProperties();
		Session session=Session.getDefaultInstance(props, null);
		newGmailClient.setDealregexfile(args[0]);
		newGmailClient.setDateregexfile(args[1]);
		try {
			newGmailClient.readImapInbox(session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
