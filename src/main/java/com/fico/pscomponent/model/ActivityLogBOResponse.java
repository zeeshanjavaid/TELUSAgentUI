package com.fico.pscomponent.model;

public class ActivityLogBOResponse {
	private Integer logId;
	private Boolean success;
	private String result;
	private String errorMessage;
	private Boolean clientError;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getClientError() {
		return clientError;
	}

	public void setClientError(Boolean clientError) {
		this.clientError = clientError;
	}

}
