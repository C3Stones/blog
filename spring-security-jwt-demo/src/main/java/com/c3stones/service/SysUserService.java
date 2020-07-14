package com.c3stones.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.c3stones.entity.SysAuth;
import com.c3stones.entity.SysRole;
import com.c3stones.entity.SysUser;

/**
 * 系统用户Service
 * 
 * @author CL
 *
 */
public interface SysUserService extends IService<SysUser> {

	/**
	 * 根据用户名称查询用户信息
	 * 
	 * @param username 用户名称
	 * @return
	 */
	SysUser findUserByUserName(String username);

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