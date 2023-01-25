/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.pscomponent.timerservice;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fico.pscomponent.quartz.exception.JobDoesNotExistException;
import com.fico.pscomponent.quartz.model.JobDTO;
import com.fico.pscomponent.quartz.model.JobInfo;
import com.fico.pscomponent.quartz.model.JobResponse;
import com.fico.pscomponent.quartz.service.JobService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;

@ExposeToClient
public class TimerService {

    private static final Logger logger = LoggerFactory.getLogger(TimerService.class);

    @Autowired
	private JobService jobService;
   
    public ResponseEntity<String> scheduleJob(JobDTO jobDTO) {
        if(logger.isInfoEnabled())
    	    logger.info("Schedule Job service invoked");
        JobResponse jobResponse = jobService.scheduleJob(jobDTO);
        return createResponse(jobResponse);
    }

    public ResponseEntity<String> updateScheduledJob(JobDTO jobDTO) {
        if(logger.isInfoEnabled())
            logger.info("Re-schedule job service invoked");
        JobResponse jobResponse = jobService.updateScheduledJob(jobDTO);
        return createResponse(jobResponse);
    }

    public ResponseEntity<String> deactivateJob(String name) {
        JobResponse jobResponse = jobService.pauseJob(name);
        return createResponse(jobResponse);
    }
    
    public ResponseEntity<String> activateJob(String name) {
        JobResponse jobResponse = jobService.resumeJob(name);
        return createResponse(jobResponse);
    }
    
    public ResponseEntity<String> runNow(String name) {
    	JobResponse jobResponse = jobService.runJobNow(name);
        return createResponse(jobResponse);
    }

    public ResponseEntity<String> deleteJob(String jobName) {
    	JobResponse jobResponse = jobService.deleteJob(jobName);
    	return createResponse(jobResponse);
    }

    public List<JobInfo> getAllJobsInfo() throws SchedulerException {
    	List<JobInfo> jobInfoDetails = jobService.getAllJobDetails();
		return jobInfoDetails;
    }

    public List<String> getJobTypes() {
    	return jobService.getJobTypes();
    }

    public ResponseEntity<JobInfo> getJobInfo(String jobName) {
    	try {
			JobInfo jobInfo = jobService.getJobInfo(jobName);
			return new ResponseEntity<JobInfo>(jobInfo, HttpStatus.OK);
		} catch (SchedulerException e) {
			return new ResponseEntity<JobInfo>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JobDoesNotExistException e) {
			return new ResponseEntity<JobInfo>(HttpStatus.BAD_REQUEST);
		}
    }

    private ResponseEntity<String> createResponse(JobResponse jobResponse) {
    	if (jobResponse.getSuccess()) {
    		 return new ResponseEntity<String>(jobResponse.getMessage(), HttpStatus.OK);
    	} else if (jobResponse.getClientError()){
    		return new ResponseEntity<String>(jobResponse.getMessage(), HttpStatus.BAD_REQUEST); 
    	} else {
    		return new ResponseEntity<String>(jobResponse.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
}
