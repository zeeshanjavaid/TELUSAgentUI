package com.fico.ps.model;

import java.util.List;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

// @JsonInclude(JsonInclude.Include.NON_NULL)
public class Error implements Serializable {
	
	private  final String code; 
	private  final String desc;
// 	private  ZonedDateTime timestamp = ZonedDateTime.now();
	private  LocalDateTime timestamp = LocalDateTime.now();
	
	private  final List<Error> details;
	private  final InnerError innerError;

    	
		
	
    public Error() {
    	this.code=null;
		this.desc=null;
		this.details=null;
		this.innerError=null;
	}
	
    public Error(String code,String desc) {
		this.code=code;
		this.desc=desc;
		this.details=null;
		this.innerError=null;
	}
	
	public Error(String code,String desc,List<Error> details, InnerError innerError) {
		this.innerError=innerError;
		this.details=details;
		this.code=code;
		this.desc=desc;
	}


	public String getCode() {
		return code;
	}
	
	public String getDesc() {
		return desc;
	}



	public List<Error> getDetails() {
		return details;
	}


	public InnerError getInnerError() {
		return innerError;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}
