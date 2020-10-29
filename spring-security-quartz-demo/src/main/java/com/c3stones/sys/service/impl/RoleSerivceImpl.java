package com.c3stones.sys.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.sys.entity.Role;
import com.c3stones.sys.entity.User;
import com.c3stones.sys.entity.UserRole;
import com.c3stones.sys.mapper.RoleMapper;
import com.c3stones.sys.mapper.UserMapper;
import com.c3stones.sys.mapper.UserRoleMapper;
import com.c3stones.sys.service.RoleService;

import cn.hutool.core.util.StrUtil;

/**
 * 系统角色Service实现
 * 
 * @author CL
 *
 */
@Service
public class RoleSerivceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private UserMapper userMapper;

	/**
	 * 根据用户ID查询
	 * 
	 * @param userId 用户ID
	 * @return 系统角色列表
	 */
	@Override
	public List<Role> findByUserId(Integer userId) {
		return this.baseMapper.findByUserId(userId);
	}

	/**
	 * 查询列表数据
	 * 
	 * @param role    系统角色
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	@Override
	public Page<Role> listData(Role role, long current, long size) {
		QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
		if (null != role.getId()) {
			queryWrapper.eq("id", role.getId());
		}
		if (StrUtil.isNotBlank(role.getRoleName())) {
			queryWrapper.like("role_name", role.getRoleName());
		}
		if (StrUtil.isNotBlank(role.getRoleCode())) {
			queryWrapper.like("role_code", role.getRoleCode());
		}
		return baseMapper.selectPage(new Page<>(current, size), queryWrapper);
	}

	/**
	 * 检验角色编码是否唯一
	 * 
	 * @param roleCode 角色编码
	 * @return
	 */
	@Override
	public Boolean checkRoleCode(@NotNull String roleCode) {
		if (StrUtil.isNotBlank(roleCode)) {
			QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
			queryWrapper.like("role_code", roleCode);
			Integer count = baseMapper.selectCount(queryWrapper);
			return (count != null && count > 0) ? false : true;
		}
		return false;
	}

	/**
	 * 查询绑定用户列表数据
	 * 
	 * @param userRole 用户角色
	 * @param user     系统用户
	 * @param current  当前页
	 * @param size     每页显示条数
	 * @return
	 */
	@Override
	public Page<User> userListData(UserRole userRole, User user, long current, long size) {
		Page<User> page = new Page<>(current, size);
		QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("role_id", userRole.getRoleId());
		List<UserRole> userRoleList = userRoleMapper.selectList(queryWrapper);
		if (!ListUtils.isEmpty(userRoleList)) {
			QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
			userQueryWrapper.in("id", userRoleList.stream().map(UserRole::getUserId).collect(Collectors.toList()));
			if (StrUtil.isNotBlank(user.getUsername())) {
				userQueryWrapper.like("username", user.getUsername());
			}
			if (StrUtil.isNotBlank(user.getNickname())) {
				queryWrapper.like("nickname", user.getNickname());
			}
			return userMapper.selectPage(page, userQueryWrapper);
		}
		return page;
	}

}