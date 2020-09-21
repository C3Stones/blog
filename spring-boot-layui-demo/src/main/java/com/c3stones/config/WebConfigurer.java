package com.c3stones.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Setter;

/**
 * Web配置类
 * 
 * @author CL
 *
 */
@Configuration
@ConfigurationProperties(prefix = "security.web")
public class WebConfigurer implements WebMvcConfigurer {

	/**
	 * 忽略的URL
	 */
	@Setter
	private List<String> excludes;

	/**
	 * 配置拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns(excludes);
	}

}