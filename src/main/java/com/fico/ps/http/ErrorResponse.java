package com.fico.ps.http;

public class ErrorResponse {
	  private final Error error;
	  
	  public ErrorResponse() {
		this.error = null;}
	  
	  public ErrorResponse(Error error) {
	    this.error = error;
	  }
	  
	  public Error getError() {
	    return this.error;
	  }
	  
	
	
}
