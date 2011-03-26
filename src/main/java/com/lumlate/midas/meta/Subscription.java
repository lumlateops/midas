package com.lumlate.midas.meta;

import com.lumlate.midas.user.Retailer;

public class Subscription {
	private Retailer retailer;
	private Vertical vertical;
	private String subscription_email;
	
	public Retailer getRetailer() {
		return retailer;
	}
	public void setRetailer(Retailer retailer) {
		this.retailer = retailer;
	}
	public Vertical getVertical() {
		return vertical;
	}
	public void setVertical(Vertical vertical) {
		this.vertical = vertical;
	}
	public String getSubscription_email() {
		return subscription_email;
	}
	public void setSubscription_email(String subscription_email) {
		this.subscription_email = subscription_email;
	}
}
