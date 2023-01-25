package com.fico.pscomponent.handlers;

import org.springframework.stereotype.Service;

@Service
public class ActivityLogBOHandler {/*

	private static final Logger logger = LoggerFactory.getLogger(ActivityLogBOHandler.class);

	@Autowired
	private IActivityLogBOService activityLogBOService;

	
	 * @Autowired
	 * 
	 * @Qualifier("NoWrapRootMapper") private ObjectMapper objectMapper;
	 

	public Integer createActivityLog(String requestPayload, Integer applicationId, String logType, String name, Timestamp requestTS) {
		ActivityLogBO activityLogBO = ActivityLogBOUtil.createActivityLogBO(requestPayload, logType, applicationId, name, requestTS);
		ActivityLogBOResponse response = activityLogBOService.createActivityLog(activityLogBO);
		if (response.getSuccess()) {
			return response.getLogId();
		} else {
			throw new WMRuntimeException("Failed to persist activity Log with error "+ response.getErrorMessage());
		}
	}

	public Integer createActivityLogUniqueEntry(String requestPayload, Integer applicationId, String logType, String name, Timestamp requestTS) {
		ActivityLogBO activityLogBO = ActivityLogBOUtil.createActivityLogBO(requestPayload, logType, applicationId, name, requestTS);
		ActivityLogBOResponse response = activityLogBOService.createActivityLogUniqueEntry(activityLogBO);
		if (response.getSuccess()) {
			return response.getLogId();
		} else {
			throw new WMRuntimeException("Failed to persist activity Log with error "+ response.getErrorMessage());
		}
	}

	
	 * public void updateActivityLog(AssessmentResponse response, String logType,
	 * Integer logId, Timestamp responseTs, boolean error) { String responsePayload
	 * = getResponsePayload(response); String resultMessage =
	 * getResultMessage(response);
	 * 
	 * ActivityLogBOResponse logResponse = updateActivityLog(responsePayload,
	 * logType, logId, resultMessage, !error, false, error, responseTs); if
	 * (!logResponse.getSuccess()) { throw new
	 * WMRuntimeException("Failed to update activity Log with error "+
	 * logResponse.getErrorMessage()); } }
	 

	public ActivityLogBOResponse updateActivityLog(String response, String logType, Integer logId, String resultMessage, boolean success,
			boolean noHit, boolean error, Timestamp responseTs) {
		ActivityLogBO activityLogBO = ActivityLogBOUtil.updateActivityLogBO(response, logType, logId, success, false, noHit, error, resultMessage, responseTs);
		return activityLogBOService.createActivityLog(activityLogBO);
	}

	
	 * private String getResponsePayload(AssessmentResponse response) { String
	 * responsePayload = null; try { responsePayload =
	 * objectMapper.writeValueAsString(response); } catch (JsonProcessingException
	 * e) { logger.error("Error parsing response payload ", e); responsePayload =
	 * "Error parsing response payload"; } return responsePayload; }
	 * 
	 * private String getResultMessage(AssessmentResponse response) { String
	 * resultMessage = null; if (!Objects.isNull(response.getApplicantResults())) {
	 * resultMessage = "Result Message to update"; // TO-DO populate it with valid
	 * response field } return resultMessage; }
	 
*/}
