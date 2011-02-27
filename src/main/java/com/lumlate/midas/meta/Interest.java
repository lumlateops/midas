package com.lumlate.midas.meta;

import java.util.HashSet;

public class Interest {
	private HashSet<String> interest;
	public Interest(){
		this.interest.add("Sports");
		this.interest.add("Movies");
		this.interest.add("Food");
		this.interest.add("Wine");
		this.interest.add("Outdoors");
		this.interest.add("Art");
		this.interest.add("Photography");
		this.interest.add("Apparel");
		this.interest.add("Electronics");
		this.interest.add("Homedecoration");
		this.interest.add("Business");
		this.interest.add("Finance");
	}
	public HashSet<String> getInterest() {
		return interest;
	}
	
	public Boolean IsInterest(String interest){
		if(this.interest.contains(interest)){
			return true;
		}
		return false;
	}
}
