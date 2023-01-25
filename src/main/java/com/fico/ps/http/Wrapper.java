package com.fico.ps.http;

public class Wrapper<T> {

	private final T payload;
	private final Error error;
	
	public Wrapper(T payload, Error error) {
		
		this.payload=payload;
		this.error=error;
		
	}
	
	public Wrapper(T payload) {
		
		this.payload=payload;
		this.error=null;
		
	}

	public T getPayload() {
		return payload;
	}

	public Error getError() {
		return error;
	}
}
