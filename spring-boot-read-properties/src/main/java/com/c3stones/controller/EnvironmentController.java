package com.c3stones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Environment 获取参数值 Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "env")
public class EnvironmentController {

	@Autowired
	private Environment environment;

	/**
	 * 获取参数
	 * 
	 * @return
	 */
	@RequestMapping(value = "get")
	public String get() {
		return "产品名称为：" + environment.getProperty("product.name") + ", 作者为：" + environment.getProperty("product.author")
				+ ", 版本号为：" + environment.getProperty("product.version", "1.0");
	}

}
