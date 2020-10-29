package com.c3stones.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统登录Controller
 * 
 * @author CL
 *
 */
@Controller
public class LoginController {

	/**
	 * 登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "login")
	public String login() {
		return "login";
	}

}
