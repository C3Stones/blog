package com.c3stones.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.c3stones.security.entity.UserDetails;
import com.c3stones.sys.entity.Role;
import com.c3stones.sys.entity.User;
import com.c3stones.sys.service.RoleService;
import com.c3stones.sys.service.UserService;

/**
 * 用户登录Service
 * 
 * @author CL
 *
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	/**
	 * 根据用户名查用户信息
	 * 
	 * @param username 用户名称
	 * @return 用户详细信息
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", username);
		User user = userService.getOne(queryWrapper);
		if (user != null) {
			UserDetails userDetails = new UserDetails();
			BeanUtils.copyProperties(user, userDetails);

			// 用户角色
			Set<GrantedAuthority> authorities = new HashSet<>();

			// 查询用户角色
			List<Role> roleList = roleService.findByUserId(userDetails.getId());
			roleList.forEach(role -> {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
			});

			userDetails.setAuthorities(authorities);

			return userDetails;
		}
		return null;
	}

}