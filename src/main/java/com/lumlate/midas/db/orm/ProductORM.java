package com.lumlate.midas.db.orm;

import java.sql.Date;

public class ProductORM {

	private long id;
	private Date createdAt;
	private String name;
	private Date updatedAt;
	private String vertical;
	
	public ProductORM(long id, Date createdAt, String name, Date updatedAt,
			String vertical) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.name = name;
		this.updatedAt = updatedAt;
		this.vertical = vertical;
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
	public String getVertical() {
		return vertical;
	}
	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	
}
