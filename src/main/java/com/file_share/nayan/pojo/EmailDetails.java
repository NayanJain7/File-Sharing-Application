package com.file_share.nayan.pojo;


public class EmailDetails {
	
	private String uuid;
	private String emailTo;
	private String emailFrom;
	public EmailDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EmailDetails(String uuid,String emailTo, String emailFrom) {
		this.uuid= uuid;
		this.emailTo = emailTo;
		this.emailFrom = emailFrom;
	}
	
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
}
