package com.c3stones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.config.ModuleProperties;
import com.c3stones.config.ModuleProperties.Module;

/**
 * ConfigurationProperties 注解获取参数值 Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "config")
public class ConfigurationPropertiesController {

	@Autowired
	private ModuleProperties moduleProperties;

	/**
	 * 获取参数
	 * 
	 * @return
	 */
	@RequestMapping(value = "get")
	public String get() {
		List<Module> module = moduleProperties.getModule();
		return module.toString();
	}

}
