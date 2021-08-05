package com.c3stones.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "")
public class DemoController {

	/**
	 * 示例方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/hello")
	public String hello() {
		return "Hello World!";
	}

}
