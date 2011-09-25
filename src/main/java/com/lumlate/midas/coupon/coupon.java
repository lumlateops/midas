package com.lumlate.midas.coupon;

import java.util.Date;

import com.lumlate.midas.db.orm.RetailersORM;
import com.lumlate.midas.location.Location;
import com.lumlate.midas.meta.Product;
import com.lumlate.midas.user.Consumer;

public class Coupon {
	
	private RetailersORM retailer;
	private Consumer consumer;
	
	private int dealvalue;
	private int salepercentage;
	
	private Product product;
	
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
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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
}
