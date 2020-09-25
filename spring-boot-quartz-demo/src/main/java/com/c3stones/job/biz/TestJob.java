package com.c3stones.job.biz;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.c3stones.job.service.JobService;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试定时任务
 * 
 * @author CL
 *
 */
@Slf4j
// @DisallowConcurrentExecution //不并发执行
public class TestJob implements Job {

	@Autowired
	private JobService jobService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		log.info("定时任务1 => 定时任务定时任务数量 => {}，参数值 => {}，当前时间 => {}", jobService.count(),
				"{ username=" + jobDataMap.get("username") + ", age=" + jobDataMap.get("age") + " }",
				LocalDateTime.now());
	}

}
