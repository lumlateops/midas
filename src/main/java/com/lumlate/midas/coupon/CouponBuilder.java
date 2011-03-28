package com.lumlate.midas.coupon;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.lumlate.midas.email.Email;
import com.lumlate.midas.location.Location;
import com.lumlate.midas.meta.Product;
import com.lumlate.midas.user.Consumer;
import com.lumlate.midas.user.Retailer;
import com.lumlate.midas.utils.HtmlParser;

public class CouponBuilder {
	private int orginalvalue;
	private int dealvalue;
	private int salepercentage;
	private int priceaftersale;
	
	private Retailer retailer;
	private Consumer consumer;
	private Product product;
	
	Pattern dollar1;
	Pattern dollar2;
	Pattern dollar3;
	Pattern dollar4;
	Pattern dollar5;
	Pattern dollar6;
	Pattern dollar7;
	Pattern dollar8;
	Pattern dollar9;
	Pattern dollar10;
	Pattern dollar11;
	Pattern dollar12;
	
	public CouponBuilder(){
		this.CompileDealRegexes();	
	}
	
	private void CompileDealRegexes(){
		this.dollar1=Pattern.compile("(.*)(\\$)(\\d+\\.\\d+)(.*\\$)(\\d+\\.\\d+)(.*)"); //$40.00 off of $100.00
		this.dollar2=Pattern.compile("(.*)(\\$)(\\d+)(.*\\$)(\\d+)(.*)"); //$40 off of $100
		this.dollar3=Pattern.compile("(.*)(\\d+)(-)(\\d+)(\\%)(.*)"); //40-50%
		this.dollar4=Pattern.compile("(.*)(\\s+)(\\d+)(\\% [Oo]ff)(.*)"); //40% off
		this.dollar5=Pattern.compile("(.*)(\\$)(\\d+)(\\s+)([Oo]ff)(.*)"); //$40 off
		this.dollar6=Pattern.compile("(.*)(\\s+)(\\d+)(\\%.*)"); //40%
		this.dollar7=Pattern.compile("(.*)([Ss]ave up to $)(\\d+)(.*)"); //save up to $40
		this.dollar8=Pattern.compile("(.*)([Ss]ave up to)(\\s+)(\\d+)(\\%.*)"); //save up to 40%
		this.dollar9=Pattern.compile("(.*)([Fr]om $)(\\d+)(.*)"); //from $40
		this.dollar11=Pattern.compile("(.*)($)(\\d+)(.*)"); //$40
		this.dollar12=Pattern.compile("(.*)(\\d+)(\\%.*)"); //40%
	}
	
	private void CompileDateRegexes(){
		
	}
	
	public Coupon BuildCoupon(Email email){
		Coupon coupon=new Coupon();
		try {
			if(this.ExtractDealValue(email.getHtml().getTitle())==true){
				
			}
			//this.ExtractExpirationDates();
			//this.ExtractRetailer();
			//this.ExtractConsumer();
			//this.ExtractProduct();
			//this.Extractdollars("5 hours only! 15% or 20% everything at");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//return coupon;
		return coupon;
	}
	
	private void ExtractProduct() {
		// TODO Auto-generated method stub
		
	}
	private void ExtractConsumer() {
		// TODO Auto-generated method stub
		
	}
	private Coupon ExtractRetailer(Email email,Coupon coupon) throws Exception {
		this.retailer=new Retailer();
		if(email.getFromemail()!=null){
			this.retailer.setDomain(email.getFromemail());
			if(email.getFromname()!=null){
				this.retailer.setName(email.getFromname());
			}
		}
		if(email.getFromemail()!=null){
			this.retailer.setSubscription_email(new InternetAddress(email.getFromemail()));
		}
		coupon.setRetailer(retailer);
		return coupon;
	}
	private void ExtractExpirationDates() {
		// TODO Auto-generated method stub
		
	}
	private Boolean ExtractDealValue(String text) throws Throwable{
		if(text.isEmpty()){
			return false;
		}
		//System.out.println("Vipul "+text);
		Matcher matcher1 = this.dollar1.matcher(text);
		Matcher matcher2 = this.dollar2.matcher(text);
		Matcher matcher3 = this.dollar3.matcher(text);
		Matcher matcher4 = this.dollar4.matcher(text);
		Matcher matcher5 = this.dollar5.matcher(text);
		Matcher matcher6 = this.dollar6.matcher(text);
		
		if(matcher1.matches()){
			System.out.println(text+" "+matcher1.group(3)+" "+matcher1.group(5));
			return true;
		}
		else if(matcher2.matches()){
			System.out.println(text+" "+matcher2.group(3)+" "+matcher2.group(5));
			return true;
		}
		else if(matcher3.matches()){
			System.out.println(text+" "+matcher3.group(2)+" "+matcher3.group(4));
			return true;
		}
		else if(matcher4.matches()){
			System.out.println(text+" "+matcher4.group(3));
			return true;
		}
		else if(matcher5.matches()){
			System.out.println(text+" "+matcher5.group(3));
			return true;
		}
		else if(matcher6.matches()){
			System.out.println(text+" "+matcher6.group(3));
			return true;
		}else{
			return false;
		}
	}
}
