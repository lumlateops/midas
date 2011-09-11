package com.lumlate.midas.db.orm;

import java.sql.Date;

public class SubscriptionORM {

	private long id;
	private Boolean active;
	private Date createdAt;
	private Date updatedAt;
	private long retailer_id;
	private long AccountId;
	public SubscriptionORM(long id, Boolean active, Date createdAt,
			Date updatedAt, long retailer_id, long accountId) {
		super();
		this.id = id;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.retailer_id = retailer_id;
		AccountId = accountId;
	}
	public SubscriptionORM() {
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public long getRetailer_id() {
		return retailer_id;
	}
	public void setRetailer_id(long retailer_id) {
		this.retailer_id = retailer_id;
	}
	public long getAccountId() {
		return AccountId;
	}
	public void setAccountId(long accountId) {
		AccountId = accountId;
	}
	public long getId(long accountid, long retailerid) {
		return retailerid;
		// TODO Auto-generated method stub
		
	}
}
