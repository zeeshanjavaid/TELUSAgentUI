package com.fico.ps.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ActivityVO {

	private Integer id;
	private Integer sourceId;
	private String sourceCode;
	private String sourceDescription;
	private Integer applicationId;
	private Integer applicationStatusId;
	private String applicationStatusCode;
	private String applicationStatusDescription;
	private String description;
	private String name;
	private Integer typeId;
	private String typeCode;
	private String typeDescription;
	private Timestamp startTime;
	private Timestamp endTime;
	private String status;
	private String username;
	private Integer duration;
	private Boolean isError;
	private ActivityPayloadVO activityPayloadVO;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getSourceDescription() {
		return sourceDescription;
	}
	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getApplicationStatusId() {
		return applicationStatusId;
	}
	public void setApplicationStatusId(Integer applicationStatusId) {
		this.applicationStatusId = applicationStatusId;
	}
	public String getApplicationStatusCode() {
		return applicationStatusCode;
	}
	public void setApplicationStatusCode(String applicationStatusCode) {
		this.applicationStatusCode = applicationStatusCode;
	}
	public String getApplicationStatusDescription() {
		return applicationStatusDescription;
	}
	public void setApplicationStatusDescription(String applicationStatusDescription) {
		this.applicationStatusDescription = applicationStatusDescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Boolean getIsError() {
		return isError;
	}
	public void setIsError(Boolean isError) {
		this.isError = isError;
	}
	public ActivityPayloadVO getActivityPayloadVO() {
		return activityPayloadVO;
	}
	public void setActivityPayloadVO(ActivityPayloadVO activityPayloadVO) {
		this.activityPayloadVO = activityPayloadVO;
	}
	
}