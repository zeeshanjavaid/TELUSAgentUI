package com.fico.pscomponent.quartz.service;

import static org.quartz.JobBuilder.newJob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.BooleanUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fico.pscomponent.quartz.exception.JobAlreadyExistException;
import com.fico.pscomponent.quartz.exception.JobDoesNotExistException;
import com.fico.pscomponent.quartz.exception.TriggerDoesNotExistException;
import com.fico.pscomponent.quartz.model.JobDTO;
import com.fico.pscomponent.quartz.model.JobInfo;
import com.fico.pscomponent.quartz.model.JobResponse;
import com.fico.pscomponent.quartz.scheduler.SchedulerService;

@Service
public class JobServiceImpl implements JobService {

	private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

//	private JobDTO jobDTO;

	@Autowired
	private SchedulerService schedularService;

	@Autowired
	private JobValidationService jobValidationService;

// 	@PostConstruct
// 	public void initialize() {
// 		this.jobDTO = new JobDTO();
// 		this.jobDTO.setIsCron(true);
// 		this.jobDTO.setCronExpression("0 0 0 1/1 * ? *"); // run every day mid night at 12am
// 		this.jobDTO.setJobName("ExtractTableToCsv");
// 		this.jobDTO.setJobType("ExtractTableToCsvJob");
// 		this.jobDTO.setDescription("Extract activity log to S3 bucket everyday at 12am");

// 		try {
// 			if (!schedularService.isJobExists(this.jobDTO.getJobName())) {
// 				logger.info("Scheduler job {} does not exists", this.jobDTO.getJobName());
// 				scheduleJob(this.jobDTO);
// 			} else {
// 				logger.info("Scheduler Job {} already exists", this.jobDTO.getJobName());
// 			}
// 		} catch (Exception ex) {
// 			logger.error("Exception while creating default job", ex);
// 		}
// 	}

	@Override
	public JobResponse scheduleJob(JobDTO jobDTO) {
		JobResponse jobResponse = new JobResponse();
		try {
			jobValidationService.validateJobBody(jobDTO);
			if (schedularService.isJobExists(jobDTO.getJobName())) {
				throwJobAlreadyExistException(jobDTO.getJobName());
			}
		} catch (SchedulerException e) {
			jobResponse.setClientError(false);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		} catch (Exception e) {
			jobResponse.setClientError(true);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		}
		JobDetail jobDetail;
		try {
			jobDetail = createJobDetail(jobDTO);
		} catch (ClassNotFoundException e) {
			jobResponse.setClientError(false);
			jobResponse.setSuccess(false);
			jobResponse.setMessage("Job Service is not created for Job Type -" + jobDTO.getJobType());
			return jobResponse;
		}
		Trigger trigger = createTrigger(jobDTO);
		try {
			schedularService.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			jobResponse.setClientError(false);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		}
		logger.info("-----Job Scheduled Successfully------");
		jobResponse.setSuccess(true);
		jobResponse.setMessage("Job Scheduled Successfully");
		return jobResponse;
	}

	@Override
	public JobResponse updateScheduledJob(JobDTO jobDTO) {
		JobResponse jobResponse = new JobResponse();
		try {
			jobValidationService.validateJobBody(jobDTO);
		} catch (Exception e) {
			jobResponse.setClientError(true);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		}

		try {
			if (schedularService.isJobExists(jobDTO.getJobName())) {
				Trigger trigger = schedularService.getTriggersOfAJob(jobDTO.getJobName());
				if (trigger instanceof CronTrigger && BooleanUtils.isTrue(jobDTO.getIsCron())) {
					if (!((CronTrigger) trigger).getCronExpression().equalsIgnoreCase(jobDTO.getCronExpression())) {
						schedularService.rescheduleJob(trigger.getKey(), createCronTrigger(jobDTO.getJobName(),
								jobDTO.getCronExpression(), jobDTO.getDescription()));
					} else {
						jobResponse.setSuccess(true);
						jobResponse.setMessage("There are no changes to update");
						return jobResponse;
					}
				} else if (trigger instanceof SimpleTrigger && BooleanUtils.isTrue(jobDTO.getIsFrequency())) {
					SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
					Long minutes = TimeUnit.MILLISECONDS.toMinutes(simpleTrigger.getRepeatInterval());
					if (minutes.intValue() != jobDTO.getFrequency()) {
						schedularService.rescheduleJob(trigger.getKey(), createSimpleTrigger(jobDTO.getJobName(),
								jobDTO.getFrequency(), jobDTO.getDescription()));
					} else {
						jobResponse.setSuccess(true);
						jobResponse.setMessage("There is no value changes");
						return jobResponse;
					}
				} else {
					jobResponse.setClientError(true);
					jobResponse.setSuccess(false);
					jobResponse.setMessage("Existing Trigger and updating information is not of same type");
					return jobResponse;
				}
				logger.info("Job {} Rescheduled Successfully", jobDTO.getJobName());
			} else {
				throwJobDoesNotExistException(jobDTO.getJobName());
			}
		} catch (SchedulerException e) {
			jobResponse.setClientError(false);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		} catch (Exception e) {
			jobResponse.setClientError(true);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		}

		jobResponse.setSuccess(true);
		jobResponse.setMessage("Cron Expression updated successfully");
		return jobResponse;
	}

	@Override
	public JobResponse deleteJob(String jobName) {
		JobResponse jobResponse = new JobResponse();
		try {
			if (schedularService.isJobExists(jobName)) {
				schedularService.deleteJob(jobName);
				logger.info("Job {} deleted Successfully", jobName);
			} else {
				throwJobDoesNotExistException(jobName);
			}
		} catch (JobDoesNotExistException e) {
			jobResponse.setClientError(true);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		} catch (SchedulerException e) {
			jobResponse.setClientError(false);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		}

		jobResponse.setSuccess(true);
		jobResponse.setMessage("Job " + jobName + " deleted successfully");
		return jobResponse;
	}

	@Override
	public JobResponse pauseJob(String jobName) {
		JobResponse jobResponse = new JobResponse();
		logger.info("Pause Trigger initiated");
		try {
			if (isTriggerExists(jobName)) {
				logger.info("Trigger exist with name {}", jobName);
				schedularService.pauseTrigger(jobName);
			} else {
				throwTriggerDoesNotExistException(jobName);
			}
		} catch (TriggerDoesNotExistException e) {
			jobResponse.setClientError(true);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		} catch (SchedulerException e) {
			jobResponse.setClientError(false);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		}

		jobResponse.setSuccess(true);
		jobResponse.setMessage("Job " + jobName + " deactivated successfully");
		return jobResponse;
	}

	@Override
	public JobResponse resumeJob(String jobName) {
		JobResponse jobResponse = new JobResponse();
		logger.info("Resume Trigger initiated");
		try {
			if (isTriggerExists(jobName)) {
				logger.info("Trigger exist with name {}", jobName);
				schedularService.resumeTrigger(jobName);
			} else {
				throwTriggerDoesNotExistException(jobName);
			}
		} catch (TriggerDoesNotExistException e) {
			jobResponse.setClientError(true);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		} catch (SchedulerException e) {
			jobResponse.setClientError(false);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		}

		jobResponse.setSuccess(true);
		jobResponse.setMessage("Job " + jobName + " activated successfully");
		return jobResponse;
	}

	@Override
	public JobResponse runJobNow(String jobName) {
		JobResponse jobResponse = new JobResponse();
		logger.info("Run Now initiated");
		try {
			if (isTriggerExists(jobName)) {
				logger.info("Job exist with name {}", jobName);
				schedularService.runNow(jobName);
				logger.info("Run Now successful");
			} else {
				throwTriggerDoesNotExistException(jobName);
			}
		} catch (TriggerDoesNotExistException e) {
			jobResponse.setClientError(true);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		} catch (SchedulerException e) {
			jobResponse.setClientError(false);
			jobResponse.setSuccess(false);
			jobResponse.setMessage(e.getMessage());
			return jobResponse;
		}

		jobResponse.setSuccess(true);
		jobResponse.setMessage("Job " + jobName + " has run successfully");
		return jobResponse;
	}

	@Override
	public JobInfo getJobInfo(String jobName) throws SchedulerException, JobDoesNotExistException {
		if (schedularService.isJobExists(jobName)) {
			return schedularService.getJobInfo(jobName);
		}
		throwJobDoesNotExistException(jobName);
		return null;
	}

	@Override
	public List<JobInfo> getAllJobDetails() throws SchedulerException {
		return schedularService.getAllJobDetails();
	}

	@Override
	public List<String> getJobTypes() {
		List<String> jobTypeList = new ArrayList<String>();
		Reflections reflection = new Reflections("com.fico.pscomponent.quartz.job");
		Set<Class<? extends Job>> subTypesOf = reflection.getSubTypesOf(Job.class);
		subTypesOf.forEach(type -> jobTypeList.add(type.getSimpleName()));
		return jobTypeList;
	}

	private JobDetail createJobDetail(JobDTO jobDTO) throws ClassNotFoundException {
		JobDataMap jobDataMap = new JobDataMap();
		Class<?> classType = Class.forName("com.fico.pscomponent.quartz.job." + jobDTO.getJobType());
		return newJob((Class<? extends Job>) classType).withIdentity(jobDTO.getJobName()).setJobData(jobDataMap)
				.withDescription(jobDTO.getDescription()).build();
	}

	private Trigger createSimpleTrigger(String jobName, String description) {
		return TriggerBuilder.newTrigger().withIdentity(jobName).withDescription(description)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
	}

	private Trigger createCronTrigger(String jobName, String cronExpression, String description) {
		return TriggerBuilder.newTrigger().withIdentity(jobName).withDescription(description).startAt(new Date())
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
	}

	private Trigger createSimpleTrigger(String jobName, int frequency, String description) {
		return TriggerBuilder.newTrigger().withIdentity(jobName).withDescription(description)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(frequency).repeatForever())
				.build();
	}

	private Trigger createTrigger(JobDTO jobDTO) {
		if (BooleanUtils.isTrue(jobDTO.getIsCron())) {
			return createCronTrigger(jobDTO.getJobName(), jobDTO.getCronExpression(), jobDTO.getDescription());
		} else if (BooleanUtils.isTrue(jobDTO.getIsFrequency())) {
			return createSimpleTrigger(jobDTO.getJobName(), jobDTO.getFrequency(), jobDTO.getDescription());
		} else {
			return createSimpleTrigger(jobDTO.getJobName(), jobDTO.getDescription());
		}
	}

	public boolean isTriggerExists(String triggerName) throws SchedulerException {
		return schedularService.isTriggerExists(triggerName);
	}

	private void throwJobDoesNotExistException(String jobName) throws JobDoesNotExistException {
		logger.info("Job {} does not exist", jobName);
		throw new JobDoesNotExistException("Job " + jobName + " does not exist");
	}

	private void throwJobAlreadyExistException(String jobName) throws JobAlreadyExistException {
		logger.info("Job {} already exist", jobName);
		throw new JobAlreadyExistException("Job " + jobName + " already exist");
	}

	private void throwTriggerDoesNotExistException(String jobName) throws TriggerDoesNotExistException {
		logger.info("Trigger {} does not exist", jobName);
		throw new TriggerDoesNotExistException("Job " + jobName + " does not exist");
	}
}
