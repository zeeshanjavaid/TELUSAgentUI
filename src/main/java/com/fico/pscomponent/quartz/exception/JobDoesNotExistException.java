package com.fico.pscomponent.quartz.exception;

public class JobDoesNotExistException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public JobDoesNotExistException(String message) {
		super(message);
	}
}
