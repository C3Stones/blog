package com.c3stones.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.c3stones.job.entity.Job;

/**
 * 定时任务Service
 * 
 * @author CL
 *
 */
public interface JobService extends IService<Job> {

	/**
	 * 查询列表数据
	 * 
	 * @param job     系统用户
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	public Page<Job> listData(Job job, long current, long size);

}