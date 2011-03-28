package com.lumlate.midas.user;

import java.util.HashSet;

import com.lumlate.midas.location.Location;
import com.lumlate.midas.meta.Subscription;
import com.lumlate.midas.meta.Vertical;

public class Consumer {
	
	private String firstname;
	private String lastname;
	private String email_address;
	
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
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	
	
	
}
