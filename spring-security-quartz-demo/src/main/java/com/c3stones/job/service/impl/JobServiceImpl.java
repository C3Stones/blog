package com.c3stones.job.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.job.config.QuartzHandler;
import com.c3stones.job.entity.Job;
import com.c3stones.job.mapper.JobMapper;
import com.c3stones.job.service.JobService;

import cn.hutool.core.util.StrUtil;

/**
 * 定时任务Service实现
 * 
 * @author CL
 *
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

	@Autowired
	private QuartzHandler quartzHandler;

	/**
	 * 查询列表数据
	 * 
	 * @param job     系统用户
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	@Override
	public Page<Job> listData(Job job, long current, long size) {
		QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
		if (StrUtil.isNotBlank(job.getJobName())) {
			queryWrapper.like("job_name", job.getJobName());
		}
		Page<Job> page = baseMapper.selectPage(new Page<>(current, size), queryWrapper);
		List<Job> records = page.getRecords();

		// 处理定时任务数据
		for (int i = 0; i < records.size(); i++) {
			Job j = records.get(i);
			// 获取下一次执行时间
			j.setNextfireDate(quartzHandler.nextfireDate(j.getCronExpression()));

			// 更新状态
			String status = quartzHandler.getStatus(j);
			if (!(status).equals(j.getStatus())) {
				j.setStatus(status);
				super.updateById(j);
			}

			records.set(i, j);
		}
		page.setRecords(records);
		return page;
	}
}