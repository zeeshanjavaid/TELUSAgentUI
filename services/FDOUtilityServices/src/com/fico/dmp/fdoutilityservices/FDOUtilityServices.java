/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.fdoutilityservices;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import defaulttermlibrary.Application;
import defaulttermlibrary.Party;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import com.fico.dmp.telusagentuidb.FawbPropertySource;
import com.fico.dmp.telusagentuidb.service.FawbPropertySourceService;

//import com.fico.dmp.fdoutilityservices.model.*;

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
public class FDOUtilityServices {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(FDOUtilityServices.class.getName());

    @Autowired
    private SecurityService securityService;
    
    	@Autowired
	private FawbPropertySourceService fawbPropertySourceService;


    
    public ResponseEntity<Object> callFDODemo(Application application, HttpServletRequest request) {
        logger.info("Starting sample operation with request url " + request.getRequestURL().toString());
        logger.info("Inside callFDODemo");
        logger.info(application.toString());
        for(Party party : application.getParty()){
            logger.info("Inside Party");
            logger.info("Inside Party" + party.getPerson().getPersonalIdNumber() + " " + party.getInProcess());
            if(party.getInProcess().equals("1")){
                logger.info("Inside InProcess");
                logger.info(String.valueOf(party.getBureauReport().length-1));
                party.getBureauReport()[(party.getBureauReport().length-1)].setCreditScore("765");
            } 
        }
       
        HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<Object>(application, responseHeaders, HttpStatus.OK);
    }

}