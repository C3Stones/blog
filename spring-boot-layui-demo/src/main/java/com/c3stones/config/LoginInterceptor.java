package com.c3stones.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 * 
 * @author CL
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	/**
	 * 拦截处理
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Object user = request.getSession().getAttribute("user");
		if (null == user) {
			response.sendRedirect("/login");
		}
		return true;
	}

}