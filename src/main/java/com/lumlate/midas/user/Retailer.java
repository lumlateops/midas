package com.lumlate.midas.user;

import java.util.HashSet;

import com.lumlate.midas.meta.Vertical;

public class Retailer {

	public String name;
	public String description;
	private String domain;
	public HashSet<Vertical> vertical;
	public HashSet<String> email_address;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashSet<Vertical> getVertical() {
		return vertical;
	}
	public void setVertical(HashSet<Vertical> vertical) {
		this.vertical = vertical;
	}
	public HashSet<String> getEmail_address() {
		return email_address;
	}
	public void setEmail_address(HashSet<String> email_address) {
		this.email_address = email_address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
}
