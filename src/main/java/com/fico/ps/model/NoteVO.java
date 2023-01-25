package com.fico.ps.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wavemaker.runtime.security.xss.XssDisable;

@XssDisable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteVO {
	private Integer id;
	private String content;
	private Integer noteCategoryId; 
	private String noteCategoryCode;
	private LocalDateTime timestamp;
	private Integer createdBy;
	private Integer updatedBy;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getNoteCategoryId() {
		return noteCategoryId;
	}
	public void setNoteCategoryId(Integer noteCategoryId) {
		this.noteCategoryId = noteCategoryId;
	}
	public String getNoteCategoryCode() {
		return noteCategoryCode;
	}
	public void setNoteCategoryCode(String noteCategoryCode) {
		this.noteCategoryCode = noteCategoryCode;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
}
