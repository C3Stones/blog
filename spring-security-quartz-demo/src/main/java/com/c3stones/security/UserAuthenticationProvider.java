package com.c3stones.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.c3stones.security.entity.UserDetails;
import com.c3stones.security.service.UserDetailsService;

import cn.hutool.core.util.StrUtil;

/**
 * 用户登录验证处理类
 * 
 * @author CL
 *
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * 身份验证
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 获取用户名
		String username = (String) authentication.getPrincipal();
		// 获取密码
		String password = (String) authentication.getCredentials();

		UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(username);
		if (userDetails == null) {
			throw new UsernameNotFoundException("用户名不存在");
		}

		if (!StrUtil.equals(username, userDetails.getUsername())
				|| !new BCryptPasswordEncoder().matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("用户名或密码错误");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	/**
	 * 支持指定的身份验证
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
