package com.lumlate.midas.db.orm;

import java.sql.Date;

public class UserInfoORM {

	private long id;
	private Date createdAt;
	private String emailAddress;
	private String fbEmailAddress;
	private String fbFullName;
	private long fbLocationId;
	private long fbLocationName;
	private long fbUserId;
	private String gender;
	private boolean isActive;
	private boolean isAdmin;
	private String password;
	private Date updatedAt;
	private String username;

	public UserInfoORM(long id, Date createdAt, String emailAddress,
			String fbEmailAddress, String fbFullName, long fbLocationId,
			long fbLocationName, long fbUserId, String gender,
			boolean isActive, boolean isAdmin, String password, Date updatedAt,
			String username) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.emailAddress = emailAddress;
		this.fbEmailAddress = fbEmailAddress;
		this.fbFullName = fbFullName;
		this.fbLocationId = fbLocationId;
		this.fbLocationName = fbLocationName;
		this.fbUserId = fbUserId;
		this.gender = gender;
		this.isActive = isActive;
		this.isAdmin = isAdmin;
		this.password = password;
		this.updatedAt = updatedAt;
		this.username = username;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFbEmailAddress() {
		return fbEmailAddress;
	}

	public void setFbEmailAddress(String fbEmailAddress) {
		this.fbEmailAddress = fbEmailAddress;
	}

	public String getFbFullName() {
		return fbFullName;
	}

	public void setFbFullName(String fbFullName) {
		this.fbFullName = fbFullName;
	}

	public long getFbLocationId() {
		return fbLocationId;
	}

	public void setFbLocationId(long fbLocationId) {
		this.fbLocationId = fbLocationId;
	}

	public long getFbLocationName() {
		return fbLocationName;
	}

	public void setFbLocationName(long fbLocationName) {
		this.fbLocationName = fbLocationName;
	}

	public long getFbUserId() {
		return fbUserId;
	}

	public void setFbUserId(long fbUserId) {
		this.fbUserId = fbUserId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
