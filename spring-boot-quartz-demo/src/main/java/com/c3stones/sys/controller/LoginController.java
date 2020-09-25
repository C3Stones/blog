package com.c3stones.sys.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.c3stones.common.vo.Response;
import com.c3stones.sys.entity.User;
import com.c3stones.sys.service.UserService;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;

/**
 * 系统登录Controller
 * 
 * @author CL
 *
 */
@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	/**
	 * 登录页
	 * 
	 * @return
	 */
	@GetMapping(value = { "login", "" })
	public String login() {
		return "login";
	}

	/***
	 * 登录验证
	 * 
	 * @param user 系统用户
	 * @return
	 */
	@PostMapping(value = "login")
	@ResponseBody
	public Response<User> login(User user, HttpSession session) {
		if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
			return Response.error("用户名或密码不能为空");
		}
		User queryUser = new User();
		queryUser.setUsername(user.getUsername());
		queryUser = userService.getOne(new QueryWrapper<>(queryUser));
		if (queryUser == null || !StrUtil.equals(queryUser.getUsername(), user.getUsername())
				|| !BCrypt.checkpw(user.getPassword(), queryUser.getPassword())) {
			return Response.error("用户名或密码错误");
		}
		session.setAttribute("user", queryUser);
		return Response.success("登录成功", queryUser);
	}

	/**
	 * 登出
	 * 
	 * @param httpSession
	 * @return
	 */
	@GetMapping(value = "logout")
	public String logout(HttpSession httpSession) {
		httpSession.invalidate();
		return "redirect:/login";
	}
	
}