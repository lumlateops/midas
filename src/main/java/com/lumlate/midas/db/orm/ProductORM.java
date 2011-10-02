package com.lumlate.midas.db.orm;

import java.sql.Date;

public class ProductORM {

	private long id;
	private Date createdAt;
	private String name;
	private Date updatedAt;
	private String dealcategory;

	public ProductORM() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getDealcategory() {
		return dealcategory;
	}

	public void setDealcategory(String dealcategory) {
		this.dealcategory = dealcategory;
	}

}
