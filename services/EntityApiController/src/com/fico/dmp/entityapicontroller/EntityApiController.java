/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.entityapicontroller;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import org.springframework.beans.factory.annotation.Autowired;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import io.swagger.model.CollectionEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

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
public class EntityApiController {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(EntityApiController.class.getName());

    @Autowired
    private SecurityService securityService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * This is sample java operation that accepts an input from the caller and responds with "Hello".
     *
     * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
     * getUserName() and getUserId() etc which returns the information based on the caller context.
     *
     * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
     * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
     */
    public String sampleJavaOperation(String name, HttpServletRequest request) {
        logger.debug("Starting sample operation with request url " + request.getRequestURL().toString());
        String result = null;
        if (securityService.isAuthenticated()) {
            result = "Hello " + name + ", You are logged in as " + securityService.getLoggedInUser().getUserName();
        } else {
            result = "Hello " + name + ", You are not authenticated yet!";
        }
        logger.debug("Returning {}", result);
        return result;
    }
     
    public Object getCollectionEntity(String name, HttpServletRequest request) throws Exception {
            //  return objectMapper.readValue("{\"empty\": false}", CollectionEntity.class);
    return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"agentId\":\"agent1\",\"characteristics\":[{\"id\":1,\"name\":\"string\",\"value\":\"string\",\"@type\":\"Characteristic\"}],\"collectionStatuses\":[{\"id\":1,\"code\":\"PRECOLL\",\"@type\":\"CollectionStatus\"}],\"contacts\":[{\"id\":1,\"href\":\"BASE_URL/contact/1\",\"@referredType\":\"CollectionContact\",\"@type\":\"EntityRef\"}],\"engagedCustomerParty\":{\"cbucid\":\"123\",\"cbuCode\":\"cbuCode123\",\"cbuName\":\"cbu-name-123\",\"organizationType\":\"CBU\",\"@type\":\"Organization\"},\"engagedRegionalCustomerParty\":{\"organizationType\":\"RC\",\"rcid\":\"rc-12345\",\"rcName\":\"rc-name-12345\",\"portfolioCategory\":\"PUBLIC\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"subMarketSegment\":\"string\",\"@type\":\"Organization\"},\"exclusionIndicatorCharacter\":\"string\",\"exclusionIndicatorInteger\":1,\"manualTreatmentIndicator\":false,\"name\":\"string\",\"notTouchListIndicator\":false,\"relatedEntity\":{\"id\":1,\"role\":\"RCID\",\"@type\":\"RelatedEntity\"},\"workCategory\":\"string\",\"@type\":\"CollectionEntity\"}]", objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionEntity.class));
    
    }

}