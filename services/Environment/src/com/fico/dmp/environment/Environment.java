/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.environment;

import com.fico.dmp.telusagentuidb.FawbPropertySource;
import com.fico.dmp.telusagentuidb.service.FawbPropertySourceService;
// import com.fico.pscomponent.handlers.DMPAuthenticationHandler;
import com.wavemaker.runtime.security.WMUser;
import com.wavemaker.runtime.security.dmp.WMAppDmpAuthenticationToken;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import defaulttermlibrary.InputConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

import com.fico.telus.service.TelusAPIConnectivityService;

//import com.fico.dmp.environment.model.*;

/**
 * This is a singleton class with all its public methods exposed as REST APIs
 * via generated controller class. To avoid exposing an API for a particular
 * public method, annotate it with @HideFromClient.
 * <p>
 * Method names will play a major role in defining the Http Method for the
 * generated APIs. For example, a method name that starts with delete/remove,
 * will make the API exposed as Http Method "DELETE".
 * <p>
 * Method Parameters of type primitives (including java.lang.String) will be
 * exposed as Query Parameters & Complex Types/Objects will become part of the
 * Request body in the generated API.
 */
@ExposeToClient
public class Environment {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory()
            .getLogger(Environment.class.getName());

    @Autowired
    private org.springframework.core.env.Environment environment;

    @Autowired
    private FawbPropertySourceService fawbPropertySourceService;

    @Autowired
    private TelusAPIConnectivityService telusAPIConnectivityService;

    public String whereAmI(HttpServletRequest request) {
        String fromEnvironment = environment.getProperty("DMP_ENV_ID");
        if (StringUtils.isEmpty(fromEnvironment)) {
            fromEnvironment = environment.getProperty("spring.profiles.active");
        }
        return fromEnvironment;
    }

	public String getUserID(){
		WMAppDmpAuthenticationToken authentication = (WMAppDmpAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		WMUser principal = (WMUser) authentication.getPrincipal();
		//logger.info("type: " + authentication.getClass()); //WMAppDmpAuthenticationToken
		return principal.getUserId();
	}

    public String getActiveProperty(String name, HttpServletRequest request) {

        return environment.getProperty(name);
    }

    public String getPropertyValueByName(String name) {
        try {
            FawbPropertySource propertySource = fawbPropertySourceService.getByPropertyKey(name);
            return propertySource.getPropertyValue();
        } catch (Exception e) {
            logger.error("Property Key {} does not exist", name);
        }
        return null;
    }

    public ResponseEntity getInputConfiguration(InputConfiguration config) {

        config.setPrimaryBureau(fawbPropertySourceService.getByPropertyKey("PrimaryBureau").getPropertyValue());
        config.setSecondaryBureau(fawbPropertySourceService.getByPropertyKey("SecondaryBureau").getPropertyValue());
        config.setTertiaryBureau(fawbPropertySourceService.getByPropertyKey("TertiaryBureau").getPropertyValue());
        if (fawbPropertySourceService.getByPropertyKey("PrimaryValid").getPropertyValue().equalsIgnoreCase("true"))
            config.setPrimaryValid(true);
        else
            config.setPrimaryValid(false);

        if (fawbPropertySourceService.getByPropertyKey("SecondaryValid").getPropertyValue().equalsIgnoreCase("true"))
            config.setSecondaryValid(true);
        else
            config.setSecondaryValid(false);

        if (fawbPropertySourceService.getByPropertyKey("TertiaryValid").getPropertyValue().equalsIgnoreCase("true"))
            config.setTertiaryValid(true);
        else
            config.setTertiaryValid(false);

        if (fawbPropertySourceService.getByPropertyKey("BureauValid").getPropertyValue().equalsIgnoreCase("true"))
            config.setBureauValid(true);
        else
            config.setBureauValid(false);

        if (fawbPropertySourceService.getByPropertyKey("SkipDuplicateCheck").getPropertyValue()
                .equalsIgnoreCase("true"))
            config.setSkipDuplicateCheck("true");
        else
            config.setSkipDuplicateCheck("false");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<Object>(config, responseHeaders, HttpStatus.OK);

    }

    /**
     * Setting up the root level log detail, ex INFO,DEBUG,TRACE etc
     *
     * @param level
     * @return
     */
    public String setRootLoggerLevel(String level) {
        Configurator.setRootLevel(Level.toLevel(level));
        return LogManager.getRootLogger().getLevel().toString();

    }

    public String getRootLoggerLevel() {
        return LogManager.getRootLogger().getLevel().toString();

    }

    /**
     * Setting the log level for Class specific
     *
     * @param className
     * @param level
     * @return
     */
    public String setLoggerLevel(String className, String level) {
        try {
            Configurator.setLevel(className, Level.toLevel(level));
        } catch (Exception e) {
            logger.error("Class Not found Exception ", e);
        }
        return LogManager.getRootLogger().getLevel().toString();

    }

    public String getTelusToken(String scope) {
        
        if (scope.isEmpty()){
            
            logger.error("Scope is missing in the request" );
            return "Please provide a valid scope";
        }
        
        try {
            return telusAPIConnectivityService.getTelusToken(scope);
        } catch (Exception e) {
            logger.error("Error in generating token ", e);
            return "Error";
        }
    }

}