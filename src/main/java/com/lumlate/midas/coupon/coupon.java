package com.lumlate.midas.coupon;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.lumlate.midas.db.orm.ProductORM;
import com.lumlate.midas.db.orm.RetailersORM;
import com.lumlate.midas.location.Location;
import com.lumlate.midas.user.Consumer;

public class Coupon {
	
	private RetailersORM retailer;
	private Consumer consumer;
	
	private int dealvalue;
	private int salepercentage;
	private int originalvalue;
	
	private HashSet<ProductORM> products;
	
	private String expiration; //when there is a specifc date
	private String validupto; //time left in millisec
	
	private Location validat; //at what location it is valid at
	
	private boolean is_free_shipping;
	
	
	public RetailersORM getRetailer() {
		return retailer;
	}
	public void setRetailer(RetailersORM retailer) {
		this.retailer = retailer;
	}
	public int getDealvalue() {
		return dealvalue;
	}
	public void setDealvalue(int dealvalue) {
		this.dealvalue = dealvalue;
	}
	public int getSalepercentage() {
		return salepercentage;
	}
	public void setSalepercentage(int salepercentage) {
		this.salepercentage = salepercentage;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getValidupto() {
		return validupto;
	}
	public void setValidupto(String validupto) {
		this.validupto = validupto;
	}
	public Location getValidat() {
		return validat;
	}
	public void setValidat(Location validat) {
		this.validat = validat;
	}
	public boolean isIs_free_shipping() {
		return is_free_shipping;
	}
	public void setIs_free_shipping(boolean is_free_shipping) {
		this.is_free_shipping = is_free_shipping;
	}
	public Consumer getConsumer() {
		return consumer;
	}
	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}
	public HashSet<ProductORM> getProducts() {
		return products;
	}
	public void setProducts(HashSet<ProductORM> products) {
		this.products = products;
	}
	public int getOriginalvalue() {
		return originalvalue;
	}
	public void setOriginalvalue(int originalvalue) {
		this.originalvalue = originalvalue;
	}
}
