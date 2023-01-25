package com.fico.ps.model.queue;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(content = Include.NON_NULL)
public class QueueResponse {

	private boolean isToDisplay;
	private Integer applicationId;
	private String applicationNumber;
	private String applicationStatus;
	private String applicationChannel;
	private String caseReviewType;
	private Integer caseReviewLevel;
	private String customerName;
	private Timestamp dateOfBirth;	//TD Spec
	private String postalCode;	//TD Spec
	private String province;	//TD Spec
	private String phoneNumber;
	private String mothersMaidenName;	//TD Spec
	private String employerName;	//TD Spec
	private String employmentJobcode;	//TD Spec
	private String emailId;
	private String ipAddress;
	private Timestamp applicationCreatedOn;
	private Timestamp applicationUpdatedOn;		//TD Spec
	private String applicationCreatedBy;	//TD Spec
	private String applicationUpdatedBy;
	private Integer appCreatedSinceDays = -1;	//TD Spec
	private Integer appOwnedSinceDays = -1;	//TD Spec
	private Timestamp appHoldUntil;	//TD Spec
	private String appHoldReason;	//TD Spec
	private String applicationLockedBy;
	
	public boolean isToDisplay() {
		return isToDisplay;
	}
	public void setToDisplay(boolean isToDisplay) {
		this.isToDisplay = isToDisplay;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public String getApplicationChannel() {
		return applicationChannel;
	}
	public void setApplicationChannel(String applicationChannel) {
		this.applicationChannel = applicationChannel;
	}
	public String getCaseReviewType() {
		return caseReviewType;
	}
	public void setCaseReviewType(String caseReviewType) {
		this.caseReviewType = caseReviewType;
	}
	public Integer getCaseReviewLevel() {
		return caseReviewLevel;
	}
	public void setCaseReviewLevel(Integer caseReviewLevel) {
		this.caseReviewLevel = caseReviewLevel;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Timestamp getApplicationCreatedOn() {
		return applicationCreatedOn;
	}
	public void setApplicationCreatedOn(Timestamp applicationCreatedOn) {
		this.applicationCreatedOn = applicationCreatedOn;
	}
	public String getApplicationUpdatedBy() {
		return applicationUpdatedBy;
	}
	public void setApplicationUpdatedBy(String applicationUpdatedBy) {
		this.applicationUpdatedBy = applicationUpdatedBy;
	}
	public String getApplicationLockedBy() {
		return applicationLockedBy;
	}
	public void setApplicationLockedBy(String applicationLockedBy) {
		this.applicationLockedBy = applicationLockedBy;
	}
	public String getMothersMaidenName() {
		return mothersMaidenName;
	}
	public void setMothersMaidenName(String mothersMaidenName) {
		this.mothersMaidenName = mothersMaidenName;
	}
	public String getEmployerName() {
		return employerName;
	}
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}
	public String getEmploymentJobcode() {
		return employmentJobcode;
	}
	public void setEmploymentJobcode(String employmentJobcode) {
		this.employmentJobcode = employmentJobcode;
	}
	public Timestamp getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Timestamp dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getApplicationCreatedBy() {
		return applicationCreatedBy;
	}
	public void setApplicationCreatedBy(String applicationCreatedBy) {
		this.applicationCreatedBy = applicationCreatedBy;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Integer getAppCreatedSinceDays() {
		return appCreatedSinceDays;
	}
	public void setAppCreatedSinceDays(Integer appCreatedSinceDays) {
		this.appCreatedSinceDays = appCreatedSinceDays;
	}
	public Integer getAppOwnedSinceDays() {
		return appOwnedSinceDays;
	}
	public void setAppOwnedSinceDays(Integer appOwnedSinceDays) {
		this.appOwnedSinceDays = appOwnedSinceDays;
	}
	public Timestamp getAppHoldUntil() {
		return appHoldUntil;
	}
	public void setAppHoldUntil(Timestamp appHoldUntil) {
		this.appHoldUntil = appHoldUntil;
	}
	public String getAppHoldReason() {
		return appHoldReason;
	}
	public void setAppHoldReason(String appHoldReason) {
		this.appHoldReason = appHoldReason;
	}
	public Timestamp getApplicationUpdatedOn() {
		return applicationUpdatedOn;
	}
	public void setApplicationUpdatedOn(Timestamp applicationUpdatedOn) {
		this.applicationUpdatedOn = applicationUpdatedOn;
	}
	
}
