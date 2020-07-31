package com.c3stones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置类
 * 
 * @author CL
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

	/**
	 * 注入Docket
	 * 
	 * @return
	 */
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(setApiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.c3stones.controller")).paths(PathSelectors.any())
				.build();
	}

	/**
	 * 配置在线文档的基本信息
	 * 
	 * @return
	 */
	private ApiInfo setApiInfo() {
		return new ApiInfoBuilder().title("SpringBoot整合Swagger2示例")
				.description("<a href='https://www.cnblogs.com/cao-lei/' target='_blank'>欢迎访问我的博客</a>")
				.termsOfServiceUrl("https://www.cnblogs.com/cao-lei/").version("V1.0").build();
	}

}
