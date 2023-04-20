package com.fico.telus.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentInfo {
	Integer empId;
	List<String> workCategory;
    
    @JsonProperty("empId")
	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

    
    @JsonProperty("workCategory")
	public List<String> getWorkCategory() {
		return workCategory;
	}

	public void setWorkCategory(List<String> workCategory) {
		this.workCategory = workCategory;
	}
      
}
