package com.c3stones.job.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.job.entity.Job;

/**
 * 定时任务Mapper
 * 
 * @author CL
 *
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {

}