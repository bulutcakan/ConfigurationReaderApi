package com.bulut.api.jop;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.bulut.api.constans.Constants;
import com.bulut.api.service.RefreshConfigureParameterService;

public class CheckAndRefreshParametersJop implements Job {

	@Autowired
	RefreshConfigureParameterService refreshConfigureParmeterService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		JobDataMap map = arg0.getMergedJobDataMap();
		connectRedisAndRefreshParam(map);

	}

	@SuppressWarnings("unchecked")
	private void connectRedisAndRefreshParam(JobDataMap map) {
		String applicationName = map.getString(Constants.APPLİCATION_NAME);
		String host = map.getString(Constants.HOST);
		refreshConfigureParmeterService.connectAndRefreshParam(host,applicationName);

	}

}
