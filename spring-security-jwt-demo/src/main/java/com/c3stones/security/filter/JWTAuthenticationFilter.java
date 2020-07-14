package com.c3stones.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.c3stones.security.config.JWTConfig;
import com.c3stones.security.entity.SysUserDetails;
import com.c3stones.security.utils.JWTTokenUtil;

/**
 * JWT权限过滤器，用于验证Token是否合法
 * 
 * @author CL
 *
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// 取出Token
		String token = request.getHeader(JWTConfig.tokenHeader);

		if (token != null && token.startsWith(JWTConfig.tokenPrefix)) {
			SysUserDetails sysUserDetails = JWTTokenUtil.parseAccessToken(token);

			if (sysUserDetails != null) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						sysUserDetails, sysUserDetails.getId(), sysUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}

}
