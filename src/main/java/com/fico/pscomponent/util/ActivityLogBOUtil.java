package com.fico.pscomponent.util;

import java.sql.Timestamp;
import java.util.Objects;

import com.fico.pscomponent.model.ActivityLogBO;

public class ActivityLogBOUtil {

	public static ActivityLogBO createActivityLogBO(String requestPayload, String logType, 
			Integer applicationId, String name) {
		ActivityLogBO activityLogBO = new ActivityLogBO();
		activityLogBO.setFicoId(applicationId);
		activityLogBO.setDoNotDeleteFlag(false);
		activityLogBO.setLogType(logType);
		activityLogBO.setName(name);
		activityLogBO.setRequestTS(new Timestamp(System.currentTimeMillis()));
		activityLogBO.setRequestPayload(requestPayload);
		return activityLogBO;
	}

	public static ActivityLogBO createActivityLogBO(String requestPayload, String logType, 
			Integer applicationId, String name, Timestamp requestTS) {
		ActivityLogBO activityLogBO = createActivityLogBO(requestPayload, logType, applicationId, name);
		if (!Objects.isNull(requestTS)) {
			activityLogBO.setRequestTS(requestTS);
		}
		return activityLogBO;
	}

	public static ActivityLogBO updateActivityLogBO(String responsePayload, String logType,
	        Integer logId, Boolean isSuccess, Boolean isTimeout, Boolean isNoHit, Boolean isError, String resultMessage) {
		ActivityLogBO activityLogBO = new ActivityLogBO();
		activityLogBO.setLogId(logId);
		activityLogBO.setLogType(logType);
		activityLogBO.setResponseTS(new Timestamp(System.currentTimeMillis()));
		activityLogBO.setIsTimeout(isTimeout);
		activityLogBO.setIsSuccess(isSuccess);
		activityLogBO.setIsError(isError);
		activityLogBO.setIsNoHit(isNoHit);
		activityLogBO.setResultMessage(resultMessage);
		activityLogBO.setResponsePayload(responsePayload);
		return activityLogBO;
	}

	public static ActivityLogBO updateActivityLogBO(String responsePayload, String logType,
	        Integer logId, Boolean isSuccess, Boolean isTimeout, Boolean isNoHit, Boolean isError, String resultMessage, Timestamp responseTS) {
		ActivityLogBO updateActivityLogBO = updateActivityLogBO(responsePayload, logType, logId, isSuccess, isTimeout, isNoHit, isError, resultMessage);
		if (!Objects.isNull(responseTS)) {
			updateActivityLogBO.setResponseTS(responseTS);
		}
		return updateActivityLogBO;
	}
}
