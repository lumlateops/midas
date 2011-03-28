package com.lumlate.midas.user;

import java.util.HashSet;

import javax.mail.internet.InternetAddress;

import com.lumlate.midas.meta.Vertical;

public class Retailer {

	public String name;
	private String domain;
	public InternetAddress subscription_email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public InternetAddress getSubscription_email() {
		return subscription_email;
	}
	public void setSubscription_email(InternetAddress subscription_email) {
		this.subscription_email = subscription_email;
	}
}
