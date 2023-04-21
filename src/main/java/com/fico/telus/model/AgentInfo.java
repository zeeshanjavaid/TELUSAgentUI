package com.fico.telus.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentInfo {
	
	String empId;
	List<String> workCategory;
	
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public List<String> getWorkCategory() {
		return workCategory;
	}
	public void setWorkCategory(List<String> workCategory) {
		this.workCategory = workCategory;
	}
   
}
