package com.c3stones.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置Controller
 * 
 * @author CL
 *
 */
@RestController
@RefreshScope
public class ConfigController {

	@Value("${project-name}")
	private String projectName;

	@RequestMapping(value = "/getInfo")
	public String getInfo() {
		return projectName;
	}

}
