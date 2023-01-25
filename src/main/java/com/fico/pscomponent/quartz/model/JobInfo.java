package com.fico.pscomponent.quartz.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobInfo {
	private String jobType;
	private String jobName;
	private String description;
	private String cronExpression;
	private Integer frequency;
	@JsonProperty("isCron")
	private Boolean isCron;
	@JsonProperty("isFrequency")
	private Boolean isFrequency;
	@JsonProperty("isPaused")
	private Boolean isPaused;

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Boolean getIsCron() {
		return isCron;
	}

	public void setIsCron(Boolean isCron) {
		this.isCron = isCron;
	}

	public Boolean getIsFrequency() {
		return isFrequency;
	}

	public void setIsFrequency(Boolean isFrequency) {
		this.isFrequency = isFrequency;
	}

	public Boolean getIsPaused() {
		return isPaused;
	}

	public void setIsPaused(Boolean isPaused) {
		this.isPaused = isPaused;
	}

}
