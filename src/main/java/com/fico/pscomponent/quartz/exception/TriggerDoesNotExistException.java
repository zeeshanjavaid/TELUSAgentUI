package com.fico.pscomponent.quartz.exception;

public class TriggerDoesNotExistException extends Exception {

	private static final long serialVersionUID = 1L;

	public TriggerDoesNotExistException(String message) {
		super(message);
	}
}
