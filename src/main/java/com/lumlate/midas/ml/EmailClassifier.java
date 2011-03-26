package com.lumlate.midas.ml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lumlate.midas.email.Email;

public class EmailClassifier {
	private HashSet<String> categories=new HashSet<String>();
	private HashMap<String,Boolean> dealhash=new HashMap<String,Boolean>();
	private LinkedList<Pattern> dealregexs=new LinkedList();
	private HashMap<String,Boolean> subscribehash=new HashMap<String,Boolean>();
	private Pattern[] subscriberegexs;
	private HashMap<String,Boolean> confirmhash=new HashMap<String,Boolean>();
	private LinkedList<Pattern> confirmregexs=new LinkedList();
	private HashMap<String,Boolean> spamhash=new HashMap<String,Boolean>();
	private HashMap<String,Boolean> otherhash=new HashMap<String,Boolean>();
	
	public EmailClassifier(){
		this.categories.add("deal");
		this.categories.add("subscription");
		this.categories.add("spam");
		this.categories.add("other");
		this.categories.add("confirmation");
		this.populatedealhash();
		this.populateconfirmhash();
		this.populatesubscribehash();
		this.compiledealregexes();
		this.compileconfirmregexes();
		this.compilesubscriberegexes();
		
	}

	private void compileconfirmregexes() {
		// TODO Auto-generated method stub
		
	}

	private void populateconfirmhash() {
		// TODO Auto-generated method stub
		this.confirmhash.put("confirm", true);
		this.confirmhash.put("confirmation", true);
		this.confirmhash.put("registration", true);
		this.confirmhash.put("activate", true);
	}

	private void compilesubscriberegexes() {
		// TODO Auto-generated method stub
		
	}

	private void compiledealregexes() {
		// TODO Auto-generated method stub
		Pattern r1=Pattern.compile("\\$\\d+\\.\\d+");
		Pattern r2=Pattern.compile("\\d+%");
		Pattern r3=Pattern.compile("\\$\\d+ for \\$\\d+");
		this.dealregexs.add(r1);
		this.dealregexs.add(r2);
		this.dealregexs.add(r3);
	}
	
	private void populatesubscribehash() {
		this.subscribehash.put("thank", true);
		this.subscribehash.put("welcome", true);
		this.subscribehash.put("thanks", true);
		this.subscribehash.put("joining", true);
		this.subscribehash.put("singing", true);
		this.subscribehash.put("subscription", true);
	}
	private void populatedealhash() {
		this.dealhash.put("sweepstake", true);
		this.dealhash.put("coupon", true);
		this.dealhash.put("save", true);
		this.dealhash.put("redeem", true);
		this.dealhash.put("bargain", true);
		this.dealhash.put("expire", true);
		this.dealhash.put("expiration", true);
		this.dealhash.put("valid", true);
		this.dealhash.put("offer", true);
		this.dealhash.put("promo", true);
		this.dealhash.put("code", true);
		this.dealhash.put("exclusive", true);
		this.dealhash.put("discount", true);
		this.dealhash.put("deal", true);
		this.dealhash.put("promotion", true);
		this.dealhash.put("final", true);
		this.dealhash.put("hour", true);
		this.dealhash.put("saving", true);
		this.dealhash.put("pass", true);
		this.dealhash.put("free", true);
		this.dealhash.put("shipping", true);
		this.dealhash.put("reward", true);
		this.dealhash.put("half", true);
		this.dealhash.put("off", true);
		this.dealhash.put("sale", true);
		this.dealhash.put("special", true);
		this.dealhash.put("giving", true);
		this.dealhash.put("away", true);
		this.dealhash.put("last", true);
		this.dealhash.put("chance", true);
		//this.dealhash.put("", true);		
	}

	public String classifyEmail(Email msg){
		String subject=msg.getSubject();
		String[] subjectlist=subject.split(" ");
		for(String word:subjectlist){
			if(this.confirmhash.containsKey(word)){
				if(this.categories.contains("confirmation")){
					return "confirmation";
				}
			}else if(this.subscribehash.containsKey(word)){
				if(this.categories.contains("subscription")){
					return "subscription";
				}
			}else if(this.dealhash.containsKey(word)){
				if(this.categories.contains("deal")){
					return "deal";
				}				
			}
		}
		for(Pattern p:this.dealregexs){
			Matcher m=p.matcher(subject);
			if(m.matches()){
				if(this.categories.contains("deal")){
					return "deal";
				}				
			}
		}
		/*
		for(Pattern p:this.subscriberegexs){
			Matcher m=p.matcher(subject);
			if(m.matches()){
				if(this.categories.contains("subscription")){
					return "subscription";
				}				
			}
		}*/		
		return "other";
	}
}
