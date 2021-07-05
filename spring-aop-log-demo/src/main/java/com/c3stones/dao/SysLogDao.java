package com.c3stones.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.entity.SysLog;

/**
 * 系统日志Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLog> {

}