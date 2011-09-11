package com.lumlate.midas.email;


import java.sql.Date;

import com.lumlate.midas.utils.HtmlParser;

public class Email {
	private String fromname;
	private String fromemail;
	private String to;
	private Date recieveddate;
	private Date sentdate;
	private String subject;
	private String senderip;
	private String spf_result;
	private String domainkey_status;
	private String content;
	private boolean is_plaintext;
	private boolean is_html;
	private boolean is_inline;
	private boolean is_attachment;
	private String category; 
	private HtmlParser html;
	
	public String getFromname() {
		return fromname;
	}
	public void setFromname(String fromname) {
		this.fromname = fromname.toLowerCase();
	}
	public String getFromemail() {
		return fromemail;
	}
	public void setFromemail(String fromemail) {
		this.fromemail = fromemail.toLowerCase();
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to.toLowerCase();
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject.toLowerCase();
	}
	public String getSenderip() {
		return senderip;
	}
	public void setSenderip(String senderip) {
		this.senderip = senderip;
	}
	public String getSpf_result() {
		return spf_result;
	}
	public void setSpf_result(String spf_result) {
		this.spf_result = spf_result;
	}
	public String getDomainkey_status() {
		return domainkey_status;
	}
	public void setDomainkey_status(String domainkey_status) {
		this.domainkey_status = domainkey_status;
	}
	public Date getRecieveddate() {
		return recieveddate;
	}
	public void setRecieveddate(Date recieveddate) {
		this.recieveddate = recieveddate;
	}
	public Date getSentdate() {
		return sentdate;
	}
	public void setSentdate(Date sentdate) {
		this.sentdate = sentdate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content.toLowerCase();
	}
	public boolean isIs_plaintext() {
		return is_plaintext;
	}
	public void setIs_plaintext(boolean is_plaintext) {
		this.is_plaintext = is_plaintext;
	}
	public boolean isIs_html() {
		return is_html;
	}
	public void setIs_html(boolean is_html) {
		this.is_html = is_html;
	}
	public boolean isIs_inline() {
		return is_inline;
	}
	public void setIs_inline(boolean is_inline) {
		this.is_inline = is_inline;
	}
	public boolean isIs_attachment() {
		return is_attachment;
	}
	public void setIs_attachment(boolean is_attachment) {
		this.is_attachment = is_attachment;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public HtmlParser getHtml() {
		return html;
	}
	public void setHtml(HtmlParser html) {
		this.html = html;
	}
}
