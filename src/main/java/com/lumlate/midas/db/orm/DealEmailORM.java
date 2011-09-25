package com.lumlate.midas.db.orm;

import java.sql.Date;

public class DealEmailORM {

	private long id;
	private long category;
	private String content;
	private String dateReceived;
	private String domainKey;
	private String fromEmail;
	private String fromName;
	private String parsedContent;
	private String senderIP;
	private String sentDate;
	private String spfResult;
	private String subject;
	private String toName;
	
	public DealEmailORM() {
		super();
		this.id = id;
		this.category = category;
		this.content = content;
		this.dateReceived = dateReceived;
		this.domainKey = domainKey;
		this.fromEmail = fromEmail;
		this.fromName = fromName;
		this.parsedContent = parsedContent;
		this.senderIP = senderIP;
		this.sentDate = sentDate;
		this.spfResult = spfResult;
		this.subject = subject;
		this.toName = toName;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCategory() {
		return category;
	}
	public void setCategory(long category) {
		this.category = category;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getDomainKey() {
		return domainKey;
	}
	public void setDomainKey(String domainKey) {
		this.domainKey = domainKey;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getParsedContent() {
		return parsedContent;
	}
	public void setParsedContent(String parsedContent) {
		this.parsedContent = parsedContent;
	}
	public String getSenderIP() {
		return senderIP;
	}
	public void setSenderIP(String senderIP) {
		this.senderIP = senderIP;
	}
	public String getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public String getSpfResult() {
		return spfResult;
	}
	public void setSpfResult(String spfResult) {
		this.spfResult = spfResult;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}

	public void clear() {
		id=0;
		category=0;
		content=null;
		dateReceived=null;
		domainKey=null;
		fromEmail=null;
		fromName=null;
		parsedContent=null;
		senderIP=null;
		sentDate=null;
		spfResult=null;
		subject=null;
		toName=null;	
	}

}
