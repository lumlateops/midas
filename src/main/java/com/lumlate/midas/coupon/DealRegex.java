package com.lumlate.midas.coupon;

import java.util.Vector;
import java.util.regex.Pattern;

public class DealRegex {
	private int id;
	private String pattern;
	private String indexes="";
	private Boolean is_absolute=false;
	private Boolean is_percentage=false;
	private String desc;
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Boolean getIs_absolute() {
		return is_absolute;
	}
	public void setIs_absolute(Boolean is_absolute) {
		this.is_absolute = is_absolute;
	}
	public Boolean getIs_percentage() {
		return is_percentage;
	}
	public void setIs_percentage(Boolean is_percentage) {
		this.is_percentage = is_percentage;
	}
	public String[] getIndexes(){
		return this.indexes.split(",");
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
