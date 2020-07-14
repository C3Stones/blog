package com.c3stones.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.dao.SysUserDao;
import com.c3stones.entity.SysAuth;
import com.c3stones.entity.SysRole;
import com.c3stones.entity.SysUser;
import com.c3stones.service.SysUserService;

/**
 * 系统用户Service实现
 * 
 * @author CL
 *
 */
@Service
public class SysUserSerivceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

	/**
	 * 根据用户名称查询用户信息
	 * 
	 * @param username 用户名称
	 * @return
	 */
	@Override
	public SysUser findUserByUserName(String username) {
		return this.baseMapper.selectOne(
				new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username).ne(SysUser::getStatus, "1"));
	}

	/**
	 * 根据用户ID查询角色
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	@Override
	public List<SysRole> findRoleByUserId(Long userId) {
		return this.baseMapper.findRoleByUserId(userId);
	}

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	@Override
	public List<SysAuth> findAuthByUserId(Long userId) {
		return this.baseMapper.findAuthByUserId(userId);
	}

}
