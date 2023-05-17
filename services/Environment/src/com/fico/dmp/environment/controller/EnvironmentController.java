/**
 *This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.
 */
package com.fico.dmp.environment.controller;

import com.fico.dmp.environment.Environment;
import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import defaulttermlibrary.InputConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;

@RestController
@RequestMapping(value = "/environment")
public class EnvironmentController {

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/activeProperty", produces = "application/json", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String getActiveProperty(@RequestParam(value = "name", required = false) String name, HttpServletRequest request) {
        return environment.getActiveProperty(name, request);
    }

    @RequestMapping(value = "/inputConfiguration", method = RequestMethod.POST)
    public ResponseEntity getInputConfiguration(@RequestBody InputConfiguration config) {
        return environment.getInputConfiguration(config);
    }

    @RequestMapping(value = "/propertyValueByName", produces = "application/json", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String getPropertyValueByName(@RequestParam(value = "name", required = false) String name) {
        return environment.getPropertyValueByName(name);
    }

    @RequestMapping(value = "/rootLoggerLevel", produces = "application/json", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String getRootLoggerLevel() {
        return environment.getRootLoggerLevel();
    }

    @RequestMapping(value = "/telustoken", produces = "application/json", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String getTelusToken(@RequestParam(value = "scope", required = false) String scope) {
        return environment.getTelusToken(scope);
    }

    @RequestMapping(value = "/userID", produces = "application/json", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String getUserID() {
        return environment.getUserID();
    }

    @RequestMapping(value = "/loggerlevel", produces = "application/json", method = RequestMethod.PUT)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String setLoggerLevel(@RequestParam(value = "className", required = false) String className, @RequestParam(value = "level", required = false) String level) {
        return environment.setLoggerLevel(className, level);
    }

    @RequestMapping(value = "/rootLoggerLevel", produces = "application/json", method = RequestMethod.PUT)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String setRootLoggerLevel(@RequestParam(value = "level", required = false) String level) {
        return environment.setRootLoggerLevel(level);
    }

    @RequestMapping(value = "/whereAmI", produces = "application/json", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String whereAmI(HttpServletRequest request) {
        return environment.whereAmI(request);
    }
}
