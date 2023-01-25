package com.fico.pscomponent.quartz.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.fico.pscomponent.quartz.exception.JobDoesNotExistException;
import com.fico.pscomponent.quartz.model.JobDTO;
import com.fico.pscomponent.quartz.model.JobInfo;
import com.fico.pscomponent.quartz.model.JobResponse;

public interface JobService {
    JobResponse scheduleJob(JobDTO jobDTO);
    JobResponse updateScheduledJob(JobDTO jobDTO);
    JobResponse deleteJob(String jobName);
    JobResponse pauseJob(String name);
    JobResponse resumeJob(String name);
    JobResponse runJobNow(String name);
    JobInfo getJobInfo(String jobName) throws SchedulerException, JobDoesNotExistException;
    List<JobInfo> getAllJobDetails() throws SchedulerException;
    List<String> getJobTypes();
}
