package com.c3stones;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.c3stones.common.utils.Swagger2ConfigUtils;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * Core模块Swagger2配置类
 * 
 * @author CL
 *
 */
@Configuration
public class CoreSwagger2Config {

	/**
	 * 新增Core模块
	 * 
	 * @return
	 */
	@Bean
	public Docket coreApi() {
		String moduleCode = "core";
		String moduleName = "核心模块";
		String[] basePackage = { "com.c3stones.sys.controller", "com.c3stones.common.controller" };
		return Swagger2ConfigUtils.docket(moduleCode, moduleName, basePackage);
	}

}
