package com.c3stones.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.entity.SysAuth;
import com.c3stones.entity.SysRole;
import com.c3stones.entity.SysUser;

/**
 * 系统用户Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {

	/**
	 * 根据用户ID查询角色
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<SysRole> findRoleByUserId(Long userId);

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<SysAuth> findAuthByUserId(Long userId);
}
