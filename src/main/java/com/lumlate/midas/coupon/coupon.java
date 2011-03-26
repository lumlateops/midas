package com.lumlate.midas.coupon;

import java.util.Date;

import com.lumlate.midas.location.Location;
import com.lumlate.midas.meta.Product;
import com.lumlate.midas.user.Consumer;
import com.lumlate.midas.user.Retailer;

public class Coupon {
	
	private Retailer retailer;
	private Consumer consumer;
	
	private int orginalvalue;
	private int dealvalue;
	private int salepercentage;
	private int priceaftersale;
	
	private Product product;
	
	private Date expiration;
	private Date validupto;
	
	private Location validat; //at what location it is valid at
	
	private String title;  //from coupon body if different from email subject
	private String description; 
	private String content;
	private String subject;  //from email subject
	
	private boolean is_free_shipping;
	
	
	public Retailer getRetailer() {
		return retailer;
	}
	public void setRetailer(Retailer retailer) {
		this.retailer = retailer;
	}
	public int getOrginalvalue() {
		return orginalvalue;
	}
	public void setOrginalvalue(int orginalvalue) {
		this.orginalvalue = orginalvalue;
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
	public int getPriceaftersale() {
		return priceaftersale;
	}
	public void setPriceaftersale(int priceaftersale) {
		this.priceaftersale = priceaftersale;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public Date getValidupto() {
		return validupto;
	}
	public void setValidupto(Date validupto) {
		this.validupto = validupto;
	}
	public Location getValidat() {
		return validat;
	}
	public void setValidat(Location validat) {
		this.validat = validat;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isIs_free_shipping() {
		return is_free_shipping;
	}
	public void setIs_free_shipping(boolean is_free_shipping) {
		this.is_free_shipping = is_free_shipping;
	}
}
