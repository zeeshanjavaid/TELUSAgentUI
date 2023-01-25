package com.fico.pscomponent.service.logging;

import org.springframework.stereotype.Service;

@Service
public class ActivityLogBOService {/*

	private static final Logger logger = LoggerFactory.getLogger(ActivityLogBOService.class);
	
	@Autowired
	private ActivitylogService activityLogService;

	@Autowired
	private ActivitypayloadService activityPayloadService;

	@Autowired
	private ActivitylogtypeService activityLogTypeService;
	
	@Autowired
	private ActivitylognameService activityLogNameService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private PSComponentQueryExecutorService psComponentQueryExecutorService;

	@Autowired
	private PropertiesUtil propertiesUtil;

	@Value("${app.environment.log.externalTime.enabled}")
	private boolean isExternalTimeEnabled;

	private String createdBy;

	public String getCreatedBy() {
		return this.createdBy;
	}

	@PostConstruct
	public void init() {
		this.createdBy = propertiesUtil.getPropertyValue("system.user.id");
	}

	@Override
	public ActivityLogBOResponse createActivityLog(ActivityLogBO activityLogBO) {
		logger.debug("----- Creating an activity log entry -----");
		ActivityLogBOResponse response = new ActivityLogBOResponse();

		try {
			logger.debug("----- Validating an activity log entry -----");
			ActivityLogBOValidationService.validate(activityLogBO);
		} catch (ActivityLogBOValidationException validationException) {
			logger.error("Validation Exception : ", validationException);
			response.setSuccess(false);
			response.setClientError(true);
			response.setErrorMessage(validationException.getMessage());
			return response;
		}

		//Get Activity Log Type
		Activitylogtype activityLogType = null;
		try {
			activityLogType = activityLogTypeService.getByCode(activityLogBO.getLogType());
		} catch (EntityNotFoundException exception) {
			logger.error("Log Type not found : ", exception);
			response.setSuccess(false);
			response.setClientError(true);
			response.setErrorMessage("Log Type " + activityLogBO.getLogType() + " not found");
			return response;
		}

		//Get Activity Log Name
		Activitylogname activityLogName = null;
		try {
		    if (!Objects.isNull(activityLogBO.getName())) {
				logger.debug("Activity Log name {}", activityLogBO.getName());
				activityLogName = activityLogNameService.getByCode(activityLogBO.getName());
			}
		} catch (EntityNotFoundException exception) {
			logger.error("Log Name not found : ", exception);
		}

		if (!Objects.isNull(activityLogBO.getLogId())) {
			Activitylog dbActivityLog = null;
			try {
				dbActivityLog = activityLogService.getById(activityLogBO.getLogId());
			} catch (EntityNotFoundException exception) {
				logger.error("Activity Log not found: ", exception);
				response.setSuccess(false);
				response.setClientError(true);
				response.setErrorMessage("Activity Log not found for id " + activityLogBO.getLogId());
				return response;
			}
			logger.debug("------------ Activity Log Present ------------");
			return updateActivityLog(activityLogBO, dbActivityLog, response);
		} else {
			return createOrUpdateActivityLog(activityLogBO, activityLogType, activityLogName, response);
		}
	}

	// Call this method to create unique activity log entry without considering fico Id and Log Name.
	// this method allows to insert activity log without fico Id as well
	@Override
	public ActivityLogBOResponse createActivityLogUniqueEntry(ActivityLogBO activityLogBO) {
		logger.debug("----invoke:  createActivityLogUniqueEntry -----");
		ActivityLogBOResponse response = new ActivityLogBOResponse();

		try {
			logger.debug("----- Validating an activity log entry -----");
			ActivityLogBOValidationService.validate(activityLogBO);
		} catch (ActivityLogBOValidationException validationException) {
			logger.error("Validation Exception : ", validationException);
			response.setSuccess(false);
			response.setClientError(true);
			response.setErrorMessage(validationException.getMessage());
			return response;
		}

		//Get Activity Log Type
		Activitylogtype activityLogType = null;
		try {
			activityLogType = activityLogTypeService.getByCode(activityLogBO.getLogType());
		} catch (EntityNotFoundException exception) {
			logger.error("Log Type not found : ", exception);
			response.setSuccess(false);
			response.setClientError(true);
			response.setErrorMessage("Log Type " + activityLogBO.getLogType() + " not found");
			return response;
		}

		//Get Activity Log Name
		Activitylogname activityLogName = null;
		try {
		    if (!Objects.isNull(activityLogBO.getName())) {
				logger.debug("Activity Log name {}", activityLogBO.getName());
				activityLogName = activityLogNameService.getByCode(activityLogBO.getName());
			}
		} catch (EntityNotFoundException exception) {
			logger.error("Log Name not found : ", exception);
		}
		
		return createActivityLog(activityLogBO, activityLogType, activityLogName, response);
	}

	private ActivityLogBOResponse createActivityLog(ActivityLogBO activityLogBO, Activitylogtype activityLogType,
			Activitylogname activityLogName, ActivityLogBOResponse response) {
		try {
			// Create Activity Payload Entry
			Activitypayload activityPayload = new Activitypayload();
			activityPayload.setIsSuccess(activityLogBO.getIsSuccess());
			activityPayload.setIsTimeout(activityLogBO.getIsTimeout());
			activityPayload.setIsError(activityLogBO.getIsError());
			activityPayload.setIsNoHit(activityLogBO.getIsNoHit());
			activityPayload.setDoNotDeleteFlag(activityLogBO.getDoNotDeleteFlag());
			if (!Objects.isNull(activityLogBO.getRequestTS())) {
				activityPayload.setRequestTs(activityLogBO.getRequestTS().getTime());
			} else {
				activityPayload.setRequestTs(System.currentTimeMillis());
			}
			if (!Objects.isNull(activityLogBO.getResponseTS())) {
				activityPayload.setResponseTs(activityLogBO.getResponseTS().getTime());
			}
			activityPayload.setResultMessage(activityLogBO.getResultMessage());
			activityPayload.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			activityPayload.setCreatedBy(getCreatedBy());

			if (!Objects.isNull(activityLogBO.getRequestTS()) && !Objects.isNull(activityLogBO.getResponseTS())) {
				// set Time taken in milliseconds
				long timeTakenInMilSec = activityLogBO.getResponseTS().getTime() - activityLogBO.getRequestTS().getTime();
				activityPayload.setTimeTakeninMilliSec(timeTakenInMilSec);
			}

			if (!Objects.isNull(activityLogBO.getRequestPayload())) {
			    
				//Skipping Unescape to allow intner json for policy_details_api to be persisted in the log file as received in the request
				// String reqPayload = StringEscapeUtils.unescapeHtml4(activityLogBO.getRequestPayload());
				// FraudCheckUtil fraudUtil = new FraudCheckUtil();
                String reqPayload = activityLogBO.getRequestPayload().replace("&quot;","\\u0022");
    //             reqPayload = fraudUtil.removeTranslationFields(reqPayload);
                
				activityPayload.setRequestPayload(reqPayload.getBytes());
			}

			if (!Objects.isNull(activityLogBO.getResponsePayload())) {
				String resPayload = StringEscapeUtils.unescapeHtml4(activityLogBO.getResponsePayload());
				if (isExternalTimeEnabled) {
					Long bureauTime = extractBureauTimeFromResponse(resPayload);
					if (!Objects.isNull(bureauTime)) {
						activityPayload.setExternalTime(bureauTime);	
					}
				}
				activityPayload.setResponsePayload(resPayload.getBytes());
			}

			Activitypayload dbActivityPayload = activityPayloadService.create(activityPayload);
			logger.debug("----- Activity payload inserted successfully -----");

			// Create Activity Log Entry
			Activitylog activityLog = new Activitylog();
			//Get the application from applicationId
			if (!Objects.isNull(activityLogBO.getFicoId())) {
				try {
					Application application =  applicationService.getById(activityLogBO.getFicoId());
					activityLog.setApplicationId(application.getId());
					activityLog.setApplication(application);
				} catch (EntityNotFoundException exception) {
					logger.error("Application not found: ", exception);
				}
			}

			activityLog.setDescription(activityLogBO.getDescription());
			activityLog.setDoNotDeleteFlag(activityLogBO.getDoNotDeleteFlag());
			activityLog.setActivitypayload(dbActivityPayload);
			activityLog.setActivitylogtype(activityLogType);
			activityLog.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			activityLog.setCreatedBy(getCreatedBy());
			activityLog.setActivitylogname(activityLogName);

			activityLog = activityLogService.create(activityLog);
			logger.debug("----- Activity log inserted successfully -----");
			
			response.setSuccess(true);
			response.setLogId(activityLog.getId());
			response.setResult("Activity log entry created successfully");
			return response;
		} catch (Exception e) {
			logger.error("Exception caught: ", e);
			response.setSuccess(false);
			response.setErrorMessage("Failed to create activity log entry: " + e.getMessage());
			return response;
		}
	}

	private ActivityLogBOResponse updateActivityLog(ActivityLogBO activityLogBO, Activitylog dbActivityLog,
			ActivityLogBOResponse response) {
		try {
			Activitypayload dbActivityPayload = dbActivityLog.getActivitypayload();

			if (!Objects.isNull(activityLogBO.getDescription())) {
				dbActivityLog.setDescription(activityLogBO.getDescription());
			}

			if (!Objects.isNull(activityLogBO.getIsSuccess())) {
				dbActivityPayload.setIsSuccess(activityLogBO.getIsSuccess());
			}

			if (!Objects.isNull(activityLogBO.getIsTimeout())) {
				dbActivityPayload.setIsTimeout(activityLogBO.getIsTimeout());
			}

			if (!Objects.isNull(activityLogBO.getIsError())) {
				dbActivityPayload.setIsError(activityLogBO.getIsError());
			}

			if (!Objects.isNull(activityLogBO.getIsNoHit())) {
				dbActivityPayload.setIsNoHit(activityLogBO.getIsNoHit());
			}

			if (!Objects.isNull(activityLogBO.getResultMessage())) {
				dbActivityPayload.setResultMessage(activityLogBO.getResultMessage());
			}

			if (!Objects.isNull(activityLogBO.getRequestPayload())) {
				String reqPayload = StringEscapeUtils.unescapeHtml4(activityLogBO.getRequestPayload());
				dbActivityPayload.setRequestPayload(reqPayload.getBytes());
			}

			if (!Objects.isNull(activityLogBO.getResponsePayload())) {
				String resPayload = StringEscapeUtils.unescapeHtml4(activityLogBO.getResponsePayload());
				if (isExternalTimeEnabled) {
					Long bureauTime = extractBureauTimeFromResponse(resPayload);
					if (!Objects.isNull(bureauTime)) {
						dbActivityPayload.setExternalTime(bureauTime);
					}
				}
				dbActivityPayload.setResponsePayload(resPayload.getBytes());
			}

			Long responseTS = System.currentTimeMillis();
			if (Objects.isNull(activityLogBO.getResponseTS())) {
				activityLogBO.setResponseTS(new Timestamp(responseTS));
			}

			dbActivityPayload.setResponseTs(activityLogBO.getResponseTS().getTime());

            if (!Objects.isNull(activityLogBO.getRequestTS())) {
				dbActivityPayload.setRequestTs(activityLogBO.getRequestTS().getTime());
			}

			if (!Objects.isNull(dbActivityPayload.getResponseTs()) && !Objects.isNull(dbActivityPayload.getRequestTs())) {
				// set Time taken in milliseconds
				long timeTakenInMilSec = dbActivityPayload.getResponseTs() - dbActivityPayload.getRequestTs();
				dbActivityPayload.setTimeTakeninMilliSec(timeTakenInMilSec);
			}

			dbActivityLog.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			dbActivityLog.setUpdatedBy(getCreatedBy());
			dbActivityPayload.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			dbActivityPayload.setUpdatedBy(getCreatedBy());
			dbActivityPayload = activityPayloadService.update(dbActivityPayload);

			dbActivityLog.setActivitypayload(dbActivityPayload);
			activityLogService.update(dbActivityLog);

			response.setLogId(dbActivityLog.getId());
			response.setSuccess(true);
			response.setResult("Activity log entry updated successfully");
			return response;

		} catch (Exception e) {
			logger.error("Exception caught: ", e);
			response.setSuccess(false);
			response.setErrorMessage("Failed to update activity log entry: " + e.getMessage());
			return response;
		}
	}

    private ActivityLogBOResponse createOrUpdateActivityLog(ActivityLogBO activityLogBO, Activitylogtype activityLogType, Activitylogname activityLogName, ActivityLogBOResponse response) {
    	logger.debug("Invoked: createOrUpdateActivityLog()");

    	if (Objects.isNull(activityLogName)) {
			return createActivityLog(activityLogBO, activityLogType, activityLogName, response);
		}

		//check if the entry already exists
		//i.e same application id and serial id exist
		Pageable pageable = PageRequest.of(0, 1);
		Page<GetActivityLogByAppIdAndNameResponse> pageResponse = psComponentQueryExecutorService.executeGetActivityLogByAppIdAndName(activityLogBO.getFicoId(), activityLogName.getId(), pageable);
		if (pageResponse.hasContent()) {
		    logger.debug("Activity log Entry exists for ficoId {} and name {}", activityLogBO.getFicoId(), activityLogBO.getName());
		    GetActivityLogByAppIdAndNameResponse logResponse = pageResponse.getContent().get(0);
			Activitylog dbActivityLog = null;
			try {
			    logger.debug("Recieved Activity Id {}", logResponse.getId());
				dbActivityLog = activityLogService.getById(Integer.parseInt(logResponse.getId().toString()));
				return updateActivityLog(activityLogBO, dbActivityLog, response);
			}  catch (EntityNotFoundException exception) {
				logger.error("Activity Log not found: ", exception);
			}
		} else {
			logger.debug("Activity log Entry does not exists for ficoId {} and name {}", activityLogBO.getFicoId(), activityLogBO.getName());
		}

		return createActivityLog(activityLogBO, activityLogType, activityLogName, response);
	}

    private Long extractBureauTimeFromResponse(String responsePayload) {
		try {
			JSONObject responseJsonObj = new JSONObject(responsePayload);
			if (!responseJsonObj.has("outputs")) {
				return null;
			}
			JSONArray outputsJsonArray = responseJsonObj.getJSONArray("outputs");
			JSONObject outputsJsonObj = outputsJsonArray.getJSONObject(0);
			String value = outputsJsonObj.getString("value");
			try (StringReader reader = new StringReader(value)) {
				InputSource inputXML = new InputSource(reader);
				XPath xPath = XPathFactory.newInstance().newXPath();
				String bureauTimeStr = xPath.evaluate("/Timings/BureauTime", inputXML);
				Long bureaTime = Long.parseLong(bureauTimeStr);
				logger.info("Bureau Time {}", bureaTime);
				return bureaTime;
			}
		} catch (Exception e) {
			logger.error("Exception while processing bureauTime {}", e.getMessage());
			return null;
		}
	}
*/}
