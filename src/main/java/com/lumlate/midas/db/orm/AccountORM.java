package com.lumlate.midas.db.orm;

import java.sql.Date;

import com.lumlate.midas.email.Email;

public class AccountORM {
	private long id;
	private boolean active;
	private Date createdAt;
	private String dllrAccessToken;
	private String dllrTokenSecret;
	private String email;
	private Date lastConfirmedAt;
	private String lastError;
	private Date lastErrorAt;
	private Date updatedAt;
	private long userid;
	private long provider_id;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getDllrAccessToken() {
		return dllrAccessToken;
	}
	public void setDllrAccessToken(String dllrAccessToken) {
		this.dllrAccessToken = dllrAccessToken;
	}
	public String getDllrTokenSecret() {
		return dllrTokenSecret;
	}
	public void setDllrTokenSecret(String dllrTokenSecret) {
		this.dllrTokenSecret = dllrTokenSecret;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getLastConfirmedAt() {
		return lastConfirmedAt;
	}
	public void setLastConfirmedAt(Date lastConfirmedAt) {
		this.lastConfirmedAt = lastConfirmedAt;
	}
	public String getLastError() {
		return lastError;
	}
	public void setLastError(String lastError) {
		this.lastError = lastError;
	}
	public Date getLastErrorAt() {
		return lastErrorAt;
	}
	public void setLastErrorAt(Date lastErrorAt) {
		this.lastErrorAt = lastErrorAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getProvider_id() {
		return provider_id;
	}
	public void setProvider_id(long provider_id) {
		this.provider_id = provider_id;
	}
	public long getId(String email) {
		return id;
		// TODO Auto-generated method stub
		
	}
	
	
}
