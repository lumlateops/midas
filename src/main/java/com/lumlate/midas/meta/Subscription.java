package com.lumlate.midas.meta;

import java.util.HashSet;

import com.lumlate.midas.user.Retailer;

public class Subscription {
	private Retailer retailer;
	private HashSet<String> subscription_emails;
	private VerticalbyProduct vertical;
	
	public Retailer getRetailer() {
		return retailer;
	}
	public void setRetailer(Retailer retailer) {
		this.retailer = retailer;
	}
	public HashSet<String> getSubscription_emails() {
		return subscription_emails;
	}
	public void setSubscription_emails(HashSet<String> subscription_emails) {
		this.subscription_emails = subscription_emails;
	}
	public VerticalbyProduct getVertical() {
		return vertical;
	}
	public void setVertical(VerticalbyProduct vertical) {
		this.vertical = vertical;
	}
	
	
}
