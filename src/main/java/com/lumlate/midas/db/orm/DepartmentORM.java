package com.lumlate.midas.db.orm;

import java.sql.Date;

public class DepartmentORM {
	
	private long id;
	private Date createdAt;
	private String email;
	private String logo;
	private String name;
	private Date updatedAt;
	private long retailerId;
	
	public DepartmentORM(long id, Date createdAt, String email, String logo,
			String name, Date updatedAt, long retailerId) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.email = email;
		this.logo = logo;
		this.name = name;
		this.updatedAt = updatedAt;
		this.retailerId = retailerId;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public long getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(long retailerId) {
		this.retailerId = retailerId;
	}
	
}
