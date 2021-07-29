package com.c3stones.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.c3stones.entity.User;
import com.c3stones.enums.Status;
import com.c3stones.service.UserService;

/**
 * 用户Controller
 *
 * @author CL
 */
@RestController
@RequestMapping(value = "user")
public class UserController {

	@Reference
	private UserService userService;

	/**
	 * 查询用户信息
	 *
	 * @param userCode 用户编码
	 * @return 用户信息
	 */
	@RequestMapping(value = "get")
	public User get(String userCode) {
		User user = userService.getById(userCode);
		if (user != null) {
			user.setStatus(Status.transf(user.getStatus()));
		}
		return user;
	}
}
