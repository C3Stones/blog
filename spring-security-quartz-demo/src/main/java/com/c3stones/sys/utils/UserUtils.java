package com.c3stones.sys.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.c3stones.security.entity.UserDetails;
import com.c3stones.sys.entity.User;

import cn.hutool.core.bean.BeanUtil;

/**
 * 用户工具类
 * 
 * @author CL
 *
 */
public class UserUtils {

	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	public static User get() {
		User user = new User();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		BeanUtil.copyProperties(userDetails, user, "password");
		return user;
	}

	/**
	 * 获取session
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return requestAttributes.getRequest();
	}

}