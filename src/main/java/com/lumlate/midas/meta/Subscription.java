package com.lumlate.midas.meta;

import com.lumlate.midas.user.Retailers;

public class Subscription {
	private Retailers retailer;
	private Vertical vertical;
	private String subscription_email;
	
	public Retailers getRetailer() {
		return retailer;
	}
	public void setRetailer(Retailers retailer) {
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
