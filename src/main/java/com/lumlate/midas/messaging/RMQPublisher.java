package com.lumlate.midas.messaging;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class RMQPublisher {

	public ConnectionFactory factory;
	public String QUEUE_NAME;
	public String RMQserver;
	public Connection connection;
	public Channel channel;
	
	public void RMWConsumer(String server,String queue) throws Exception{
		QUEUE_NAME=queue;
		RMQserver=server;
	    factory = new ConnectionFactory();
	    factory.setHost(this.RMQserver);
	    connection = factory.newConnection();
	    channel = connection.createChannel();
	}
	
	public void publish(String message) throws Exception{
	    channel.queueDeclare(this.QUEUE_NAME, true, false, false, null); //durable=true
	    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
	}
	
	public void close() throws Exception{
		channel.close();
	    connection.close();
	}
}
