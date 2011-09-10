package com.lumlate.midas.messaging;

import java.io.IOException;
import java.util.LinkedList;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class RMQConsumer {

	public ConnectionFactory factory;
	public String QUEUE_NAME;
	public String RMQserver;
	public Connection connection;
	public Channel channel;
	public QueueingConsumer consumer;
	
	public void RMWConsumer(String server,String queue) throws Exception{
		QUEUE_NAME=queue;
		RMQserver=server;
	    factory = new ConnectionFactory();
	    factory.setHost(this.RMQserver);
	    connection = factory.newConnection();
	    channel = connection.createChannel();
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    consumer = new QueueingConsumer(channel);
	    channel.basicConsume(QUEUE_NAME, true, consumer);
	}
	
	public LinkedList<String> consume() throws Exception{
		LinkedList<String> returnlist = new LinkedList<String>();
	    while (true) {
	      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	      returnlist.add(new String(delivery.getBody()));
	    }
	}
	
	public void close() throws Exception{
		channel.close();
	    connection.close();
	}
}
