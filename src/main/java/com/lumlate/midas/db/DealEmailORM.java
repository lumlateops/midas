package com.lumlate.midas.db;

import java.sql.Date;

public class DealEmailORM {

	private long id;
	private String category;
	private String content;
	private Date dateReceived;
	private String domainKey;
	private String fromEmail;
	private String fromName;
	private String parsedContent;
	private String senderIP;
	private Date sentDate;
	private String spfResult;
	private String subject;
	private String toName;

	public DealEmailORM(long id, String category, String content,
			Date dateReceived, String domainKey, String fromEmail,
			String fromName, String parsedContent, String senderIP,
			Date sentDate, String spfResult, String subject, String toName) {
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
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

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
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

}
