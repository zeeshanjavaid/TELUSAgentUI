package com.fico.pscomponent.quartz.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobDTO {

	@NotNull(message = "Job Type should not be empty")
	private String jobType;
	@NotNull(message = "Job Name should not be empty")
	private String jobName;
	private String cronExpression;
	@JsonProperty("isCron")
	private Boolean isCron;
	@JsonProperty("isFrequency")
	private Boolean isFrequency;
	private int frequency;
	private String description;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Boolean getIsCron() {
		return isCron;
	}

	public void setIsCron(Boolean isCron) {
		this.isCron = isCron;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsFrequency() {
		return isFrequency;
	}

	public void setIsFrequency(Boolean isFrequency) {
		this.isFrequency = isFrequency;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
