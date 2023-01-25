/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.pscomponent.propertiesservice;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fico.pscomponent.handlers.PropertiesHandler;
import com.fico.pscomponent.handlers.PropertiesHandler.LoadResponse;
import com.fico.pscomponent.service.property.FawbPropertySourceRefresher;
import com.fico.pscomponent.service.property.PropertyRefreshService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;

import com.fico.ps.cache.DistributedRefreshUtil;

@ExposeToClient
public class PropertiesService {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesService.class);
    
    @Autowired 
    private DistributedRefreshUtil distributedRefreshUtil;

    @Autowired
    private Environment environment;

    @Autowired
    private PropertiesHandler propertiesHandler;

    @Autowired
    private PropertyRefreshService propertyRefreshService;

    public Map<String, String> getProperties(HttpServletRequest request) {
    	return propertiesHandler.getProperties();
    }

    public String getPropertyValueByName(String name, HttpServletRequest request) {
        if(logger.isInfoEnabled())
            logger.info("searching property: {}", name);
    	return propertiesHandler.getPropertyValueByName(name);
    }

    public ResponseEntity<LoadResponse> loadDefaultProperties(HttpServletRequest request) {
        LoadResponse loadResponse = propertiesHandler.loadDefaultProperties();
        refreshProperties();
        return createResponse(loadResponse);
    }
    
    /* Method reads the csv,xls file and uploads the properties into the system*/

    public ResponseEntity<LoadResponse> loadCustomProperties(MultipartFile file, HttpServletRequest request) {
        if(logger.isInfoEnabled())
            logger.info("In properties service loadCustomProperties :::::::::::::::::::::::::");
    	LoadResponse loadResponse = propertiesHandler.loadCustomProperties(file);
    	refreshProperties();
    	return createResponse(loadResponse);
    }
    
    @CacheEvict(value="getPropertyValueByName",allEntries=true)
    public void refreshProperties() {
        if(logger.isInfoEnabled())
            logger.info("In properties service refreshProperties ");
        
        //Replaced local refresh with hazelcast cluster refresh.
        distributedRefreshUtil.properties();
    	//propertyRefreshService.refreshProperties();
    }

    private ResponseEntity<LoadResponse> createResponse(LoadResponse loadResponse) {
        if(logger.isInfoEnabled())
            logger.info("loadResponse: {}", loadResponse);
    	if (loadResponse.isSuccess()) {
        	return new ResponseEntity<LoadResponse>(loadResponse, HttpStatus.OK);
        } else {
        	return new ResponseEntity<LoadResponse>(loadResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Boolean getAuditFlagValue(String name, HttpServletRequest request) {
        if(name == null || !name.startsWith("app.environment.ps.")){
            return false;
        }
        if(logger.isInfoEnabled())
            logger.info("searching Audit related property: {}", name);
        return environment.getProperty(name, Boolean.class);
    }
}
