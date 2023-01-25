package com.fico.ps.http;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public class Error implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  final String code; 
	private  final String desc;
	private  final ZonedDateTime timestamp = ZonedDateTime.now();
	private  final List<Error> details;
	private  final InnerError innerError;
	private  final List<ValidationMessage> validationMessages;

    public Error(String code,String desc) {
		this.code=code;
		this.desc=desc;
		this.details=null;
		this.innerError=null;
		this.validationMessages = null;
	}
    
    
	
	public Error(String code,String desc,Exception ex) {
		this.innerError=null;
		this.details=null;
		this.code=code;
		this.desc=desc;
		this.validationMessages = null;
	}
	
	public Error(String code,String desc, List<ValidationMessage> validationMessages) {
		this.code=code;
		this.desc=desc;
		this.details=null;
		this.innerError=null;
		this.validationMessages = validationMessages;
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
	
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public List<ValidationMessage> getValidationMessages() {
		return validationMessages;
	}
	
}
