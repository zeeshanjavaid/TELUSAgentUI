package com.fico.pscomponent.exception;

public class ActivityLogBOValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ActivityLogBOValidationException(String message, Throwable ex) {
		super(message, ex);
	}
}
