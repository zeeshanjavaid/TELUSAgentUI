/**
 *This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.
 */
package com.fico.dmp.parrreportservice.controller;

import com.fico.dmp.parrreportservice.ParrReportService;
import java.lang.String;
import java.lang.Integer;
import java.lang.Exception;
import com.fico.telus.model.ParrReports;
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
@RequestMapping(value = "/parrReport")
public class ParrReportController {

    @Autowired
    private ParrReportService parrReportService;

    @RequestMapping(value = "/parrReport", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public List<ParrReports> getParrReport(@RequestParam(value = "fields", required = false) String fields, @RequestParam(value = "offset", required = false) Integer offset, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "agentId", required = false) String agentId, @RequestParam(value = "entityId", required = false) String entityId, @RequestParam(value = "entityRisk", required = false) String entityRisk, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "createdFrom", required = false) String createdFrom, @RequestParam(value = "createdTo", required = false) String createdTo, @RequestParam(value = "evaluation", required = false) String evaluation, @RequestParam(value = "createdBy", required = false) String createdBy, @RequestParam(value = "createdTeam", required = false) String createdTeam) throws Exception {
        return parrReportService.getParrReport(fields, offset, limit, agentId, entityId, entityRisk, status, createdFrom, createdTo, evaluation, createdBy, createdTeam);
    }
}
