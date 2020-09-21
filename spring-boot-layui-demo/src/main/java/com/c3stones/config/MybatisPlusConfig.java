package com.c3stones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**
 * Mybatis-Plus配置类
 * 
 * @author CL
 *
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisPlusConfig {

	/**
	 * 注入分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}
}