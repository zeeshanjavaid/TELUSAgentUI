package com.fico.pscomponent.model;

import javax.validation.constraints.NotNull;

public class UserDTO {

	@NotNull(message = "First name is missing")
	private String firstName;
	@NotNull(message = "Last name is missing")
	private String lastName;
	//@NotNull(message = "UserId is missing")
	private String userId;
	@NotNull(message = "Email is missing")
	private String email;
	@NotNull(message = "Status is missing")
	private String status;
	@NotNull(message = "Role is missing")
	private String role;
	private boolean isActive;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	

}
