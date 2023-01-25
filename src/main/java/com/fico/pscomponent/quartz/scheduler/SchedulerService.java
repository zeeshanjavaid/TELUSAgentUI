package com.fico.pscomponent.quartz.scheduler;

import static com.fico.pscomponent.quartz.constant.JobDataMapConstant.RUN_NOW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.fico.pscomponent.quartz.model.JobInfo;

@Service
public class SchedulerService {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

	@Autowired
	private Scheduler scheduler;

	private static boolean isContextRefreshedOnce = false;

	@EventListener
	@Order(Integer.MAX_VALUE - 1)
	public void onContextRefresh(org.springframework.context.event.ContextRefreshedEvent e) {
		if (!isContextRefreshedOnce) {
			try {
				if (!scheduler.isStarted()) {
					scheduler.startDelayed(30);
				}
				isContextRefreshedOnce = true;
			} catch (SchedulerException ex) {
				logger.error("Exception while initializing scheduler service", ex);
			}
		}
	}

	public void scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		scheduler.scheduleJob(jobDetail, trigger);
	}

	public JobDetail getJobDetail(String name) throws SchedulerException {
		return scheduler.getJobDetail(new JobKey(name));
	}

	public Trigger getTriggersOfAJob(String jobName) throws SchedulerException {
		List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(new JobKey(jobName));
		return triggersOfJob.get(0);
	}

	public void rescheduleJob(TriggerKey key, Trigger trigger) throws SchedulerException {
		scheduler.rescheduleJob(key, trigger);
	}

	public boolean isJobExists(String jobName) throws SchedulerException {
		return scheduler.checkExists(new JobKey(jobName));
	}

	public boolean isTriggerExists(String triggerName) throws SchedulerException {
		return scheduler.checkExists(new TriggerKey(triggerName));
	}

	public void deleteJob(String jobName) throws SchedulerException {
		scheduler.deleteJob(new JobKey(jobName));
	}

	public void pauseTrigger(String triggerName) throws SchedulerException {
		scheduler.pauseTrigger(new TriggerKey(triggerName));
	}

	public void resumeTrigger(String triggerName) throws SchedulerException {
		scheduler.resumeTrigger(new TriggerKey(triggerName));
	}

	public void runNow(String name) throws SchedulerException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(RUN_NOW, "true");
		scheduler.triggerJob(new JobKey(name), jobDataMap);
	}

	public JobInfo getJobInfo(String name) throws SchedulerException {
		return createJobInfo(new JobKey(name));
	}

	public List<JobInfo> getAllJobDetails() throws SchedulerException {
	    logger.info("In getAllJobDetails before object assigning :::::::::::::::::::::");
		List<JobInfo> jobInfoList = new ArrayList<JobInfo>();
		for (String groupName : scheduler.getJobGroupNames()) {
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
				JobInfo jobInfo = createJobInfo(jobKey);
				if (!Objects.isNull(jobInfo)) {
					jobInfoList.add(jobInfo);
				}
			}
		}
		return jobInfoList;
	}

	private JobInfo createJobInfo(JobKey jobKey) throws SchedulerException {
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		String jobType = jobDetail.getJobClass().getSimpleName();
		List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
		Boolean isPaused = false;
		if (!triggersOfJob.isEmpty()) {
			if (triggersOfJob.get(0) instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) triggersOfJob.get(0);
				TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());

				if (triggerState.equals(TriggerState.PAUSED)) {
					isPaused = true;
				}

				String cronExpression = cronTrigger.getCronExpression();
				return createJobInfo(jobType, jobKey.getName(), cronTrigger.getDescription(), true, cronExpression,
						false, null, isPaused);
			} else if (triggersOfJob.get(0) instanceof SimpleTrigger) {
				SimpleTrigger simpleTrigger = (SimpleTrigger) triggersOfJob.get(0);

				TriggerState triggerState = scheduler.getTriggerState(simpleTrigger.getKey());

				if (triggerState.equals(TriggerState.PAUSED)) {
					isPaused = true;
				}

				Long minutes = TimeUnit.MILLISECONDS.toMinutes(simpleTrigger.getRepeatInterval());
				return createJobInfo(jobType, jobKey.getName(), simpleTrigger.getDescription(), false, null, true,
						minutes.intValue(), isPaused);
			}
		}
		return null;
	}

	private JobInfo createJobInfo(String jobType, String jobName, String description, Boolean isCron,
			String cronExpression, Boolean isFrequency, Integer frequency, Boolean isPaused) {
		JobInfo jobInfo = new JobInfo();
		jobInfo.setJobType(jobType);
		jobInfo.setJobName(jobName);
		jobInfo.setDescription(description);
		jobInfo.setIsCron(isCron);
		jobInfo.setCronExpression(cronExpression);
		jobInfo.setIsFrequency(isFrequency);
		jobInfo.setFrequency(frequency);
		jobInfo.setIsPaused(isPaused);
		return jobInfo;
	}

	@PreDestroy
	public void shutDown() {
		if (!Objects.isNull(scheduler)) {
			try {
				scheduler.shutdown(true);
			} catch (SchedulerException e) {
				logger.error("Scheduler shutdown failed with error ", e);
				try {
					if (!scheduler.isShutdown()) {
						scheduler.shutdown(false);
					}
				} catch (SchedulerException e1) {
					logger.error("Scheduler shutdown failed in retry ", e1);
				}
			}
		}
	}
}
