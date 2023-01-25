package com.fico.ps.exception;

public class ApplicationValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String exceptionMessage;
	private Throwable throwable;
	
	//constructors
	public ApplicationValidationException(String message) {
		super(message);
		this.exceptionMessage = message;
	}
	
	public ApplicationValidationException(String message, Throwable throwable) {
		super(message, throwable);
		this.exceptionMessage = message;
		this.throwable = throwable;
	}

	//getters and setters
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public Throwable getThrowable() {
		return throwable;
	}
	
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
}
