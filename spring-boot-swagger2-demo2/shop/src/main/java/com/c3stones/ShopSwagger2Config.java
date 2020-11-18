package com.c3stones;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.c3stones.common.utils.Swagger2ConfigUtils;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * Shop模块Swagger2配置类
 * 
 * @author CL
 *
 */
@Configuration
public class ShopSwagger2Config {

	/**
	 * 新增Shop模块
	 * 
	 * @return
	 */
	@Bean
	public Docket shopApi() {
		String moduleCode = "shop";
		String moduleName = "商城模块";
		String basePackage = "com.c3stones.order.controller";
		return Swagger2ConfigUtils.docket(moduleCode, moduleName, basePackage);
	}

}
