package com.fico.core.services;

import com.fico.core.util.CommonUtils;
import com.fico.dmp.telusagentuidb.AuditDataChange;
import com.fico.dmp.telusagentuidb.models.query.QueryGetDomainValueByIdResponse;
import com.fico.dmp.telusagentuidb.service.AuditDataChangeService;
import com.fico.ps.hermes.audit.ChangeData;
import com.fico.ps.hermes.util.JacksonUtility;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("facade.AuditDataChangeServiceBS")
public class AuditDataChangeServiceBS {

    @Autowired
    AuditDataChangeService auditDataChangeService;

    @Autowired
    DomainValueServiceBS domainValueServiceBS;

    @Autowired
    @Qualifier("core.CommonUtils")
    private CommonUtils commonUtils;

    static final String ACTION_ADD = "CREATE";
    static final String ACTION_DELETE = "DELETE";
    static final String ACTION_UPDATE = "UPDATE";

    private static final Logger logger = LoggerFactory.getLogger(AuditDataChangeServiceBS.class);

    //		@Transactional(value = "CoreTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public Object[] getAuditDataChangeListByFilters(Integer applicationId, Integer currentPage, Integer pageSize,
                                                    String sortOrders, String userLocaleArg) throws Exception {
    	if(logger.isDebugEnabled())
    		logger.debug("inside getAuditDataChangeListByFilters");

        try {
            Page<AuditDataChange> auditDataChangeList = null;
            Pageable pageable = null;

            // set up pagination parameters
            if (currentPage == null && pageSize == null && (sortOrders == null || sortOrders.trim().length() == 0)) {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
            } else {
                List<String> sortOrderList = null;

                if (currentPage == null)
                    currentPage = 0;
                if (pageSize == null)
                    pageSize = Integer.MAX_VALUE;
                if (sortOrders == null || sortOrders.trim().length() == 0) {
                    sortOrderList = new ArrayList<String>(1);
                    // set default sort order on 'code' ASC
                    sortOrderList.add("createdOn ASC");
                } else {
                    sortOrderList = Arrays.asList(sortOrders.split(","));
                }

                // create the pageable instance
                // 1. set sort orders and sort
                List<Sort.Order> soList = new ArrayList<Sort.Order>();
                sortOrderList.forEach((sortOrder) -> {
                    final String[] sortMetadata = sortOrder.split(" ");
                    final String propertyName = sortMetadata[0];
                    final String propertyOrder = (sortMetadata.length > 1) ? sortMetadata[1].toUpperCase() : "DESC";

                    soList.add(new Sort.Order(Sort.Direction.fromString(propertyOrder), propertyName));
                });

                // 2. set pageable param
                pageable = PageRequest.of(currentPage, pageSize, Sort.by(soList));

            }

            auditDataChangeList = auditDataChangeService.findAll("applicationId =" + applicationId, pageable);

            List<AuditDataChange> processAuditList = new ArrayList<AuditDataChange>();
            for (AuditDataChange change : auditDataChangeList) {
                processAuditList.add(copyAuditDataChange(change));
            }

            JacksonUtility utility = new JacksonUtility();
            utility.initialize();

            prepareChangeData(processAuditList, utility, userLocaleArg);

            Object[] pageData = new Object[4];

            pageData[0] = auditDataChangeList.getNumber();
            pageData[1] = auditDataChangeList.getSize();
            pageData[2] = auditDataChangeList.getTotalElements();
            List<AuditDataChange> modifiedList = commonUtils.setNullToEmptyForStringProperties(AuditDataChange.class, processAuditList);
            pageData[3] = modifiedList;

            return pageData;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private AuditDataChange copyAuditDataChange(AuditDataChange change) {
        AuditDataChange auditDataChange = new AuditDataChange();
        auditDataChange.setAction(change.getAction());
        auditDataChange.setApplication(change.getApplication());
        auditDataChange.setApplicationId(change.getApplicationId());
        auditDataChange.setChangeData(change.getChangeData());
        auditDataChange.setCreatedBy(change.getCreatedBy());
        auditDataChange.setCreatedOn(change.getCreatedOn());
        auditDataChange.setDomainClassName(change.getDomainClassName());
        auditDataChange.setEntityId(change.getEntityId());
        auditDataChange.setEntityName(change.getEntityName());
        auditDataChange.setId(change.getId());
        auditDataChange.setParentDomainClassName(change.getParentDomainClassName());
        auditDataChange.setParentEntityId(change.getParentEntityId());
        auditDataChange.setParentEntityName(change.getParentEntityName());
        auditDataChange.setUser(change.getUser());
        return auditDataChange;
    }

    private void prepareChangeData(List<AuditDataChange> processAuditList, JacksonUtility utility, String userLocaleArg) throws Exception {
        try {
            JSONArray jsonArrObject = readJsonFile();

            for (AuditDataChange auditDataChange : processAuditList) {
                String updateColumnString = "";
                ChangeData[] changedatasArr = (ChangeData[]) utility.deSerialize(auditDataChange.getChangeData(), ChangeData[].class);

                switch (auditDataChange.getAction()) {
                    case ACTION_ADD:
                        for (ChangeData changeData : changedatasArr) {
                            updateColumnString = updateColumnString + changeData.getPropertyName() + ": " + checkAndReturnDVCodeFromID(jsonArrObject, auditDataChange.getDomainClassName(), changeData.getPropertyName(), changeData.getNewValue(), ACTION_ADD, userLocaleArg) + "</br>";
                        }
                        break;
                    case ACTION_UPDATE:
                        for (ChangeData changeData : changedatasArr) {
                            updateColumnString = updateColumnString + changeData.getPropertyName() + ": from " +
                                    checkAndReturnDVCodeFromID(jsonArrObject, auditDataChange.getDomainClassName(), changeData.getPropertyName(), changeData.getOldValue(), ACTION_UPDATE, userLocaleArg) + " to " +
                                    checkAndReturnDVCodeFromID(jsonArrObject, auditDataChange.getDomainClassName(), changeData.getPropertyName(), changeData.getNewValue(), ACTION_UPDATE, userLocaleArg) + "</br>";
                        }
                        break;
                    case ACTION_DELETE:
                        break;
                }
                auditDataChange.setChangeData(updateColumnString);
                if(logger.isDebugEnabled())
                	logger.debug("auditDataChange.getChangeData() Data " + ReflectionToStringBuilder.toString(auditDataChange.getChangeData()));
            }

        } catch (Exception e) {
        	if(logger.isErrorEnabled())
        		logger.error("Fatal error occured in prepareChangeData", e);
            throw new Exception(e);
        }
    }

    private String checkAndReturnDVCodeFromID(JSONArray jsonArrObject, String domainClassName, String propertyName, String newValue, String action, String userLocaleArg) 
    		throws NumberFormatException, Exception {
        
    	 if(logger.isDebugEnabled())
    		 logger.debug("EntityName:" + domainClassName + " PropertyName:" + propertyName + " newValue:" + newValue);
    
        for (Object obj : jsonArrObject) {
            JSONObject jsonObj = (JSONObject) obj;

            String entity = (String) jsonObj.get("domainClassName");

            if (entity.trim().equals(domainClassName.trim())) {
                JSONArray fields = (JSONArray) jsonObj.get("Fields");
                for (Object fieldObj : fields) {
                    String field = (String) fieldObj;
                    if (field.toUpperCase().trim().equals(propertyName.toUpperCase().trim())) {
                        if(newValue!=null && !newValue.equals("0")) {
                            newValue = getDVCodefromDVId(newValue, userLocaleArg);
                        }
                        break;
                    }
                }
            }
        }
        if (action == ACTION_UPDATE)
            return (newValue == null || newValue == "") ? "''" : newValue;
        else
            return newValue;
    }

    private String getDVCodefromDVId(String newValue, String userLocaleArg) {
    	 if(logger.isDebugEnabled())
    		 logger.debug("getDVCodefromDVId called on : " + newValue);
        QueryGetDomainValueByIdResponse queryGetDomainValueByIdResponse;
        try {
            queryGetDomainValueByIdResponse = domainValueServiceBS.getDomainValueByDomainValueId(Integer.parseInt(newValue), userLocaleArg);
        } catch (Exception e) {
        	 if(logger.isErrorEnabled())
        		 logger.error("Error fetching code from id:" + newValue, e);
            return newValue;
        }
        return queryGetDomainValueByIdResponse.getCode();
    }

    private JSONArray readJsonFile() throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray arr = new JSONArray();
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("DomainValueFields.json");
            Reader reader = new InputStreamReader(inputStream);
            arr = (JSONArray) parser.parse(reader);
        } catch (FileNotFoundException e) {
        	if(logger.isErrorEnabled())
        		logger.error("File not found while reading file", e);
            throw new Exception(e);
        } catch (IOException e) {
        	if(logger.isErrorEnabled())
        		logger.error("Fatal error occured in readJsonFile", e);
            throw new Exception(e);
        } catch (ParseException e) {
        	if(logger.isErrorEnabled())
        		logger.error("Fatal error occured in parsing the JSON file", e);
            throw new Exception(e);
        }
        return arr;
    }

}
