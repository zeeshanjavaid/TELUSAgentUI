package com.fico.pscomponent.model;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class ActivityLogBO implements Serializable {

	private Integer logId;

	private Integer ficoId;

	private String logType;

	private String description;

	private Boolean doNotDeleteFlag;

	private Timestamp requestTS;

	private Timestamp responseTS;

	private String requestPayload;

	private String responsePayload;

	private String resultMessage;

	private Boolean isSuccess;

	private Boolean isTimeout;

	private Boolean isError;

	private Boolean isNoHit;

	private String name;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getFicoId() {
		return ficoId;
	}

	public void setFicoId(Integer ficoId) {
		this.ficoId = ficoId;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDoNotDeleteFlag() {
		return doNotDeleteFlag;
	}

	public void setDoNotDeleteFlag(Boolean doNotDeleteFlag) {
		this.doNotDeleteFlag = doNotDeleteFlag;
	}

	public Timestamp getRequestTS() {
		return requestTS;
	}

	public void setRequestTS(Timestamp requestTS) {
		this.requestTS = requestTS;
	}

	public Timestamp getResponseTS() {
		return responseTS;
	}

	public void setResponseTS(Timestamp responseTS) {
		this.responseTS = responseTS;
	}

	public String getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public String getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(String responsePayload) {
		this.responsePayload = responsePayload;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Boolean getIsTimeout() {
		return isTimeout;
	}

	public void setIsTimeout(Boolean isTimeout) {
		this.isTimeout = isTimeout;
	}

	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public Boolean getIsNoHit() {
		return isNoHit;
	}

	public void setIsNoHit(Boolean isNoHit) {
		this.isNoHit = isNoHit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
