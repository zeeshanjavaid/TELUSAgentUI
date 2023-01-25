package com.fico.pscomponent.service.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.StringUtils;

import com.fico.pscomponent.exception.ActivityLogBOValidationException;
import com.fico.pscomponent.model.ActivityLogBO;

public class ActivityLogBOValidationService {

	public static void validate(ActivityLogBO activityLogBO) throws ActivityLogBOValidationException {
		 List<String> validationErrorMessages = new ArrayList<String>();
		
 		if (StringUtils.isEmpty(activityLogBO.getLogType()) && Objects.isNull(activityLogBO.getLogId())) {
 			validationErrorMessages.add("Missing Log Type (logType) in request");
 		}

// 		if (StringUtils.isEmpty(activityLogBO.getRequestPayload()) && 
// 		        StringUtils.isEmpty(activityLogBO.getResponsePayload())) {
// 			validationErrorMessages.add("Missing Request or Response Payload (requestPayload, responsePayload) in request");
// 		}

// 		if (StringUtils.isEmpty(activityLogBO.getRequestTs()) && StringUtils.isEmpty(activityLogBO.getResponseTs())) {
// 			validationErrorMessages.add("Missing Request or Response Timestamp (requestTs, responseTs) in request");
// 		}

		if (validationErrorMessages.size() > 0) {
			StringBuilder errors = new StringBuilder();
			validationErrorMessages.forEach(message -> errors.append(" \n").append(message));
			throw new ActivityLogBOValidationException("Validation Failed - Details: " + errors.toString(), new Exception("Validation Failed"));
		}
	}
}
