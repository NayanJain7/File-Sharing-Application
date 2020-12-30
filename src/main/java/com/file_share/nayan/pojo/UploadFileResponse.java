package com.file_share.nayan.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;


@Entity
@Table(name = "FILE_DETAILS")
public class UploadFileResponse {
	@Id
	private String uuid;
	private String fileName;
	private String fileType;
	private long size;
	private String filePath;
	private String shareLink;

	private long created;
	
	@PrePersist
	protected void onCreate() {
		created = new Date().getTime();
	}

	public UploadFileResponse() {
		super();

	}

	public UploadFileResponse(String uuid, String fileName, String fileType, long size, String filePath,
			String shareLink) {
		super();
		this.uuid = uuid;
		this.fileName = fileName;
		this.fileType = fileType;
		this.size = size;
		this.filePath = filePath;
		this.shareLink = shareLink;
	}

	public String getUuid() {
		return uuid;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public long getSize() {
		return size;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	
	
	public long getCreateDateTime() {
		return created;
	}

	
	@Override
	public String toString() {
		return "UploadFileResponse [uuid=" + uuid + ", fileName=" + fileName + ", fileType=" + fileType + ", size="
				+ size + ", filePath=" + filePath + ", shareLink=" + shareLink + ", createDateTime=" + created
				+ "]";
	}

}
