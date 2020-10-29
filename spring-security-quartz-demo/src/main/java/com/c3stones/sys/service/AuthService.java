package com.c3stones.sys.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.c3stones.sys.entity.Auth;
import com.c3stones.sys.entity.Role;
import com.c3stones.sys.entity.RoleAuth;

/**
 * 系统权限Service
 * 
 * @author CL
 *
 */
public interface AuthService extends IService<Auth> {

	/**
	 * 根据用户ID查询
	 * 
	 * @param userId 用户ID
	 * @return 系统权限列表
	 */
	public List<Auth> findByUserId(Integer userId);

	/**
	 * 查询列表数据
	 * 
	 * @param auth    系统权限
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	public Page<Auth> listData(Auth auth, long current, long size);

	/**
	 * 查询绑定角色列表数据
	 * 
	 * @param roleAuth 角色权限
	 * @param role     系统角色
	 * @param current  当前页
	 * @param size     每页显示条数
	 * @return
	 */
	public Page<Role> roleListData(RoleAuth roleAuth, Role role, long current, long size);

}