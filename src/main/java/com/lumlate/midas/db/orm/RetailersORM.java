package com.lumlate.midas.db.orm;

import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;

import javax.mail.internet.InternetAddress;

import com.lumlate.midas.meta.Vertical;

public class RetailersORM {

	private long id;
	private String domain;
	private String name;
	private String image;
	private String createdAT;
	private String updatedAT;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCreatedAT() {
		return createdAT;
	}
	public void setCreatedAT(String createdAT) {
		this.createdAT = createdAT;
	}
	public String getUpdatedAT() {
		return updatedAT;
	}
	public void setUpdatedAT(String updatedAT) {
		this.updatedAT = updatedAT;
	}
}
