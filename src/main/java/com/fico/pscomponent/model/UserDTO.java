package com.fico.pscomponent.model;

import javax.validation.constraints.NotNull;
import java.util.List;


public class UserDTO {

	@NotNull(message = "First name is missing")
	private String firstName;
	@NotNull(message = "Last name is missing")
	private String lastName;
	@NotNull(message = "UserId is missing")
	private String userId;
	@NotNull(message = "Email is missing")
	private String email;
	//@NotNull(message = "Status is missing")
	private String status;
	@NotNull(message = "Role is missing")
	private String role;
	private boolean isActive;
	
//	@NotNull(message = "Work Category is missing")
	private List<String> workCategory;
	
	@NotNull(message = "EmplID is missing")
	private String emplId;
	
	@NotNull(message = "TeamId is missing")
	private String teamId;

//@NotNull(message = "TeamManager is missing")
	private Integer teamManagerId;
	
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
	
	public List<String> getWorkCategory() {
		return workCategory;
	}

	public void setWorkCategory(List<String> workCategory) {
		this.workCategory = workCategory;
	}
	
	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Integer getTeamManagerId() {
		return teamManagerId;
	}

	public void setTeamManagerId(Integer teamManagerId) {
		this.teamManagerId = teamManagerId;
	}
	

}
