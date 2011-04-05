package com.lumlate.midas.coupon;

import java.util.regex.Pattern;

public class ValidDateRegex {
	private String pattern;
	private String indexes="";
	private Boolean is_absolute=false;
	
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
	
	
}
