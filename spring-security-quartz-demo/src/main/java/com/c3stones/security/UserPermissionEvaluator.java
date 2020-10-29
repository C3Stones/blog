package com.c3stones.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.c3stones.security.entity.UserDetails;
import com.c3stones.sys.entity.Auth;
import com.c3stones.sys.service.AuthService;

/**
 * 用户权限注解处理类
 * 
 * @author CL
 *
 */
@Component
public class UserPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	private AuthService authService;

	/**
	 * 判断是否拥有权限
	 * 
	 * @param authentication 用户身份
	 * @param targetUrl      目标路径
	 * @param permission     路径权限
	 * 
	 * @return 是否拥有权限
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// 用户权限
		Set<String> permissions = new HashSet<String>();

		// 查询用户权限
		List<Auth> authList = authService.findByUserId(userDetails.getId());
		authList.forEach(auth -> {
			permissions.add(auth.getPermission());
		});

		// 判断是否拥有权限
		if (permissions.stream().filter(p -> (permission.toString().startsWith(p))).count() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}