package com.lumlate.midas.db.orm;

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
	private Date expiryDate;
	private String url;
	private String validTo;
	private long subscription_id;
	private long userInfoid;
	private long dealEmailId;
	private boolean freeShipping;
	private long locationId;
	public DealORM(long id, Date createdAt, float dealValue,
			int discountPercentage, float originalValue, Date postDate,
			String title, Date updatedAt, Date expiryDate, String url,
			String validTo, long subscription_id, long userInfoid,
			long dealEmailId, boolean freeShipping, long locationId) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.dealValue = dealValue;
		this.discountPercentage = discountPercentage;
		this.originalValue = originalValue;
		this.postDate = postDate;
		this.title = title;
		this.updatedAt = updatedAt;
		this.expiryDate = expiryDate;
		this.url = url;
		this.validTo = validTo;
		this.subscription_id = subscription_id;
		this.userInfoid = userInfoid;
		this.dealEmailId = dealEmailId;
		this.freeShipping = freeShipping;
		this.locationId = locationId;
	}
	public DealORM() {
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
	public void setUpdatedAt(Date date) {
		this.updatedAt = date;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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
	public long getSubscription_id() {
		return subscription_id;
	}
	public void setSubscription_id(long subscription_id) {
		this.subscription_id = subscription_id;
	}
	public long getUserInfoid() {
		return userInfoid;
	}
	public void setUserInfoid(long userInfoid) {
		this.userInfoid = userInfoid;
	}
	public long getDealEmailId() {
		return dealEmailId;
	}
	public void setDealEmailId(long dealEmailId) {
		this.dealEmailId = dealEmailId;
	}
	public boolean isFreeShipping() {
		return freeShipping;
	}
	public void setFreeShipping(boolean freeShipping) {
		this.freeShipping = freeShipping;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

}
