/**
 *This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.
 */
package com.fico.dmp.agentinfoservice.controller;

import com.fico.dmp.agentinfoservice.AgentInfoService;
import com.fico.telus.model.AgentInfo;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;

@RestController
@RequestMapping(value = "/agentInfo")
public class AgentInfoController {

    @Autowired
    private AgentInfoService agentInfoService;

    @RequestMapping(value = "/agent", method = RequestMethod.GET)
    public List<AgentInfo> getAgent() {
        return agentInfoService.getAgent();
    }
}
