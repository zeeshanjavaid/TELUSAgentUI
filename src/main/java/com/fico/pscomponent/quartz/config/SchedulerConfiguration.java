package com.fico.pscomponent.quartz.config;

import java.util.Properties;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class SchedulerConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerConfiguration.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	@Qualifier("TELUSAgentUIDBWMManagedDataSource")
	private HikariDataSource telusAgentUIDBWMManagedDataSource;

 	@Bean
 	public Scheduler scheduler() throws Exception {
 		SchedulerFactoryBean factory = new SchedulerFactoryBean();
 		factory.setApplicationContext(applicationContext);
 		factory.setQuartzProperties(quartzProperties());
 		factory.setDataSource(telusAgentUIDBWMManagedDataSource);
 		factory.setJobFactory(springBeanJobFactory());
 		factory.afterPropertiesSet();

 		Scheduler scheduler = factory.getScheduler();
 		return scheduler;
 	}

 //	Spring context aware job factory
 	@Bean
 	public SpringBeanJobFactory springBeanJobFactory() {
 		return new AutowiringSpringBeanJobFactory();
 	}

 	@Bean
 	public Properties quartzProperties() throws Exception {
 		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
 		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
 		propertiesFactoryBean.afterPropertiesSet();
 		Properties properties = propertiesFactoryBean.getObject();
 		logger.info("Properties set are {}", properties);
 		return properties;
 	}
}
