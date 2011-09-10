package com.lumlate.midas.db;

import java.sql.Date;

public class DealORM {

	private long id;
	private Date createdAt;
	private float dealValue;
	private int discountPercentage;
	private float originalValue;
	private Date postDate;
	private String title;
	private Date updatedAt;
	private String url;
	private String validTo;
	private long subscriptionId;
	private long userInfoid;
	
	public DealORM(long id, Date createdAt, float dealValue,
			int discountPercentage, float originalValue, Date postDate,
			String title, Date updatedAt, String url, String validTo,
			long subscriptionId, long userInfoid) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.dealValue = dealValue;
		this.discountPercentage = discountPercentage;
		this.originalValue = originalValue;
		this.postDate = postDate;
		this.title = title;
		this.updatedAt = updatedAt;
		this.url = url;
		this.validTo = validTo;
		this.subscriptionId = subscriptionId;
		this.userInfoid = userInfoid;
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
	public float getDealValue() {
		return dealValue;
	}
	public void setDealValue(float dealValue) {
		this.dealValue = dealValue;
	}
	public int getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public float getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(float originalValue) {
		this.originalValue = originalValue;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public long getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public long getUserInfoid() {
		return userInfoid;
	}
	public void setUserInfoid(long userInfoid) {
		this.userInfoid = userInfoid;
	}

}
