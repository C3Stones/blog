package com.c3stones.job.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 调度器配置类
 * 
 * @author CL
 *
 */
@Configuration
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer {

	@Autowired
	private DataSource dataSource;

	@Override
	public void customize(SchedulerFactoryBean schedulerFactoryBean) {
		// 启动延时
		schedulerFactoryBean.setStartupDelay(10);
		// 自动启动任务调度
		schedulerFactoryBean.setAutoStartup(true);
		// 是否覆盖现有作业定义
		schedulerFactoryBean.setOverwriteExistingJobs(true);
		// 配置数据源
		schedulerFactoryBean.setDataSource(dataSource);
	}

}