package com.lumlate.midas.coupon;

import java.util.regex.Pattern;

public class DateRegex {
	private int id;
	private String pattern;
	private String indexes="";
	private Boolean is_date=false;
	private Boolean is_day=false;
	private Boolean is_week=false;
	private Boolean is_hour=false;
	private Boolean is_other=false;
	private String desc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public Boolean getIs_date() {
		return is_date;
	}
	public void setIs_date(Boolean is_date) {
		this.is_date = is_date;
	}
	public Boolean getIs_day() {
		return is_day;
	}
	public void setIs_day(Boolean is_day) {
		this.is_day = is_day;
	}
	public Boolean getIs_week() {
		return is_week;
	}
	public void setIs_week(Boolean is_week) {
		this.is_week = is_week;
	}
	public Boolean getIs_hour() {
		return is_hour;
	}
	public void setIs_hour(Boolean is_hour) {
		this.is_hour = is_hour;
	}
	public Boolean getIs_other() {
		return is_other;
	}
	public void setIs_other(Boolean is_other) {
		this.is_other = is_other;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
