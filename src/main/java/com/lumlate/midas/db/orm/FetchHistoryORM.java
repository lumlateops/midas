package com.lumlate.midas.db.orm;

public class FetchHistoryORM {
	
	private long id;
	private String fetchEndTime;
	private String fetchStartTime;
	private String fetchErrorMessage;
	private String fetchStatus;
	private String sessionid;
	private long userid;
	
	public FetchHistoryORM(long id, String fetchEndTime, String fetchStartTime,
			String fetchErrorMessage, String fetchStatus, String sessionid,
			long userid) {
		super();
		this.id = id;
		this.fetchEndTime = fetchEndTime;
		this.fetchStartTime = fetchStartTime;
		this.fetchErrorMessage = fetchErrorMessage;
		this.fetchStatus = fetchStatus;
		this.sessionid = sessionid;
		this.userid = userid;
	}
	
	public FetchHistoryORM(String fetchEndTime, String fetchStartTime,
			String fetchErrorMessage, String fetchStatus, String sessionid,
			long userid) {
		super();
		this.fetchEndTime = fetchEndTime;
		this.fetchStartTime = fetchStartTime;
		this.fetchErrorMessage = fetchErrorMessage;
		this.fetchStatus = fetchStatus;
		this.sessionid = sessionid;
		this.userid = userid;
	}
	
	public FetchHistoryORM() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFetchEndTime() {
		return fetchEndTime;
	}
	public void setFetchEndTime(String fetchEndTime) {
		this.fetchEndTime = fetchEndTime;
	}
	public String getFetchStartTime() {
		return fetchStartTime;
	}
	public void setFetchStartTime(String fetchStartTime) {
		this.fetchStartTime = fetchStartTime;
	}
	public String getFetchErrorMessage() {
		return fetchErrorMessage;
	}
	public void setFetchErrorMessage(String fetchErrorMessage) {
		this.fetchErrorMessage = fetchErrorMessage;
	}
	public String getFetchStatus() {
		return fetchStatus;
	}
	public void setFetchStatus(String fetchStatus) {
		this.fetchStatus = fetchStatus;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	
	
	
}
