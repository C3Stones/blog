package com.c3stones.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.c3stones.sys.utils.UserUtils;

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
	public String index(Model model) {
		model.addAttribute("user", UserUtils.get());
		return "index";
	}

	/**
	 * 控制台
	 * 
	 * @return
	 */
	@GetMapping(value = "view")
	public String view() {
		return "pages/sys/view";
	}

}
