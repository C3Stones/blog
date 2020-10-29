package com.c3stones.sys.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.sys.entity.Auth;
import com.c3stones.sys.entity.Role;
import com.c3stones.sys.entity.RoleAuth;
import com.c3stones.sys.mapper.AuthMapper;
import com.c3stones.sys.mapper.RoleAuthMapper;
import com.c3stones.sys.mapper.RoleMapper;
import com.c3stones.sys.service.AuthService;

import cn.hutool.core.util.StrUtil;

/**
 * 系统权限Service实现
 * 
 * @author CL
 *
 */
@Service
public class AuthSerivceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {

	@Autowired
	private RoleAuthMapper roleAuthMapper;

	@Autowired
	private RoleMapper roleMapper;

	/**
	 * 根据用户ID查询
	 * 
	 * @param userId 用户ID
	 * @return 系统权限列表
	 */
	@Override
	public List<Auth> findByUserId(Integer userId) {
		return this.baseMapper.findByUserId(userId);
	}

	/**
	 * 查询列表数据
	 * 
	 * @param auth    系统权限
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	@Override
	public Page<Auth> listData(Auth auth, long current, long size) {
		QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();
		if (null != auth.getId()) {
			queryWrapper.eq("id", auth.getId());
		}
		if (StrUtil.isNotBlank(auth.getAuthName())) {
			queryWrapper.like("auth_name", auth.getAuthName());
		}
		if (StrUtil.isNotBlank(auth.getPermission())) {
			queryWrapper.like("permission", auth.getPermission());
		}
		return baseMapper.selectPage(new Page<>(current, size), queryWrapper);
	}

	/**
	 * 查询绑定角色列表数据
	 * 
	 * @param roleAuth 角色权限
	 * @param role     系统角色
	 * @param current  当前页
	 * @param size     每页显示条数
	 * @return
	 */
	@Override
	public Page<Role> roleListData(RoleAuth roleAuth, Role role, long current, long size) {
		Page<Role> page = new Page<>(current, size);
		QueryWrapper<RoleAuth> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("auth_id", roleAuth.getAuthId());
		List<RoleAuth> roleAuthList = roleAuthMapper.selectList(queryWrapper);
		if (!ListUtils.isEmpty(roleAuthList)) {
			QueryWrapper<Role> userQueryWrapper = new QueryWrapper<>();
			userQueryWrapper.in("id", roleAuthList.stream().map(RoleAuth::getRoleId).collect(Collectors.toList()));
			if (StrUtil.isNotBlank(role.getRoleName())) {
				userQueryWrapper.like("role_name", role.getRoleName());
			}
			if (StrUtil.isNotBlank(role.getRoleCode())) {
				queryWrapper.like("role_code", role.getRoleCode());
			}
			return roleMapper.selectPage(page, userQueryWrapper);
		}
		return page;
	}

}