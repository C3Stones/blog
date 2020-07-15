package com.c3stones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.entity.SysUser;
import com.c3stones.entity.SysUserRole;
import com.c3stones.service.SysUserRoleService;
import com.c3stones.service.SysUserService;
import com.c3stones.utils.ResponseUtils;

/**
 * 注册用户实例Contrller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "/register")
public class RegisterController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 注册普通用户
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	@RequestMapping(value = "/user")
	public ResponseUtils user(String username, String password) {
		SysUser sysUser = new SysUser();
		sysUser.setUsername(username);
		sysUser.setPassword(bCryptPasswordEncoder.encode(password));
		sysUser.setStatus("0");
		sysUserService.save(sysUser);

		// 注册普通用户
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUserId(sysUser.getId());
		sysUserRole.setRoleId(2L);
		sysUserRoleService.save(sysUserRole);

		return ResponseUtils.success(sysUser);
	}
}
