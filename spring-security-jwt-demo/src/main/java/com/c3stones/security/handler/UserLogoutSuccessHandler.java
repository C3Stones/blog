package com.c3stones.security.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.c3stones.utils.ResponseUtils;

/**
 * 登出成功处理类
 * 
 * @author CL
 *
 */
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		SecurityContextHolder.clearContext();
		ResponseUtils.responseJson(response, ResponseUtils.response(200, "登出成功", null));
	}
}
