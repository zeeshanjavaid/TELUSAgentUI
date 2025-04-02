/**
 *This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.
 */
package com.fico.dmp.pscomponent.timerservice.controller;

import com.fico.dmp.pscomponent.timerservice.TimerService;
import java.lang.String;
import org.springframework.http.ResponseEntity;
import org.quartz.SchedulerException;
import com.fico.pscomponent.quartz.model.JobInfo;
import java.util.List;
import com.fico.pscomponent.quartz.model.JobDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;

@RestController
@RequestMapping(value = "/timer")
public class TimerController {

    @Autowired
    private TimerService timerService;

    @RequestMapping(value = "/activateJob", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public ResponseEntity<String> activateJob(@RequestParam(value = "name", required = false) String name) {
        return timerService.activateJob(name);
    }

    @RequestMapping(value = "/deactivateJob", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public ResponseEntity<String> deactivateJob(@RequestParam(value = "name", required = false) String name) {
        return timerService.deactivateJob(name);
    }

    @RequestMapping(value = "/job", method = RequestMethod.DELETE)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public ResponseEntity<String> deleteJob(@RequestParam(value = "jobName", required = false) String jobName) {
        return timerService.deleteJob(jobName);
    }

    @RequestMapping(value = "/allJobsInfo", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public List<JobInfo> getAllJobsInfo() throws SchedulerException {
        return timerService.getAllJobsInfo();
    }

    @RequestMapping(value = "/currentUtcTime", produces = "application/json", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public String getCurrentUtcTime() {
        return timerService.getCurrentUtcTime();
    }

    @RequestMapping(value = "/jobInfo", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public ResponseEntity<JobInfo> getJobInfo(@RequestParam(value = "jobName", required = false) String jobName) {
        return timerService.getJobInfo(jobName);
    }

    @RequestMapping(value = "/jobTypes", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public List<String> getJobTypes() {
        return timerService.getJobTypes();
    }

    @RequestMapping(value = "/runNow", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "")
    public ResponseEntity<String> runNow(@RequestParam(value = "name", required = false) String name) {
        return timerService.runNow(name);
    }

    @RequestMapping(value = "/scheduleJob", method = RequestMethod.POST)
    public ResponseEntity<String> scheduleJob(@RequestBody JobDTO jobDTO) {
        return timerService.scheduleJob(jobDTO);
    }

    @RequestMapping(value = "/scheduledJob", method = RequestMethod.PUT)
    public ResponseEntity<String> updateScheduledJob(@RequestBody JobDTO jobDTO) {
        return timerService.updateScheduledJob(jobDTO);
    }
}
