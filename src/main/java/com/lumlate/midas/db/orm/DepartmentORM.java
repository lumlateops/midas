package com.lumlate.midas.db.orm;

import java.sql.Date;

public class DepartmentORM {

	private long id;
	private String createdAt;
	private String email;
	private String logo;
	private String name;
	private String updatedAt;
	private long retailerId;

	public DepartmentORM(String createdAt, String email, String logo,
			String name, String updatedAt, long retailerId) {
		super();
		this.createdAt = createdAt;
		this.email = email;
		this.logo = logo;
		this.name = name;
		this.updatedAt = updatedAt;
		this.retailerId = retailerId;
	}

	public DepartmentORM() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String date) {
		this.createdAt = date;
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

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(long retailerId) {
		this.retailerId = retailerId;
	}

	public void clear() {
		id = 0;
		createdAt = null;
		email = null;
		logo = null;
		name = null;
		updatedAt = null;
		retailerId = 0;
	}

}
