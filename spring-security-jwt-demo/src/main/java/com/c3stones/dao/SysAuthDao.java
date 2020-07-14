package com.c3stones.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.entity.SysAuth;

/**
 * 系统权限Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysAuthDao extends BaseMapper<SysAuth> {

}
