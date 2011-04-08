package com.lumlate.midas.coupon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;

import com.lumlate.midas.email.Email;
import com.lumlate.midas.meta.Product;
import com.lumlate.midas.user.Consumer;
import com.lumlate.midas.user.Retailer;
import com.google.gson.*;

public class CouponBuilder {
	private int dealvalue;
	private int salepercentage;
	
	private Retailer retailer;
	private Consumer consumer;
	private Product product;
	private Gson gson = new Gson();
	
	HashMap<Pattern,DealRegex> dealpatternhash = new HashMap<Pattern,DealRegex>();
	HashMap<Pattern,ValidDateRegex> datepatternhash = new HashMap<Pattern,ValidDateRegex>();
	
	File dealregexfile;
	File validregexfile;
	BufferedReader dealregexreader;
	BufferedReader valifregexreader;
	
	public CouponBuilder(String dealregexfile, String validregexfile){
		this.dealregexfile=new File(dealregexfile);
		System.out.println(dealregexfile);
		this.validregexfile=new File(validregexfile);
		this.CompileDealRegexes();
		//this.CompileDateRegexes();
	}
	
	private void CompileDealRegexes(){
		String line="";
		try {
			this.dealregexreader =  new BufferedReader(new FileReader(this.dealregexfile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while((line = this.dealregexreader.readLine()) != null){
				DealRegex regex = this.gson.fromJson(line, DealRegex.class);
				this.dealpatternhash.put(Pattern.compile(regex.getPattern()), regex);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void CompileDateRegexes(){
		String line="";
		try {
			this.valifregexreader =  new BufferedReader(new FileReader(this.validregexfile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while((line = this.valifregexreader.readLine()) != null){
				ValidDateRegex regex = this.gson.fromJson(line, ValidDateRegex.class);
				this.datepatternhash.put(Pattern.compile(regex.getPattern()), regex);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public Coupon BuildCoupon(Email email){
		Coupon coupon=new Coupon();
		try {
			ExtractDealValue(email.getSubject());
			ExtractExpirationDates(email.getSubject());
			this.ExtractRetailer(email, coupon);
			this.ExtractConsumer(email, coupon);
			this.ExtractProduct();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//return coupon;
		return coupon;
	}
	
	private void ExtractProduct() {
		// TODO Auto-generated method stub
		
	}
	private Coupon ExtractConsumer(Email email, Coupon coupon) {
		this.consumer=new Consumer();
		if(email.getTo()!=null){
			consumer.setEmail_address(email.getTo());
		}
		return coupon;
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
	
	private ValidDateRegex ExtractExpirationDates(String text) {
		if(text.isEmpty()){
			return null;
		}
		for(Pattern key:this.datepatternhash.keySet()){
			Matcher matcher = key.matcher(text);
			if(matcher.matches()){
				return this.datepatternhash.get(key);
			}
		}
		return null;
	}

	private void ExtractDealValue(String text) throws Exception{
		if(text.isEmpty()){
			return;
		}
		for(Pattern key:this.dealpatternhash.keySet()){
			Matcher matcher = key.matcher(text);
			if(matcher.matches()){
				if(this.dealpatternhash.get(key).getIs_percentage()){
					if(this.dealpatternhash.get(key).getIndexes().length>1){
						this.salepercentage=Integer.parseInt(Collections.max(Arrays.asList(this.dealpatternhash.get(key).getIndexes())));
					}else if(this.dealpatternhash.get(key).getIndexes().length==1){
						this.salepercentage=Integer.parseInt(this.dealpatternhash.get(key).getIndexes()[0]);
					}
				}else if(this.dealpatternhash.get(key).getIs_absolute()){
					if(this.dealpatternhash.get(key).getIndexes().length==2){
						int value1=Integer.parseInt(this.dealpatternhash.get(key).getIndexes()[0]);
						int value2=Integer.parseInt(this.dealpatternhash.get(key).getIndexes()[1]);
						if(value1>value2 && value2>0){
							this.salepercentage=value2*100/value1;
						}else if(value1<value2 && value1>0){
							this.salepercentage=value1*100/value2;
						}
					}else if(this.dealpatternhash.get(key).getIndexes().length==1){
						this.dealvalue=Integer.parseInt(this.dealpatternhash.get(key).getIndexes()[0]);
					}
				}
			}
		}
	}
	
	public void Close() throws Throwable{
		this.dealregexreader.close();
		this.valifregexreader.close();
	}
}
