package com.lumlate.midas.db.orm;

import java.sql.Date;

public class SubscriptionORM {

	private long id;
	private Boolean active;
	private String createdAt;
	private String updatedAt;
	private long department_id;
	private long AccountId;

	public SubscriptionORM(long id, Boolean active, String createdAt,
			String updatedAt, long department_id, long accountId) {
		super();
		this.id = id;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.department_id = department_id;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String string) {
		this.createdAt = string;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(long department_id) {
		this.department_id = department_id;
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

	public void clear() {
		id = 0;
		active = false;
		createdAt = null;
		updatedAt = null;
		department_id = 0;
		AccountId = 0;

	}
}
