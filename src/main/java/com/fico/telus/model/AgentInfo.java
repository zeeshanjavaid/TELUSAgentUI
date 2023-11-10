package com.fico.telus.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentInfo {
	
	private String empId;
	private String role;
	private String teamId;
	private List<String> workCategory;

	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
	public List<String> getWorkCategory() {
		return workCategory;
	}
	public void setWorkCategory(List<String> workCategory) {
		this.workCategory = workCategory;
	}

}
