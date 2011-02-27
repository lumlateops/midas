package com.lumlate.midas.user;

import java.util.HashSet;

import com.lumlate.midas.meta.VerticalbyProduct;

public class Retailer {

	public String name;
	public String description;
	public HashSet<VerticalbyProduct> vertical;
	public HashSet<String> email_address;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashSet<VerticalbyProduct> getVertical() {
		return vertical;
	}
	public void setVertical(HashSet<VerticalbyProduct> vertical) {
		this.vertical = vertical;
	}
	public HashSet<String> getEmail_address() {
		return email_address;
	}
	public void setEmail_address(HashSet<String> email_address) {
		this.email_address = email_address;
	}
	
	
}