package com.fico.core.services;

import com.fico.core.util.CommonUtils;
import com.fico.dmp.telusagentuidb.Activity;
import com.fico.dmp.telusagentuidb.ActivityPayload;
import com.fico.dmp.telusagentuidb.models.query.QueryActivityLogResponse;
import com.fico.dmp.telusagentuidb.service.ActivityPayloadService;
import com.fico.dmp.telusagentuidb.service.ActivityService;
import com.fico.dmp.telusagentuidb.service.TELUSAgentUIDBQueryExecutorService;
import com.fico.ps.model.ActivityPayloadVO;
import com.fico.ps.model.ActivityVO;
import com.fico.ps.model.workflow.Content;
import com.fico.ps.model.workflow.ProcessExecutionHistoryVO;
import com.fico.ps.model.workflow.SubprocessStep;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("facade.ActivityServiceBS")
public class ActivityServiceBS {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityPayloadService activityPayloadService;

    @Autowired
    private DomainValueServiceBS domainValueServiceBS;

    @Autowired
    private TELUSAgentUIDBQueryExecutorService telusAgentUIDBQueryExecutorService;

    @Autowired
    @Qualifier("core.CommonUtils")
    private CommonUtils commonUtils;

    private static final Logger logger = LoggerFactory.getLogger(ActivityServiceBS.class);

    /**
     * Fetches an activity payload record by Id
     *
     * @param activityPayloadId
     * @return
     * @throws Exception
     */
    public ActivityPayloadVO getActivityPayloadById(Integer activityPayloadId) throws Exception {
    	if(logger.isInfoEnabled())
    		logger.info("-----Inside method 'getActivityPayloadById'");

        ActivityPayloadVO activityPayloadVO = null;
        if (activityPayloadId == null)
            return null;
        else {
            ActivityPayload activityPayloadDTO =
                    activityPayloadService.findById(activityPayloadId);
            if (activityPayloadDTO == null)
                return null;
            else {
                activityPayloadVO = new ActivityPayloadVO();
                activityPayloadVO.setId(activityPayloadDTO.getId());
                activityPayloadVO.setActivityId(activityPayloadDTO.getActivityId());
                activityPayloadVO.setCreatedBy(activityPayloadDTO.getCreatedBy());
                activityPayloadVO.setCreatedTime(activityPayloadDTO.getCreatedTime());
                activityPayloadVO.setDataPayload(activityPayloadDTO.getDataPayload());
                activityPayloadVO.setRequestId(activityPayloadDTO.getRequestId());
                activityPayloadVO.setResponseId(activityPayloadDTO.getResponseId());
                activityPayloadVO.setIsRequestPayload(activityPayloadDTO.getIsRequestPayload());

                return activityPayloadVO;
            }
        }
    }

    /**
     * Returns the activity log list as a paginated dataset
     *
     * @param userLocale
     * @param applicationNumber
     * @param activityType
     * @param activityName
     * @param createdDateStart
     * @param createdDateEnd
     * @param pageNumber
     * @param pageSize
     * @param sortProperties
     * @return
     * @throws Exception
     */
    public ActivityLogResponseWrapper getActivityEventLogs(String userLocale, String applicationNumber, Integer activityType, String activityName, Boolean isAppHistory, Timestamp createdDateStart, Timestamp createdDateEnd, Integer pageNumber,
                                                           Integer pageSize, String sortProperties) throws Exception {
    	
    	if(logger.isInfoEnabled())
    		logger.info("-----Inside method 'getActivityEventLogs'");

        Pageable pageable = null;

        if (pageNumber == null && pageSize == null && sortProperties == null)
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "activityId");
        else {
            if (pageNumber == null)
                pageNumber = 0;
            if (pageSize == null)
                pageSize = Integer.MAX_VALUE;

            List<Sort.Order> sortOrders = null;
            if (sortProperties != null && !sortProperties.trim().isEmpty()) {
                sortOrders = new ArrayList<>();
                List<String> sortProps = Arrays.asList(sortProperties.split(","));

                for (String sortProp : sortProps) {
                    final String[] sortMetadata = sortProp.split(" ");
                    final String sortKey = sortMetadata[0];
                    final String sortDirection = (sortMetadata.length > 1) ? sortMetadata[1].toUpperCase() : "ASC";

                    //add to sort order list
                    sortOrders.add(new Sort.Order(Direction.fromString(sortDirection), sortKey));
                }
            }

            //create the pageable instance
            pageable = PageRequest.of(pageNumber, pageSize, (sortOrders != null ? Sort.by(sortOrders) : Sort.unsorted()));
        }

        //call the query service to return the paginated results
        Page<QueryActivityLogResponse> activityPageData = telusAgentUIDBQueryExecutorService.executeQuery_ActivityLog(userLocale, applicationNumber,
                activityType, activityName, (isAppHistory == null ? false : isAppHistory), createdDateStart, createdDateEnd, pageable);
        List<QueryActivityLogResponse> pageAsList = activityPageData.toList();
        pageAsList = commonUtils.setNullToEmptyForStringProperties(QueryActivityLogResponse.class, pageAsList);

        //wrap the paginated dataset
        ActivityLogResponseWrapper responseWrapper = new ActivityLogResponseWrapper();
        responseWrapper.setPageNumber(activityPageData.getNumber());
        responseWrapper.setPageSize(activityPageData.getSize());
        responseWrapper.setTotalElements(activityPageData.getTotalElements());
        responseWrapper.setActivityList(pageAsList);

        return responseWrapper;
    }

    /**
     * Registers the specified FAWB event type for the given application Id
     *
     * @param applicationId
     * @param applicationStatus
     * @param activityName
     * @param activityDesc
     * @param activityStatus
     * @param activityUser
     * @param activityStartTime
     * @param activityEndTime
     * @param isActivityInError
     * @param dataPayload
     * @param isRequestPayload
     * @param activityType
     * @throws Exception
     */
    public void saveFAWBEventHistory(Integer applicationId, Integer applicationStatus, String activityName,
                                     String activityDesc, String activityStatus, String activityUser, Timestamp activityStartTime,
                                     Timestamp activityEndTime, boolean isActivityInError, Byte[] dataPayload, Boolean isRequestPayload,
                                     ActivityType activityType) throws Exception {
    	
    	if(logger.isInfoEnabled())
    		logger.info("-----Inside method 'saveFAWBEventHistory'");

        //logger.info("Activity information received for FAWB event {}", activityType.name());

        Activity activityDTO = new Activity();
        activityDTO.setApplicationId(applicationId);
        activityDTO.setApplicationStatus(applicationStatus);
        activityDTO.setSource(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(ActivitySource.FAWB.name(), "ACTIVITY_SOURCE", null).getId().intValue());
        activityDTO.setType(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(activityType.name(), "ACTIVITY_TYPE", null).getId().intValue());
        activityDTO.setDescription(activityDesc);
        activityDTO.setName(activityName);
        activityDTO.setStatus(activityStatus);
        activityDTO.setIsError(isActivityInError);
        activityDTO.setUsername(activityUser);
        activityDTO.setStartTime(activityStartTime);
        activityDTO.setEndTime(activityEndTime);
        activityDTO.setDuration((activityDTO.getEndTime() != null && activityDTO.getStartTime() != null)
                ? (int) (activityDTO.getEndTime().getTime() - activityDTO.getStartTime().getTime()) : null);

        //logger.info("Creating activity record for FAWB event of type {}", activityType.name());
        activityDTO = activityService.create(activityDTO);

        //if the event has payload to store
        if (dataPayload != null) {
            ActivityPayload activityPayloadDTO = new ActivityPayload();
            activityPayloadDTO.setActivityId(activityDTO.getId());
            activityPayloadDTO.setCreatedTime(LocalDateTime.now());
            activityPayloadDTO.setDataPayload(ArrayUtils.toPrimitive(dataPayload));
            activityPayloadDTO.setIsRequestPayload(isRequestPayload);

            //logger.info("Creating activity payload record against activity ID {}", activityDTO.getId());
            activityPayloadDTO = activityPayloadService.create(activityPayloadDTO);
        }

        //logger.info("Activity information saved for FAWB event {}", activityType.name());
    }

    /**
     * Registers the specified ProcessDesginer transaction history for the given application Id
     *
     * @param applicationId
     * @param applicationStatus
     * @param transactionHistory
     * @param subProcessTypesToExtract
     * @throws Exception
     */
    public void saveProcessDesignerEventHistory(Integer applicationId, Integer applicationStatus,
                                                ProcessExecutionHistoryVO transactionHistory, ActivityType... subProcessTypesToExtract) throws Exception {
        
    	if(logger.isInfoEnabled())
    		logger.info("-----Inside method 'saveProcessDesignerEventHistory'");

        if (transactionHistory == null) ;
        else {
            //logger.info("Activity information received for Process Designer transaction");

            if (transactionHistory.getContent() != null && !transactionHistory.getContent().isEmpty()) {
                for (Content activityContent : transactionHistory.getContent()) {
                    List<ActivityVO> activityList = addProcessOrchestratorTransactionHistory(applicationId, applicationStatus, activityContent, subProcessTypesToExtract);

                    if (activityList != null && !activityList.isEmpty()) {
                        for (ActivityVO activityVO : activityList) {
                            Activity activityDTO = new Activity();
                            activityDTO.setApplicationId(applicationId);
                            activityDTO.setApplicationStatus(applicationStatus);
                            activityDTO.setSource(activityVO.getSourceId());
                            activityDTO.setType(activityVO.getTypeId());
                            activityDTO.setDescription(activityVO.getDescription());
                            activityDTO.setName(activityVO.getName());
                            activityDTO.setStatus(activityVO.getStatus());
                            activityDTO.setUsername("System");
                            activityDTO.setStartTime(activityVO.getStartTime());
                            activityDTO.setEndTime(activityVO.getEndTime());
                            activityDTO.setDuration(activityVO.getDuration());

                            //logger.info("Creating activity record for Process Designer event {}", activityDTO.getName());
                            activityDTO = activityService.create(activityDTO);

                            //if the event has payload to store
                            if (activityVO.getActivityPayloadVO() != null) {
                                ActivityPayload activityPayloadDTO = new ActivityPayload();
                                activityPayloadDTO.setActivityId(activityDTO.getId());
                                activityPayloadDTO.setCreatedTime(activityVO.getActivityPayloadVO().getCreatedTime());
                                activityPayloadDTO.setRequestId(activityVO.getActivityPayloadVO().getRequestId());
                                activityPayloadDTO.setResponseId(activityVO.getActivityPayloadVO().getResponseId());

                                //logger.info("Creating activity payload record against activity ID {}", activityDTO.getId());
                                activityPayloadDTO = activityPayloadService.create(activityPayloadDTO);
                            }
                        }
                    } //check for activities to save
                }
            }  //check for contents

            //logger.info("Activity information saved for Process Designer transaction");
        }
    }

    /**
     * Adds a single transaction history content from the process designer
     *
     * @param applicationId
     * @param applicationStatus
     * @param activityContent
     * @throws Exception
     */
    private List<ActivityVO> addProcessOrchestratorTransactionHistory(Integer applicationId, Integer applicationStatus,
                                                                      Content activityContent, ActivityType... activityTypesToExtract) throws Exception {
    	
    	if(logger.isInfoEnabled())
    			logger.info("Inside method 'addProcessOrchestratorTransactionHistory'");

        List<ActivityVO> activityVOList = new ArrayList<>();
        if (applicationId == null) ;
        else {
            ActivityVO activityVO = new ActivityVO();
            activityVO.setApplicationId(applicationId);
            activityVO.setApplicationStatusId(applicationStatus);
            activityVO.setName(activityContent.getName().toUpperCase().equals("START") ? ((activityContent.getTransactionHistoryText() != null && !activityContent.getTransactionHistoryText().isEmpty()) ? activityContent.getTransactionHistoryText() : activityContent.getName()) : activityContent.getName());
            activityVO.setSourceId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(ActivitySource.PROCESS_DESIGNER.name(), "ACTIVITY_SOURCE", null).getId().intValue());
            activityVO.setStatus(activityContent.getStatus());
            activityVO.setUsername("System");
            logger.info("activityContent " + activityContent.getName().toUpperCase() + "-" + activityContent.getType().toUpperCase());
            activityVO.setTypeId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(
                    ((activityContent.getName().toUpperCase().contains("DECISION") && activityContent.getType().toUpperCase().equals("CALL ACTIVITY")) ? ActivityType.CALL_DECISION_ACTIVITY.name()
                            : activityContent.getName().toUpperCase().equals("IS SAPPHIRE") ? ActivityType.SCRIPT_TASK.name() : ActivityType.getActivityTypeByCode(activityContent.getType()).name()),
                    "ACTIVITY_TYPE", null).getId().intValue());
            activityVO.setStartTime((activityContent.getStart() != null) ? new Timestamp(activityContent.getStart().getTime()) : null);
            activityVO.setEndTime((activityContent.getEnd() != null) ? new Timestamp(activityContent.getEnd().getTime()) : null);
            activityVO.setDuration((activityContent.getEnd() != null && activityContent.getStart() != null) ? (int) (activityContent.getEnd().getTime() - activityContent.getStart().getTime()) : null);

            //add the activity to list
            activityVOList.add(activityVO);

            if (activityTypesToExtract == null || activityTypesToExtract.length == 0) {
                //attach subprocess activities if any
                List<ActivityVO> subProcessActivties = getFullSubprocessHistory(applicationId, applicationStatus, activityContent);
                if (subProcessActivties != null)
                    activityVOList.addAll(subProcessActivties);
            } else {
                //attach subprocess activities if any
                List<ActivityVO> subProcessActivties = getSpecificSubprocessHistory(applicationId, applicationStatus, activityContent, activityTypesToExtract);
                if (subProcessActivties != null)
                    activityVOList.addAll(subProcessActivties);
            }
        }

        return activityVOList;
    }

    /**
     * Adds all the sub-process transaction history of a given content
     *
     * @param applicationId
     * @param applicationStatus
     * @param activityContent
     * @return
     * @throws Exception
     */
    private List<ActivityVO> getFullSubprocessHistory(Integer applicationId, Integer applicationStatus, Content activityContent) throws Exception {
    	
    	if(logger.isInfoEnabled())
    		logger.info("Inside method 'getFullSubprocessHistory'");

        List<ActivityVO> fullSubProcessHistory = new ArrayList<>();
        if (activityContent == null || activityContent.getSubprocessSteps() == null || activityContent.getSubprocessSteps().isEmpty())
            ;
        else {
            for (SubprocessStep subprocessStep : activityContent.getSubprocessSteps()) {
                ActivityVO activityVO = new ActivityVO();
                activityVO.setApplicationId(applicationId);
                activityVO.setApplicationStatusId(applicationStatus);
                activityVO.setName(subprocessStep.getName().toUpperCase().equals("START") ? ((subprocessStep.getTransactionHistoryText() != null && !subprocessStep.getTransactionHistoryText().isEmpty()) ? subprocessStep.getTransactionHistoryText() : subprocessStep.getName()) : subprocessStep.getName());
                activityVO.setSourceId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(ActivitySource.PROCESS_DESIGNER.name(), "ACTIVITY_SOURCE", null).getId().intValue());
                activityVO.setStatus(subprocessStep.getStatus());
                activityVO.setUsername("System");
                activityVO.setTypeId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(
                        ((subprocessStep.getName().toUpperCase().contains("DECISION") && subprocessStep.getType().toUpperCase().equals("CALL ACTIVITY")) ? ActivityType.CALL_DECISION_ACTIVITY.name()
                                : subprocessStep.getName().toUpperCase().equals("IS SAPPHIRE") ? ActivityType.SCRIPT_TASK.name() : ActivityType.getActivityTypeByCode(subprocessStep.getType()).name()),
                        "ACTIVITY_TYPE", null).getId().intValue());
                activityVO.setStartTime((subprocessStep.getStart() != null) ? new Timestamp(subprocessStep.getStart().getTime()) : null);
                activityVO.setEndTime((subprocessStep.getEnd() != null) ? new Timestamp(subprocessStep.getEnd().getTime()) : null);
                activityVO.setDuration((subprocessStep.getEnd() != null && subprocessStep.getStart() != null) ? (int) (subprocessStep.getEnd().getTime() - subprocessStep.getStart().getTime()) : null);

                //if sub-process step type is 'CALL_ACTIVITY'
                if (ActivityType.getActivityTypeByCode(subprocessStep.getType()).equals(ActivityType.CALL_ACTIVITY) &&
                        subprocessStep.getServiceData() != null) {
                    ActivityPayloadVO activityPayloadVO = new ActivityPayloadVO();
                    activityPayloadVO.setCreatedTime(LocalDateTime.now());
                    activityPayloadVO.setRequestId((subprocessStep.getServiceData() != null) ? subprocessStep.getServiceData().getRequestIdentifier() : null);
                    activityPayloadVO.setResponseId((subprocessStep.getServiceData() != null) ? subprocessStep.getServiceData().getResponseIdentifier() : null);

                    //attach payload to activity
                    activityVO.setActivityPayloadVO(activityPayloadVO);
                }

                //add the activity to list
                fullSubProcessHistory.add(activityVO);
                processNestedSubprocessSteps(applicationId, applicationStatus, subprocessStep, fullSubProcessHistory);
            }
        }

        return fullSubProcessHistory;
    }

    /**
     * Adds specific content/activity types from sub-process history of a given content
     *
     * @param applicationId
     * @param applicationStatus
     * @param activityContent
     * @param activityTypesToExtract
     * @return
     * @throws Exception
     */
    private List<ActivityVO> getSpecificSubprocessHistory(Integer applicationId, Integer applicationStatus, Content activityContent,
                                                          ActivityType... activityTypesToExtract) throws Exception {
    	
    	if(logger.isInfoEnabled())
    		logger.info("Inside method 'getSpecificSubprocessHistory'");

        List<ActivityVO> fullSubProcessHistory = new ArrayList<>();
        if (activityContent == null || activityContent.getSubprocessSteps() == null || activityContent.getSubprocessSteps().isEmpty())
            ;
        else {
            String extractTypes = "";
            for (ActivityType type : activityTypesToExtract) {
                extractTypes = (extractTypes.isEmpty()) ? type.getCode() : (extractTypes + "," + type.getCode());
            }

            //extract the sub-process type that matches
            for (SubprocessStep subprocessStep : activityContent.getSubprocessSteps()) {
                if (extractTypes.toUpperCase().contains(subprocessStep.getType().toUpperCase())) {
                    ActivityVO activityVO = new ActivityVO();
                    activityVO.setApplicationId(applicationId);
                    activityVO.setApplicationStatusId(applicationStatus);
                    activityVO.setName(subprocessStep.getName().toUpperCase().equals("START") ? ((subprocessStep.getTransactionHistoryText() != null && !subprocessStep.getTransactionHistoryText().isEmpty()) ? subprocessStep.getTransactionHistoryText() : subprocessStep.getName()) : subprocessStep.getName());
                    activityVO.setSourceId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(ActivitySource.PROCESS_DESIGNER.name(), "ACTIVITY_SOURCE", null).getId().intValue());
                    activityVO.setStatus(subprocessStep.getStatus());
                    activityVO.setUsername("System");
                    activityVO.setTypeId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(
                            ((subprocessStep.getName().toUpperCase().contains("DECISION") && subprocessStep.getType().toUpperCase().equals("CALL ACTIVITY")) ? ActivityType.CALL_DECISION_ACTIVITY.name()
                                    : subprocessStep.getName().toUpperCase().equals("IS SAPPHIRE") ? ActivityType.SCRIPT_TASK.name() : ActivityType.getActivityTypeByCode(subprocessStep.getType()).name()),
                            "ACTIVITY_TYPE", null).getId().intValue());
                    activityVO.setStartTime((subprocessStep.getStart() != null) ? new Timestamp(subprocessStep.getStart().getTime()) : null);
                    activityVO.setEndTime((subprocessStep.getEnd() != null) ? new Timestamp(subprocessStep.getEnd().getTime()) : null);
                    activityVO.setDuration((subprocessStep.getEnd() != null && subprocessStep.getStart() != null) ? (int) (subprocessStep.getEnd().getTime() - subprocessStep.getStart().getTime()) : null);

                    //if sub-process step type is 'CALL_ACTIVITY'
                    if (ActivityType.getActivityTypeByCode(subprocessStep.getType()).equals(ActivityType.CALL_ACTIVITY) &&
                            subprocessStep.getServiceData() != null) {
                        ActivityPayloadVO activityPayloadVO = new ActivityPayloadVO();
                        activityPayloadVO.setCreatedTime(LocalDateTime.now());
                        activityPayloadVO.setRequestId((subprocessStep.getServiceData() != null) ? subprocessStep.getServiceData().getRequestIdentifier() : null);
                        activityPayloadVO.setResponseId((subprocessStep.getServiceData() != null) ? subprocessStep.getServiceData().getResponseIdentifier() : null);

                        //attach payload to activity
                        activityVO.setActivityPayloadVO(activityPayloadVO);
                    }

                    //add the activity to list
                    fullSubProcessHistory.add(activityVO);
                    processNestedSubprocessSteps(applicationId, applicationStatus, subprocessStep, fullSubProcessHistory, activityTypesToExtract);
                }  //specific type checks
            }
        }

        return fullSubProcessHistory;
    }

    /**
     * Processes the nested sub-processes recursively
     *
     * @param applicationId
     * @param applicationStatus
     * @param subprocessStep         The parent subprocess step
     * @param stepHistory            The existing step history from its parent content/subprocess step
     * @param activityTypesToExtract The type of activity events to log
     * @throws Exception
     */
    private void processNestedSubprocessSteps(Integer applicationId, Integer applicationStatus, SubprocessStep subprocessStep,
                                              List<ActivityVO> stepHistory, ActivityType... activityTypesToExtract) throws Exception {
    	
    	if(logger.isInfoEnabled())
    		logger.info("-----Inside method 'processNestedSubprocessSteps'");

        //process only when the provided subprocess has nested subprocess steps
        if (subprocessStep != null && subprocessStep.getSubprocessSteps() != null && !subprocessStep.getSubprocessSteps().isEmpty()) {
            if (activityTypesToExtract == null || activityTypesToExtract.length == 0) {
                //extract the sub-process type that matches
                for (SubprocessStep nestedSubprocessStep : subprocessStep.getSubprocessSteps()) {
                    ActivityVO activityVO = new ActivityVO();
                    activityVO.setApplicationId(applicationId);
                    activityVO.setApplicationStatusId(applicationStatus);
                    activityVO.setName(nestedSubprocessStep.getName().toUpperCase().equals("START") ? ((nestedSubprocessStep.getTransactionHistoryText() != null && !nestedSubprocessStep.getTransactionHistoryText().isEmpty()) ? nestedSubprocessStep.getTransactionHistoryText() : nestedSubprocessStep.getName()) : nestedSubprocessStep.getName());
                    activityVO.setSourceId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(ActivitySource.PROCESS_DESIGNER.name(), "ACTIVITY_SOURCE", null).getId().intValue());
                    activityVO.setStatus(nestedSubprocessStep.getStatus());
                    activityVO.setUsername("System");
                    activityVO.setTypeId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(
                            ((nestedSubprocessStep.getName().toUpperCase().contains("DECISION") && nestedSubprocessStep.getType().toUpperCase().equals("CALL ACTIVITY"))
                                    ? ActivityType.CALL_DECISION_ACTIVITY.name() : nestedSubprocessStep.getName().toUpperCase().equals("IS SAPPHIRE") ? ActivityType.SCRIPT_TASK.name() : ActivityType.getActivityTypeByCode(nestedSubprocessStep.getType()).name()),
                            "ACTIVITY_TYPE", null).getId().intValue());
                    activityVO.setStartTime((nestedSubprocessStep.getStart() != null) ? new Timestamp(nestedSubprocessStep.getStart().getTime()) : null);
                    activityVO.setEndTime((nestedSubprocessStep.getEnd() != null) ? new Timestamp(nestedSubprocessStep.getEnd().getTime()) : null);
                    activityVO.setDuration((nestedSubprocessStep.getEnd() != null && nestedSubprocessStep.getStart() != null) ? (int) (nestedSubprocessStep.getEnd().getTime() - nestedSubprocessStep.getStart().getTime()) : null);

                    //if sub-process step type is 'CALL_ACTIVITY'
                    if (ActivityType.getActivityTypeByCode(nestedSubprocessStep.getType()).equals(ActivityType.CALL_ACTIVITY) &&
                            nestedSubprocessStep.getServiceData() != null) {
                        ActivityPayloadVO activityPayloadVO = new ActivityPayloadVO();
                        activityPayloadVO.setCreatedTime(LocalDateTime.now());
                        activityPayloadVO.setRequestId((nestedSubprocessStep.getServiceData() != null) ? nestedSubprocessStep.getServiceData().getRequestIdentifier() : null);
                        activityPayloadVO.setResponseId((nestedSubprocessStep.getServiceData() != null) ? nestedSubprocessStep.getServiceData().getResponseIdentifier() : null);

                        //attach payload to activity
                        activityVO.setActivityPayloadVO(activityPayloadVO);
                    }

                    //add to existing step history
                    stepHistory.add(activityVO);
                    processNestedSubprocessSteps(applicationId, applicationStatus, nestedSubprocessStep, stepHistory);
                } //iteration for nested subprocess steps
            } else {
                String extractTypes = "";
                for (ActivityType type : activityTypesToExtract) {
                    extractTypes = (extractTypes.isEmpty()) ? type.getCode() : (extractTypes + "," + type.getCode());
                }

                //extract the sub-process type that matches
                for (SubprocessStep nestedSubprocessStep : subprocessStep.getSubprocessSteps()) {
                    if (extractTypes.toUpperCase().contains(nestedSubprocessStep.getType().toUpperCase())) {
                        ActivityVO activityVO = new ActivityVO();
                        activityVO.setApplicationId(applicationId);
                        activityVO.setApplicationStatusId(applicationStatus);
                        activityVO.setName(nestedSubprocessStep.getName().toUpperCase().equals("START") ? ((nestedSubprocessStep.getTransactionHistoryText() != null && !nestedSubprocessStep.getTransactionHistoryText().isEmpty()) ? nestedSubprocessStep.getTransactionHistoryText() : nestedSubprocessStep.getName()) : nestedSubprocessStep.getName());
                        activityVO.setSourceId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(ActivitySource.PROCESS_DESIGNER.name(), "ACTIVITY_SOURCE", null).getId().intValue());
                        activityVO.setStatus(nestedSubprocessStep.getStatus());
                        activityVO.setUsername("System");
                        activityVO.setTypeId(domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(
                                ((nestedSubprocessStep.getName().toUpperCase().contains("DECISION") && nestedSubprocessStep.getType().toUpperCase().equals("CALL ACTIVITY"))
                                        ? ActivityType.CALL_DECISION_ACTIVITY.name() : nestedSubprocessStep.getName().toUpperCase().equals("IS SAPPHIRE") ? ActivityType.SCRIPT_TASK.name() : ActivityType.getActivityTypeByCode(nestedSubprocessStep.getType()).name()),
                                "ACTIVITY_TYPE", null).getId().intValue());
                        activityVO.setStartTime((nestedSubprocessStep.getStart() != null) ? new Timestamp(nestedSubprocessStep.getStart().getTime()) : null);
                        activityVO.setEndTime((nestedSubprocessStep.getEnd() != null) ? new Timestamp(nestedSubprocessStep.getEnd().getTime()) : null);
                        activityVO.setDuration((nestedSubprocessStep.getEnd() != null && nestedSubprocessStep.getStart() != null) ? (int) (nestedSubprocessStep.getEnd().getTime() - nestedSubprocessStep.getStart().getTime()) : null);

                        //if sub-process step type is 'CALL_ACTIVITY'
                        if (ActivityType.getActivityTypeByCode(nestedSubprocessStep.getType()).equals(ActivityType.CALL_ACTIVITY) &&
                                nestedSubprocessStep.getServiceData() != null) {
                            ActivityPayloadVO activityPayloadVO = new ActivityPayloadVO();
                            activityPayloadVO.setCreatedTime(LocalDateTime.now());
                            activityPayloadVO.setRequestId((nestedSubprocessStep.getServiceData() != null) ? nestedSubprocessStep.getServiceData().getRequestIdentifier() : null);
                            activityPayloadVO.setResponseId((nestedSubprocessStep.getServiceData() != null) ? nestedSubprocessStep.getServiceData().getResponseIdentifier() : null);

                            //attach payload to activity
                            activityVO.setActivityPayloadVO(activityPayloadVO);
                        }

                        //add to existing step history
                        stepHistory.add(activityVO);
                        processNestedSubprocessSteps(applicationId, applicationStatus, nestedSubprocessStep, stepHistory, activityTypesToExtract);
                    }
                }  //iteration for nested subprocess steps
            }
        }
    }

    /**
     * Accepts the 'datetime' as a string value and converts the datetime string into its
     * equivalent SQL DateTime value
     *
     * @param dateString
     * @return The equivalent SQL DateTime value if the supplied datetime string is parsable
     * as per pattern mentioned below otherwise returns a null
     * @apiNote This only parses the datetime string with pattern <b>yyyy-MM-dd HH:mm:ss:SSS</b>
     */
    private LocalDateTime convertDateStringToLocalDateTime(String dateString) {
    	if(logger.isInfoEnabled())
    		logger.info("--------Inside method 'convertDateStringToLocalDateTime'");
        //logger.info("Received dateString is: {}", dateString);

        LocalDateTime localDateTime = null;

        if (dateString == null || dateString.isEmpty())
            return null;

        try {
            final String datePattern = "yyyy-MM-dd HH:mm:ss:SSS";
            if (dateString.length() >= 24)
                dateString = dateString.substring(0, 23);

            java.util.Date parsedDate = new SimpleDateFormat(datePattern).parse(dateString);
            localDateTime = parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            //logger.info("The SQL DateTime equivalent of the supplied date string is: {}", localDateTime.toString());
        } catch (Exception e) {
            localDateTime = null;
        }

        return localDateTime;
    }

    /**
     * enum holding the exact domain value CODEs for the ACTIVITY_SOURCE
     */
    public enum ActivitySource {
        FAWB(1, "FAWB"),
        PROCESS_DESIGNER(2, "PROCESS DESIGNER");

        private Integer id;
        private String code;

        ActivitySource(int id, String code) {
            this.id = id;
            this.code = code;
        }

        /**
         * Returns the code associated to this enum supplied
         *
         * @param activitySource
         * @return
         */
        public String getCode() {
            return this.code;
        }

        /**
         * Returns the {@link ActivitySource} with matching code
         *
         * @param paramTypeCode
         * @return
         */
        public static ActivitySource getActivitySourceByCode(String paramTypeCode) {
            if (paramTypeCode == null)
                return null;
            else {
                List<ActivitySource> sources = Arrays.asList(ActivitySource.values());
                ActivitySource matchedSource = null;

                for (ActivitySource source : sources) {
                    if (source.code.equalsIgnoreCase(paramTypeCode)) {
                        matchedSource = source;
                        break;
                    }
                }

                return matchedSource;
            }
        }
    }

    /**
     * enum holding the exact domain value CODEs for the ACTIVITY_TYPE
     */
    public enum ActivityType {
        EVENT(1, "EVENT"),
        USER_ACTION(2, "USER ACTION"),
        CALL_ACTIVITY(3, "CALL ACTIVITY"),
        CALL_DECISION_ACTIVITY(4, "CALL DECISION ACTIVITY"),
        SCRIPT_TASK(5, "SCRIPT TASK"),
        SUB_PROCESS(6, "SUB PROCESS"),
        ERROR(7, "ERROR"),
        GATEWAY(8, "GATEWAY");

        private Integer id;
        private String code;

        ActivityType(int id, String code) {
            this.id = id;
            this.code = code;
        }

        /**
         * Returns the code associated to this enum supplied
         *
         * @param activitySource
         * @return
         */
        public String getCode() {
            return this.code;
        }

        /**
         * Returns the {@link ActivityType} with matching code
         *
         * @param paramTypeCode
         * @return
         */
        public static ActivityType getActivityTypeByCode(String paramTypeCode) {
            if (paramTypeCode == null)
                return null;
            else {
                List<ActivityType> types = Arrays.asList(ActivityType.values());
                ActivityType matchedType = null;

                for (ActivityType type : types) {
                    if (type.code.equalsIgnoreCase(paramTypeCode)) {
                        matchedType = type;
                        break;
                    }
                }

                return matchedType;
            }
        }
    }

    /**
     * Utility class that serves as a wrapper for paginated activity log dataset,
     * such that pagination and sorting actions can be easily manipulated at UI
     */
    public class ActivityLogResponseWrapper {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private List<QueryActivityLogResponse> activityList;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        public List<QueryActivityLogResponse> getActivityList() {
            return activityList;
        }

        public void setActivityList(List<QueryActivityLogResponse> activityList) {
            this.activityList = activityList;
        }

    }

}
