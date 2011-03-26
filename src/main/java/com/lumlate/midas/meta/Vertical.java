package com.lumlate.midas.meta;

import java.util.HashSet;

public class Vertical {
	private HashSet<String> vertical;
	public Vertical(){
		this.vertical.add("Sports");
		this.vertical.add("Movies");
		this.vertical.add("Restaurant");
		this.vertical.add("Liquor");
		this.vertical.add("Outdoors");
		this.vertical.add("Art");
		this.vertical.add("Photography");
		this.vertical.add("Apparel");
		this.vertical.add("Electronics");
		this.vertical.add("Furniture");
		this.vertical.add("Finance");
		this.vertical.add("Grocery");
		this.vertical.add("Local");
		this.vertical.add("Travel");
		this.vertical.add("DailyDeal");
	}
	
	public HashSet<String> getverticals() {
		return vertical;
	}
	
	public Boolean Isvertical(String vertical){
		if(this.vertical.contains(vertical)){
			return true;
		}
		return false;
	}
}
