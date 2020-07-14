package com.c3stones.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.entity.SysRoleAuth;

/**
 * 系统角色权限Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysRoleAuthDao extends BaseMapper<SysRoleAuth> {

}
