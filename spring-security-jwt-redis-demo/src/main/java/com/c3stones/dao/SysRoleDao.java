package com.c3stones.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.entity.SysRole;

/**
 * 系统角色Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRole> {

}
