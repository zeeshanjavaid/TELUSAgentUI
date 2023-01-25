package com.fico.pscomponent.quartz.model;

public class JobResponse {

	private Boolean success;
	private String message;
	private Boolean clientError;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getClientError() {
		return clientError;
	}

	public void setClientError(Boolean clientError) {
		this.clientError = clientError;
	}

}
