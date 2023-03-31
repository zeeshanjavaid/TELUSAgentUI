/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectiontreatmentservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;


import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.wordnik.swagger.annotations.ApiOperation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.model.CollectionTreatment;



import java.util.List;

import com.fico.core.util.ObjectMapperConfig;
//import com.fico.dmp.collectiontreatmentservice.model.*;

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
public class CollectionTreatmentService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionTreatmentService.class.getName());

    @Autowired
    private SecurityService securityService;

    private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();

//collectionTreatment

//List or find CollectionTreatment entries
//   @RequestMapping(value = "/collectionTreatment", method = {RequestMethod.GET})
//     public CollectionTreatment getCollectionTreatment(@RequestParam(required = true)String inputType, @RequestParam(required = true)String inputValue, @RequestParam(required = true)String level, @RequestParam(required = true)String searchMatchCriteria, @RequestParam(required = true)String billingSystem, Integer offset, Integer limit) throws Exception  {

//         return objectMapper.readValue("[{\"banId\":224434,\"banName\":\"Air Canada Toronto\",\"billingSystem\":\"CES\",\"cbucId\":1323232,\"dntlFlag\":true,\"entityId\":67666,\"entityName\":\"Air Canada Ontario\",\"entityOwner\":\"Agent123\",\"entityType\":\"RCID\",\"rcId\":323223},{\"banId\":44343,\"banName\":\"Rexdale Pharmacy\",\"billingSystem\":\"CES\",\"cbucId\":3232232,\"dntlFlag\":false,\"entityId\":676667,\"entityName\":\"Rexdale\",\"entityOwner\":\"Agent345\",\"entityType\":\"RCID\",\"rcId\":224232}]",CollectionTreatment.class);
//         // return new Object(); 
//     }



}