/**
 *This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.
 */
package com.fico.dmp.commonutilityservice.controller;

import com.fico.dmp.commonutilityservice.CommonUtilityService;
import com.fico.telus.model.AssignedUserModel;
import java.util.List;
import java.lang.Integer;
import java.lang.String;
import com.fico.telus.model.AssignedTeamModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;

@RestController
@RequestMapping(value = "/commonUtility")
public class CommonUtilityController {

    @Autowired
    private CommonUtilityService commonUtilityService;

    @RequestMapping(value = "/assignedPersonInactionManagement", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public List<AssignedUserModel> getAssignedPersonInActionManagement() {
        return commonUtilityService.getAssignedPersonInActionManagement();
    }

    @RequestMapping(value = "/loggedInUserTeamId", produces = "application/json", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String getLoggedInUserTeamId(@RequestParam(value = "userId", required = false) Integer userId) {
        return commonUtilityService.getLoggedInUserTeamId(userId);
    }

    @RequestMapping(value = "/teamListInActionManagement", method = RequestMethod.GET)
    public List<AssignedTeamModel> getTeamListInActionManagement() {
        return commonUtilityService.getTeamListInActionManagement();
    }
}
