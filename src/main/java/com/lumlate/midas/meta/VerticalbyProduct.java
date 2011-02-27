package com.lumlate.midas.meta;

import java.util.HashSet;

public class VerticalbyProduct {
	private HashSet<String> vertical;
	public VerticalbyProduct(){
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
	}
	public HashSet<String> getvertical() {
		return vertical;
	}
	
	public Boolean Isvertical(String vertical){
		if(this.vertical.contains(vertical)){
			return true;
		}
		return false;
	}
}
