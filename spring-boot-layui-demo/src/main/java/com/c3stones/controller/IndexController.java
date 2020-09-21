package com.c3stones.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 系统首页Controller
 * 
 * @author CL
 *
 */
@Controller
public class IndexController {

	/**
	 * 首页
	 * 
	 * @return
	 */
	@GetMapping(value = "index")
	public String index(Model model, HttpSession httpSession) {
		model.addAttribute("user", httpSession.getAttribute("user"));
		return "index";
	}
	
	/**
	 * 控制台
	 * 
	 * @return
	 */
	@GetMapping(value = "view")
	public String view() {
		return "pages/view";
	}

}
