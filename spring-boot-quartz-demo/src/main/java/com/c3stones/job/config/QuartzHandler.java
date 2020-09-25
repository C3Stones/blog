package com.c3stones.job.config;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.c3stones.job.entity.Job;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务管理器
 * 
 * @author CL
 *
 */
@Slf4j
@Component
public class QuartzHandler {

	@Autowired
	private Scheduler scheduler;

	/**
	 * 新增定义任务
	 * 
	 * @param job   定义任务
	 * @param clazz 任务执行类
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean start(Job job, Class clazz) {
		boolean result = true;
		try {
			String jobName = job.getJobName();
			String jobGroup = job.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null == cronTrigger) {
				// 处理参数
				Map<String, String> map = new HashMap<>(5);
				String jobDataMap = job.getJobDataMap();
				if (StrUtil.isNotBlank(jobDataMap)) {
					if (JSONUtil.isJson(jobDataMap)) {
						Map parseMap = JSONUtil.toBean(jobDataMap, Map.class);
						parseMap.forEach((k, v) -> {
							map.put(String.valueOf(k), String.valueOf(v));
						});
					}
				}
				// 启动定时任务
				JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, jobGroup)
						.setJobData(new JobDataMap(map)).build();
				cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
						.withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).build();
				scheduler.scheduleJob(jobDetail, cronTrigger);
				if (!scheduler.isShutdown()) {
					scheduler.start();
				}
			} else {
				// 重启定时任务
				cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey)
						.withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).build();
				scheduler.rescheduleJob(triggerKey, cronTrigger);
			}
		} catch (SchedulerException e) {
			log.info("新增定时任务异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param job 定时任务
	 * @return
	 */
	public boolean pasue(Job job) {
		boolean result = true;
		try {
			String jobName = job.getJobName();
			String jobGroup = job.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			Trigger trigger = scheduler.getTrigger(triggerKey);
			JobKey jobKey = trigger.getJobKey();
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			log.info("暂停定时任务异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 重启定时任务
	 * 
	 * @param job 定时任务
	 * @return
	 */
	public boolean restart(Job job) {
		boolean result = true;
		try {
			String jobName = job.getJobName();
			String jobGroup = job.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			Trigger trigger = scheduler.getTrigger(triggerKey);
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			log.info("重启定时任务异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 立即执行一次
	 * 
	 * @param job 定时任务
	 * @return
	 */
	public boolean trigger(Job job) {
		boolean result = true;
		try {
			String jobName = job.getJobName();
			String jobGroup = job.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			Trigger trigger = scheduler.getTrigger(triggerKey);
			JobKey jobKey = trigger.getJobKey();
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			log.info("立即执行一次异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 修改触发时间表达式
	 * 
	 * @param job               定时任务
	 * @param newCronExpression 新的cron表达式
	 * @return
	 */
	public boolean updateCronExpression(Job job, String newCronExpression) {
		boolean result = true;
		try {
			String jobName = job.getJobName();
			String jobGroup = job.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			job.setCronExpression(newCronExpression);
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder)
					.build();
			scheduler.rescheduleJob(triggerKey, cronTrigger);
		} catch (SchedulerException e) {
			log.info("修改触发时间表达式异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 删除定时任务
	 * 
	 * @param job 定时任务
	 * @return
	 */
	public boolean delete(Job job) {
		boolean result = true;
		try {
			String jobName = job.getJobName();
			String jobGroup = job.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			Trigger trigger = scheduler.getTrigger(triggerKey);
			JobKey jobKey = trigger.getJobKey();
			// 停止触发器
			scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			scheduler.unscheduleJob(triggerKey);
			// 删除任务
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			log.info("删除定时任务异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/***
	 * 判断是否存在定时任务
	 * 
	 * @param job 定时任务
	 * @return
	 */
	public boolean has(Job job) {
		boolean result = true;
		try {
			if (!scheduler.isShutdown()) {
				String jobName = job.getJobName();
				String jobGroup = job.getJobGroup();
				TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
				Trigger trigger = scheduler.getTrigger(triggerKey);
				result = (trigger != null) ? true : false;
			} else {
				result = false;
			}
		} catch (SchedulerException e) {
			log.info("判断是否存在定时任务异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 获得定时任务状态
	 * 
	 * @param job 定时任务
	 * @return
	 */
	public String getStatus(Job job) {
		String status = StrUtil.EMPTY;
		try {
			String jobName = job.getJobName();
			String jobGroup = job.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			TriggerState triggerState = scheduler.getTriggerState(triggerKey);
			status = triggerState.toString();
		} catch (Exception e) {
			log.info("获得定时任务状态异常：{}", e.getMessage());
		}
		return StrUtil.isNotEmpty(status) ? status : TriggerState.NONE.toString();
	}

	/**
	 * 启动调度器
	 * 
	 * @return
	 */
	public boolean startScheduler() {
		boolean result = true;
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			log.info("启动调度器异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 关闭调度器
	 * 
	 * @return
	 */
	public boolean standbyScheduler() {
		boolean result = true;
		try {
			if (!scheduler.isShutdown()) {
				scheduler.standby();
			}
		} catch (SchedulerException e) {
			log.info("关闭调度器异常：{}", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 判断调度器是否为开启状态
	 * 
	 * @return
	 */
	public boolean isStarted() {
		boolean result = true;
		try {
			result = scheduler.isStarted();
		} catch (SchedulerException e) {
			log.info("判断调度器是否为开启状态异常：{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 判断调度器是否为关闭状态
	 * 
	 * @return
	 */
	public boolean isShutdown() {
		boolean result = true;
		try {
			result = scheduler.isShutdown();
		} catch (SchedulerException e) {
			log.info("判断调度器是否为关闭状态异常：{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 判断调度器是否为待机状态
	 * 
	 * @return
	 */
	public boolean isInStandbyMode() {
		boolean result = true;
		try {
			result = scheduler.isInStandbyMode();
		} catch (SchedulerException e) {
			log.info("判断调度器是否为待机状态异常：{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 获得下一次执行时间
	 * 
	 * @param cronExpression cron表达式
	 * @return
	 */
	public LocalDateTime nextfireDate(String cronExpression) {
		LocalDateTime localDateTime = null;
		try {
			if (StrUtil.isNotEmpty(cronExpression)) {
				CronExpression ce = new CronExpression(cronExpression);
				Date nextInvalidTimeAfter = ce.getNextInvalidTimeAfter(new Date());
				localDateTime = Instant.ofEpochMilli(nextInvalidTimeAfter.getTime()).atZone(ZoneId.systemDefault())
						.toLocalDateTime();
			}
		} catch (ParseException e) {
			log.info("获得下一次执行时间异常：{}", e.getMessage());
		}
		return localDateTime;
	}

}