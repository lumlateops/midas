package com.lumlate.midas.coupon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;

import com.lumlate.midas.coupon.Coupon;
import com.lumlate.midas.coupon.DateRegex;
import com.lumlate.midas.coupon.DealRegex;
import com.lumlate.midas.db.orm.RetailersORM;
import com.lumlate.midas.email.Email;
import com.lumlate.midas.meta.Product;
import com.lumlate.midas.user.Consumer;
import com.google.gson.*;

public class CouponBuilder {
	private Gson gson = new Gson();	
	HashMap<Pattern,DealRegex> dealpatternhash = new HashMap<Pattern,DealRegex>();
	HashMap<Pattern,DateRegex> datepatternhash = new HashMap<Pattern,DateRegex>();
	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	File dealregexfile;
	File validregexfile;
	BufferedReader dealregexreader;
	BufferedReader valifregexreader;
	
	public CouponBuilder(String dealregexfile, String validregexfile){
		this.dealregexfile=new File(dealregexfile);
		this.validregexfile=new File(validregexfile);
		this.CompileDealRegexes();
		this.CompileDateRegexes();
	}
	
	public Coupon BuildCoupon(Email email){
		Coupon coupon=new Coupon();
		try {
			ExtractDealValue(email.getSubject(),coupon);
			ExtractExpirationDates(email.getHtml().getRawtext(),coupon);
			ExtractRetailer(email, coupon); 
			ExtractConsumer(email, coupon);
			//this.ExtractProduct();
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
		return coupon;
	}
	
	private void CompileDealRegexes(){
		String line="";
		try {
			this.dealregexreader =  new BufferedReader(new FileReader(this.dealregexfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while((line = this.dealregexreader.readLine()) != null){
				DealRegex regex = this.gson.fromJson(line, DealRegex.class);
				this.dealpatternhash.put(Pattern.compile(regex.getPattern(), Pattern.CASE_INSENSITIVE), regex);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void CompileDateRegexes(){
		String line="";
		try {
			this.valifregexreader =  new BufferedReader(new FileReader(this.validregexfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while((line = this.valifregexreader.readLine()) != null){
				DateRegex regex = this.gson.fromJson(line, DateRegex.class);
				this.datepatternhash.put(Pattern.compile(regex.getPattern(),Pattern.CASE_INSENSITIVE), regex);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private void ExtractDealValue(String text, Coupon coupon) throws Exception{
		if(text.isEmpty()){
			return;
		}
		for(Pattern key:this.dealpatternhash.keySet()){
			Matcher matcher = key.matcher(text);
			if(matcher.matches()){
				if(this.dealpatternhash.get(key).getIs_percentage()){
					if(this.dealpatternhash.get(key).getIndexes().length>1){
						int max=0;
						for(String index:this.dealpatternhash.get(key).getIndexes()){
							int tempval=Integer.parseInt(matcher.group(Integer.parseInt(index)));
							if(tempval>max){
								max=tempval;
							}
						}
						coupon.setSalepercentage(max);
					}else if(this.dealpatternhash.get(key).getIndexes().length==1){
						coupon.setSalepercentage(Integer.parseInt(matcher.group(Integer.parseInt(this.dealpatternhash.get(key).getIndexes()[0]))));
					}
				}else if(this.dealpatternhash.get(key).getIs_absolute()){
					if(this.dealpatternhash.get(key).getIndexes().length==2){ //Be careful this might give wrong output for entries like $5 and $10
						int value1=Integer.parseInt(matcher.group(Integer.parseInt(this.dealpatternhash.get(key).getIndexes()[0])));
						int value2=Integer.parseInt(matcher.group(Integer.parseInt(this.dealpatternhash.get(key).getIndexes()[1])));
						if(value1>value2 && value2>0){
							coupon.setSalepercentage(value2*100/value1);
						}else if(value1<value2 && value1>0){
							coupon.setSalepercentage(value1*100/value2);
						}
					}else if(this.dealpatternhash.get(key).getIndexes().length==1){
						coupon.setDealvalue(Integer.parseInt(matcher.group(Integer.parseInt(this.dealpatternhash.get(key).getIndexes()[0]))));
					}
				}
			}
		}
	}

	private void ExtractExpirationDates(String text, Coupon coupon) throws Exception {
		if(text.isEmpty()){
			return;
		}
		//text="sdhfgs dskfg df febuary 14dfjsf";
		for(Pattern key:this.datepatternhash.keySet()){
			Matcher matcher = key.matcher(text);
			if(matcher.matches()){
				String index=this.datepatternhash.get(key).getIndexes();
				//System.out.println("------------------------------- "+index);
				//System.out.println(this.datepatternhash.get(key));
				String expdate=getExpirationDate(this.datepatternhash.get(key),matcher.group(Integer.parseInt(index)));
				if(this.datepatternhash.get(key).getIs_date()){
					coupon.setExpiration(expdate);
				}else{
					coupon.setValidupto(expdate);
				}
			}
		}
		if(coupon.getExpiration()==null || coupon.getExpiration().isEmpty()){
			long now=System.currentTimeMillis();
			long weekfromnow=now+7*24*60*60*1000;
			Date expirydate=new Date(weekfromnow);
			coupon.setExpiration(dateFormat.format(expirydate));
		}
	}

	private String getExpirationDate(DateRegex data, String value) throws Exception{
		DateFormat formatter;
		DateFormat newformatter=new SimpleDateFormat("yyyy-mm-dd 00:00:00");
		if(!value.isEmpty()){
			if(data.getIs_date()){
				formatter = new SimpleDateFormat(data.getDesc());
				 return newformatter.format((Date)formatter.parse(value));
			}
		}
		return value;
	}
	
	private void ExtractProduct() {
	}
	
	private Coupon ExtractConsumer(Email email, Coupon coupon) {
		Consumer consumer=new Consumer();
		if(email.getToemail()!=null){
			consumer.setEmail_address(email.getToemail());
		}
		return coupon;
	}
	
	private Coupon ExtractRetailer(Email email,Coupon coupon) throws Exception {
		RetailersORM retailer=new RetailersORM();
		if(email.getFromemail()!=null){
			String[] addrs=email.getFromemail().split("@")[1].split("\\.");
			String fromdomain="";
			for(int i=addrs.length-2;i<=addrs.length-1;i++){
				fromdomain+=addrs[i]+".";
			}
			retailer.setDomain(fromdomain.substring(0, fromdomain.length()-1));
			if(email.getFromname()!=null){
				retailer.setName(email.getFromname());
			}
		}
		coupon.setRetailer(retailer);
		return coupon;
	}
	
	public void Close() throws Throwable{
		this.dealregexreader.close();
		this.valifregexreader.close();
	}
}
