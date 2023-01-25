package com.fico.ps.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ActivityPayloadVO {
	private Integer id;
	private Integer activityId;
	private String requestId;
	private String responseId;
	private String createdBy;
	private LocalDateTime createdTime;
	private byte[] dataPayload;
	private Boolean isRequestPayload;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getResponseId() {
		return responseId;
	}
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
	public byte[] getDataPayload() {
		return dataPayload;
	}
	public void setDataPayload(byte[] dataPayload) {
		this.dataPayload = dataPayload;
	}
	public Boolean getIsRequestPayload() {
		return isRequestPayload;
	}
	public void setIsRequestPayload(Boolean isRequestPayload) {
		this.isRequestPayload = isRequestPayload;
	}
	
}
