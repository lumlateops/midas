package com.lumlate.midas.coupon;

import java.util.regex.Pattern;

public class ValidDateRegex {
	private int id;
	private String pattern;
	private String indexes="";
	private Boolean is_absolute=false;
	private String desc;
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getIndexes() {
		return indexes;
	}
	public void setIndexes(String indexes) {
		this.indexes = indexes;
	}
	public Boolean getIs_absolute() {
		return is_absolute;
	}
	public void setIs_absolute(Boolean is_absolute) {
		this.is_absolute = is_absolute;
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
