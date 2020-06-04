package com.c3stones.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户Controller
 * 
 * @author CL
 *
 */
@RestController
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Value("${server.port}")
	private Integer port;

	/**
	 * 模拟登录
	 * 
	 * @param session  HttpSession
	 * @param username 用户名
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login(HttpSession session, String username) {
		session.setAttribute("username", username);
		String msg = String.format("用户登录的当前服务端口：%s，用户名称为：%s，Session ID为：%s", port, username, session.getId());
		logger.info(msg);
		return msg;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param session  HttpSession
	 * @param username 用户名
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo")
	public String getUserInfo(HttpSession session) {
		String username = (String) session.getAttribute("username");
		String msg = String.format("获取到的当前服务端口：%s，用户名称为：%s，Session ID为：%s", port, username, session.getId());
		logger.info(msg);
		return msg;
	}

}
