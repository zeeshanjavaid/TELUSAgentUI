package com.fico.ps.http;

public class ValidationMessage {

	private String code;
	private String message;
	private String path;
	
		
	public ValidationMessage(String code, String message, String path) {
		super();
		this.code=code;
		this.message = message;
		this.setPath(path);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
