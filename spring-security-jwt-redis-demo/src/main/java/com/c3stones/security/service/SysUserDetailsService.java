package com.c3stones.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.c3stones.entity.SysRole;
import com.c3stones.entity.SysUser;
import com.c3stones.security.entity.SysUserDetails;
import com.c3stones.service.SysUserService;

/**
 * 用户登录Service
 * 
 * @author CL
 *
 */
@Service
public class SysUserDetailsService implements UserDetailsService {

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 根据用户名查用户信息
	 * 
	 * @param username 用户名
	 * @return 用户详细信息
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = sysUserService.findUserByUserName(username);
		if (sysUser != null) {
			SysUserDetails sysUserDetails = new SysUserDetails();
			BeanUtils.copyProperties(sysUser, sysUserDetails);

			Set<GrantedAuthority> authorities = new HashSet<>(); // 角色集合

			List<SysRole> roleList = sysUserService.findRoleByUserId(sysUserDetails.getId());
			roleList.forEach(role -> {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
			});

			sysUserDetails.setAuthorities(authorities);

			return sysUserDetails;
		}
		return null;
	}

}
