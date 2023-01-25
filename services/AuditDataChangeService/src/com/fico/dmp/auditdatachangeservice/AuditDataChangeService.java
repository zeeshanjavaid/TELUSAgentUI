/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.auditdatachangeservice;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;

import com.fico.core.services.AuditDataChangeServiceBS;
import com.fico.dmp.telusagentuidb.AuditDataChange;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;


/**
 * This is a singleton class with all its public methods exposed as REST APIs via generated controller class.
 * To avoid exposing an API for a particular public method, annotate it with @HideFromClient.
 *
 * Method names will play a major role in defining the Http Method for the generated APIs. For example, a method name
 * that starts with delete/remove, will make the API exposed as Http Method "DELETE".
 *
 * Method Parameters of type primitives (including java.lang.String) will be exposed as Query Parameters &
 * Complex Types/Objects will become part of the Request body in the generated API.
 */
@ExposeToClient
public class AuditDataChangeService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(AuditDataChangeService.class.getName());

    @Autowired
    @Qualifier("facade.AuditDataChangeServiceBS")
    private AuditDataChangeServiceBS auditDataChangeServiceBS;
    			
    public CustomPageData_AuditChangeSearchFilters getAuditDataChangeListByFilters(Integer applicationId, Integer currentPage, Integer pageSize, String sortOrders, String userLocaleArg) {
    	CustomPageData_AuditChangeSearchFilters customPageDataWrapper = new CustomPageData_AuditChangeSearchFilters();
    	try {
    	    
    		Object[] auditDataList = 
    				auditDataChangeServiceBS.getAuditDataChangeListByFilters(applicationId, currentPage, pageSize, sortOrders, userLocaleArg);
    		
    		customPageDataWrapper.setPageNumber((Integer) auditDataList[0]);
    		customPageDataWrapper.setPageSize((Integer) auditDataList[1]);
    		customPageDataWrapper.setTotalRecords((Long) auditDataList[2]);
    		customPageDataWrapper.setPageContent((List<AuditDataChange>) auditDataList[3]); 
    	}
    	catch (Exception e) {
    		if(logger.isErrorEnabled())
    			logger.error("Unexpected error occurred while fetching AuditDataChange(s) with supplied filters.", e);
		}
    	
    	return customPageDataWrapper;
    }
    
    
    public class CustomPageData_AuditChangeSearchFilters {
    	Integer pageNumber;
    	Integer pageSize;
    	Long totalRecords;
    	List<AuditDataChange> pageContent;
    	
    	public Integer getPageNumber() {
    		return pageNumber;
    	}
    	public void setPageNumber(Integer pageNumber) {
    		this.pageNumber = pageNumber;
    	}
    	public Integer getPageSize() {
    		return pageSize;
    	}
    	public void setPageSize(Integer pageSize) {
    		this.pageSize = pageSize;
    	}
    	public Long getTotalRecords() {
    		return totalRecords;
    	}
    	public void setTotalRecords(Long totalRecords) {
    		this.totalRecords = totalRecords;
    	}
    	public List<AuditDataChange> getPageContent() {
    		return pageContent;
    	}
    	public void setPageContent(List<AuditDataChange> pageContent) {
    		this.pageContent = pageContent;
    	}
    }

}