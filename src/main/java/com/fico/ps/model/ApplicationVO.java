package com.fico.ps.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.util.Date;
import java.time.LocalDateTime;
import com.wavemaker.runtime.security.xss.XssDisable;
import com.fasterxml.jackson.annotation.JsonProperty;

@XssDisable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationVO {
    	private Integer id;
	private String applicationNumber;
	//private List<ApplicantVO> applicants;
	//private List<ApplicationProductVO> applicationProduct;
	private List<NoteVO> notes;
	//private ApplicationFinancial applicationFinancial;
	//private List<DecisionResponseVO> decisionResponse;
	//private List<OfferVO> offers;
	//private List<CreditApplicationRequestVO> creditApplicationRequests;
	//private ApplicationDetails applicationDetails;
	//private List<CaseReviewVO> caseReviews;
	//private Eligibility eligibility;
	//private List<DecisionAttributeVO> decisionAttribute;
   // private String bureauResponse;
   // private String turnOffDupCheck;
   // private String turnOffMultiApp;
  //  private String fDOOffline;
  //  private String bureauType;
   // private String alertTypeCd;
	//private Error error;
	
	// ****** TD Specific *****
	//private GenericAdditionalCustomerInformationVO genericAdditionalCustomerInformation;
	private String submitMode;
	//private List<AuthorizedUserVO> authorizedUser;
	//private List<ApplicationStatusHistoryVO> applicationStatusHistory;
// 	@JsonProperty("tDAppProduct")
	//private TDAppProductVO tdAppProduct;
	//private DPOIResponseVO dpoiResponse;
// 	private FulfillmentVO fulfillment;
	//private List<CommunicationEventVO> communicationEvent;
	//private List<BalanceTransferVO> balanceTransfer;
	//private List<TransferToCheckingVO> transferToChecking;
	//private List<OnlineDisclosureVO> onlineDisclosure;
	//private List<DisclosureVO> disclosures;
//	private MultiAppsVO multiApps;
// ****** TD Specific *****
	
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	
	public List<NoteVO> getNotes() {
		return notes;
	}
	public void setNotes(List<NoteVO> notes) {
		this.notes = notes;
	}
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	

	
	
	
	public String getSubmitMode() {
		return submitMode;
	}
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}
	
	
	
  
    // public FulfillmentVO getFulfillment() {
    //     return this.fulfillment;
    // }

    // public void setFulfillment(FulfillmentVO fulfillment) {
    //     this.fulfillment = fulfillment;
    // }
    
    
  
// 	 public MultiAppsVO getMultiApps() {
//         return this.multiApps;
//     }

//     public void setMultiApps(MultiAppsVO multiApps) {
//         this.multiApps = multiApps;
//     }
	
		// ****** TD Specific *****

}