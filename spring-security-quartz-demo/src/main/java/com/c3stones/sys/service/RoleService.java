package com.c3stones.sys.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.c3stones.sys.entity.Role;
import com.c3stones.sys.entity.User;
import com.c3stones.sys.entity.UserRole;

/**
 * 系统角色Service
 * 
 * @author CL
 *
 */
public interface RoleService extends IService<Role> {

	/**
	 * 根据用户ID查询
	 * 
	 * @param userId 用户ID
	 * @return 系统角色列表
	 */
	public List<Role> findByUserId(Integer userId);

	/**
	 * 查询列表数据
	 * 
	 * @param role    系统角色
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	public Page<Role> listData(Role role, long current, long size);

	/**
	 * 检验角色编码是否唯一
	 * 
	 * @param roleCode 角色编码
	 * @return
	 */
	public Boolean checkRoleCode(@NotNull String roleCode);

	/**
	 * 查询绑定用户列表数据
	 * 
	 * @param userRole 用户角色
	 * @param user     系统用户
	 * @param current  当前页
	 * @param size     每页显示条数
	 * @return
	 */
	public Page<User> userListData(UserRole userRole, User user, long current, long size);

}