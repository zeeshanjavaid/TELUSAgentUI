package com.fico.ps.exception;

public class FileHandlerException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String exceptionMessage;
	private Throwable exceptionCause;
	
	//getters and setters
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public Throwable getExceptionCause() {
		return exceptionCause;
	}

	public void setExceptionCause(Throwable exceptionCause) {
		this.exceptionCause = exceptionCause;
	}

	//constructors
	public FileHandlerException(String message) {
		super(message);
		this.exceptionMessage = message;
	}
	
	public FileHandlerException(String message, Throwable cause) {
		super(message, cause);
		this.exceptionMessage = message;
		this.exceptionCause = cause;
	}
}
