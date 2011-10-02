package com.lumlate.midas.db.orm;

import java.sql.Date;

public class DealORM {

	private long id;
	private String createdAt;
	private float dealValue;
	private int discountPercentage;
	private float originalValue;
	private String postDate;
	private String title;
	private String updatedAt;
	private String expiryDate;
	private String url;
	private String validTo;
	private long subscription_id;
	private long userInfoid;
	private long dealEmailId;
	private boolean freeShipping;
	private long locationId;
	private String tags;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
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
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
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
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public void clear() {
		id=0;
		createdAt=null;
		dealValue=0;
		discountPercentage=0;
		originalValue=0;
		postDate=null;
		title=null;
		updatedAt=null;
		expiryDate=null;
		url=null;
		validTo=null;
		subscription_id=0;
		userInfoid=0;
		dealEmailId=0;
		freeShipping=false;
		locationId=0;
		tags=null;
		
	}

}
