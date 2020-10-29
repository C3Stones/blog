package com.c3stones.job.controller;

import java.time.LocalDateTime;

import org.quartz.Trigger.TriggerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c3stones.common.vo.Response;
import com.c3stones.job.config.QuartzHandler;
import com.c3stones.job.entity.Job;
import com.c3stones.job.service.JobService;
import com.c3stones.sys.entity.User;
import com.c3stones.sys.utils.UserUtils;

/**
 * 定时任务Controller
 * 
 * @author CL
 *
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

	@Autowired
	private QuartzHandler quartzHandler;

	@Autowired
	private JobService jobService;

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/list', 'sys:job:view')")
	@RequestMapping(value = "list")
	public String list() {
		return "pages/job/jobList";
	}

	/**
	 * 查询列表数据
	 * 
	 * @param user    系统用户
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/listData', 'sys:job:view')")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Response<Page<Job>> listData(Job job, @RequestParam(name = "page") long current,
			@RequestParam(name = "limit") long size) {
		Page<Job> page = jobService.listData(job, current, size);
		return Response.success(page);
	}

	/**
	 * 更新
	 * 
	 * @param job 定时任务
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/update', 'sys:job:edit')")
	@RequestMapping(value = "update")
	@ResponseBody
	public Response<Boolean> update(Job job) {
		Assert.notNull(job.getId(), "ID不能为空");
		User user = UserUtils.get();
		if (user != null) {
			job.setUpdateUserId(user.getId());
		}
		LocalDateTime now = LocalDateTime.now();
		job.setUpdateDate(now);
		boolean result = jobService.updateById(job);
		Job queryJob = jobService.getById(job.getId());
		String status = quartzHandler.getStatus(queryJob);
		if (!(TriggerState.NONE.toString()).equals(status)) {
			result = quartzHandler.updateCronExpression(queryJob, queryJob.getCronExpression());
		}
		return Response.success("更新" + (result ? "成功" : "失败"), result);
	}

	/**
	 * 删除
	 * 
	 * @param job 定时任务
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/delete', 'sys:job:edit')")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Response<Boolean> delete(Job job) {
		Assert.notNull(job.getId(), "ID不能为空");
		Job queryJob = jobService.getById(job.getId());
		boolean result = true;
		if (!(TriggerState.NONE.toString()).equals(queryJob.getStatus())) {
			result = quartzHandler.delete(queryJob);
		}
		if (result) {
			result = jobService.removeById(job.getId());
		}
		return Response.success("删除" + (result ? "成功" : "失败"), result);
	}

	/**
	 * 启动
	 * 
	 * @param job 定时任务
	 * @return
	 * @throws ClassNotFoundException
	 */
	@PreAuthorize(value = "hasPermission('/job/start', 'sys:job:edit')")
	@RequestMapping(value = "start")
	@ResponseBody
	public Response<Boolean> start(Job job) throws ClassNotFoundException {
		Assert.notNull(job.getId(), "ID不能为空");
		Job queryJob = jobService.getById(job.getId());
		Assert.notNull(queryJob, "定时任务不存在");
		Class<?> clazz = Class.forName(queryJob.getBeanClass());
		Assert.notNull(clazz, "未找到任务执行类");
		boolean result = quartzHandler.start(queryJob, clazz);
		return Response.success("启动" + (result ? "成功" : "失败"), result);
	}

	/**
	 * 暂停
	 * 
	 * @param job 定时任务
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/pasue', 'sys:job:edit')")
	@RequestMapping(value = "pasue")
	@ResponseBody
	public Response<Boolean> pasue(Job job) {
		Assert.notNull(job.getId(), "ID不能为空");
		Job queryJob = jobService.getById(job.getId());
		Assert.notNull(queryJob, "定时任务不存在");

		String status = quartzHandler.getStatus(queryJob);
		if (!((TriggerState.NORMAL.toString()).equals(status) || (TriggerState.PAUSED.toString()).equals(status)
				|| (TriggerState.BLOCKED.toString()).equals(status))) {
			return Response.success("当前状态不可暂停", false);
		}
		if ((TriggerState.PAUSED.toString()).equals(status)) {
			return Response.success("已暂停", false);
		}

		boolean result = quartzHandler.pasue(queryJob);
		return Response.success("暂停" + (result ? "成功" : "失败"), result);
	}

	/**
	 * 立即执行
	 * 
	 * @param job 定时任务
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/trigger', 'sys:job:edit')")
	@RequestMapping(value = "trigger")
	@ResponseBody
	public Response<Boolean> trigger(Job job) {
		Assert.notNull(job.getId(), "ID不能为空");
		Job queryJob = jobService.getById(job.getId());
		Assert.notNull(queryJob, "定时任务不存在");

		String status = quartzHandler.getStatus(queryJob);
		if (!((TriggerState.NORMAL.toString()).equals(status) || (TriggerState.PAUSED.toString()).equals(status)
				|| (TriggerState.COMPLETE.toString()).equals(status))) {
			return Response.success("当前状态不可立即执行", false);
		}

		boolean result = quartzHandler.trigger(queryJob);
		return Response.success("立即执行" + (result ? "成功" : "失败"), result);
	}

	/**
	 * 判断定时器是否为待机模式
	 */
	@PreAuthorize(value = "hasPermission('/job/isInStandbyMode', 'sys:job:view')")
	@RequestMapping(value = "isInStandbyMode")
	@ResponseBody
	public Response<Boolean> isInStandbyMode() {
		boolean result = quartzHandler.isInStandbyMode();
		return Response.success(result);
	}

	/**
	 * 启动定时器
	 * 
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/startScheduler', 'sys:job:edit')")
	@RequestMapping(value = "startScheduler")
	@ResponseBody
	public Response<Boolean> startScheduler() {
		boolean result = quartzHandler.startScheduler();
		return Response.success("启动定时器" + (result ? "成功" : "失败"), result);
	}

	/**
	 * 待机定时器
	 * 
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/standbyScheduler', 'sys:job:edit')")
	@RequestMapping(value = "standbyScheduler")
	@ResponseBody
	public Response<Boolean> standbyScheduler() {
		boolean result = quartzHandler.standbyScheduler();
		return Response.success("关闭定时器" + (result ? "成功" : "失败"), result);
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/add', 'sys:job:edit')")
	@RequestMapping(value = "add")
	public String add() {
		return "pages/job/jobAdd";
	}

	/**
	 * 保存
	 * 
	 * @param job 定时任务
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/job/save', 'sys:job:edit')")
	@RequestMapping(value = "save")
	@ResponseBody
	public Response<Boolean> save(Job job) {
		User user = UserUtils.get();
		if (user != null) {
			job.setCreateUserId(user.getId());
			job.setUpdateUserId(user.getId());
		}
		LocalDateTime now = LocalDateTime.now();
		job.setCreateDate(now);
		job.setUpdateDate(now);
		boolean result = jobService.save(job);
		return Response.success(result);
	}

}