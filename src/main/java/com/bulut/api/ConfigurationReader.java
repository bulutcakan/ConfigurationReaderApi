package com.bulut.api;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bulut.api.constans.Constants;
import com.bulut.api.jop.CheckAndRefreshParametersJop;
import com.bulut.api.service.AddParams2RedisService;

public class ConfigurationReader {

	@Autowired
	AddParams2RedisService addParams2RedisService;

	public ConfigurationReader(String url, String applicationName, long refreshinterval) throws SchedulerException {
		// TODO Auto-generated constructor stub

		JobDetail job = JobBuilder.newJob(CheckAndRefreshParametersJop.class).withIdentity("refreshJop", "group1")
				.build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("refreshTriger", "group1").withSchedule(
				SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(refreshinterval).repeatForever())
				.build();
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(Constants.HOST, url);
		jobDataMap.put(Constants.APPLİCATION_NAME, applicationName);

		scheduler.start();
		scheduler.scheduleJob(job, trigger);
	}

	public <T> Object getvalue(Class<T> type, String key) {

		return type.cast(addParams2RedisService.getValueFromRedis(key));

	}

}
