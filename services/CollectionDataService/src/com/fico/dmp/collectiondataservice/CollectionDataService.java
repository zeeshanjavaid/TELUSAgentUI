/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectiondataservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;

import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;

//import com.fico.dmp.collectiondataservice.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import com.wordnik.swagger.annotations.ApiOperation;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.client.model.EntityBanDetailsResponse;

import java.util.List;

import com.fico.core.util.ObjectMapperConfig;


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
public class CollectionDataService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionDataService.class.getName());

    @Autowired
    private SecurityService securityService;
    
    private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();


    /**
     * This is sample java operation that accepts an input from the caller and responds with "Hello".
     *
     * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
     * getUserName() and getUserId() etc which returns the information based on the caller context.
     *
     * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
     * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
     */
     @RequestMapping(value = "/entityBanDetails", method = {RequestMethod.GET})
    public List<EntityBanDetailsResponse> getEntityBanDetails(String entityId) throws Exception  {


        return objectMapper.readValue("[{\"entityId\":6766677,\"banId\":2244343,\"banStatus\":\"In Collection\",\"banName\":\"Air Canada\",\"banArAmount\":234,\"banOverdueAmount\":234,\"suppresionFlag\":true,\"disputeFlag\":false},{\"entityId\":6766677,\"banId\":2244344,\"banStatus\":\"In Collection\",\"banName\":\"Air Canada2\",\"banArAmount\":22,\"banOverdueAmount\":10,\"suppresionFlag\":true,\"disputeFlag\":false}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, EntityBanDetailsResponse.class));
        // return new Object(); 
    }


}