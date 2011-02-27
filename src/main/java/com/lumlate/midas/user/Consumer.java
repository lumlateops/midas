package com.lumlate.midas.user;

import java.util.HashSet;

import com.lumlate.midas.location.Location;
import com.lumlate.midas.meta.Interest;
import com.lumlate.midas.meta.Subscription;

public class Consumer {
	
	private String firstname;
	private String lastname;
	private String username; //lumlate
	private String email_address; //external
	private HashSet<Interest> interest;
	private Location location;
	private HashSet<Subscription> subscriptions;
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public HashSet<Interest> getInterest() {
		return interest;
	}
	public void setInterest(HashSet<Interest> interest) {
		this.interest = interest;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public HashSet<Subscription> getSubscriptions() {
		return subscriptions;
	}
	public void setSubscriptions(HashSet<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	} 
	
	

}
