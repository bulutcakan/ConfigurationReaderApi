package com.bulut.api.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

@Service

public class RefreshConfigureParameterService {

	@Autowired
	AddParams2RedisService addParams2RedisService;

	private final Scheduler scheduler = null;
	public String applicationName;

	public void connectAndRefreshParam(String host, String applicationName)

	{
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(host);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		List<Map<String, Object>> allRecords = jdbcTemplate.
                queryForList("select * from config_params WHERE isActive=1 and ApplicationName='"+applicationName+"'");		
		this.applicationName = applicationName;
		addParams2RedisService.addValues2RedisCache(allRecords, applicationName);

	}
	
	
}
