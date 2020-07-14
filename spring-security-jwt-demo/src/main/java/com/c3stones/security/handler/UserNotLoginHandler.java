package com.c3stones.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.c3stones.utils.ResponseUtils;

/**
 * 未登录处理类
 * 
 * @author CL
 *
 */
@Component
public class UserNotLoginHandler implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ResponseUtils.responseJson(response, ResponseUtils.response(401, "未登录", authException.getMessage()));
	}
}
