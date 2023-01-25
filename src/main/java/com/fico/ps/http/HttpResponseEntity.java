package com.fico.ps.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

public class HttpResponseEntity<T> extends HttpEntity<Wrapper<T>> {
	
	 private final HttpStatus statusCode; 
	 
	 /**
	  * Create a new {@code ResponseEntity} with the given body and status code, and no headers. 
	  * @param body the entity body 
	  * @param statusCode the status code 
	  */ 
	 public HttpResponseEntity(T body, HttpStatus statusCode) { 
	  
	  super(new Wrapper<T>(body)); 
	  
	  this.statusCode = statusCode;

	 } 
	 
	 public HttpResponseEntity(T body, Error error, HttpStatus statusCode) { 
	
		  super(new Wrapper<T>(body,error)); 
		  this.statusCode = statusCode; 
		 
		 } 
	 
	 /**
	  * Create a new {@code HttpEntity} with the given headers and status code, and no body. 
	  * @param headers the entity headers 
	  * @param statusCode the status code 
	  */ 
	 public HttpResponseEntity(MultiValueMap<String, String> headers, HttpStatus statusCode) { 
	   super(headers); 
	  this.statusCode = statusCode;

	 } 
	 
	 /**
	  * Create a new {@code HttpEntity} with the given body, headers, and status code. 
	  * @param body the entity body 
	  * @param headers the entity headers 
	  * @param statusCode the status code 
	  */ 
	 public HttpResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus statusCode) { 
	  super(new Wrapper<T>(body),headers); 
	  this.statusCode = statusCode;

	 } 
	 
	 /**
	  * Return the HTTP status code of the response. 
	  * @return the HTTP status as an HttpStatus enum value 
	  */ 
	 public HttpStatus getStatusCode() { 
	  return statusCode; 
	 }


}
