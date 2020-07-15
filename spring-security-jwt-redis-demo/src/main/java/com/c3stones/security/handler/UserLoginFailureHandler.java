package com.c3stones.security.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.c3stones.utils.ResponseUtils;

/**
 * 登录失败处理类
 * 
 * @author CL
 *
 */
@Component
public class UserLoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		ResponseUtils.responseJson(response, ResponseUtils.response(500, "登录失败", exception.getMessage()));
	}
}
