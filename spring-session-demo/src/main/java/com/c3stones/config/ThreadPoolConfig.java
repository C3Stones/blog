package com.c3stones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 * 
 * @author CL
 *
 */
@Configuration
public class ThreadPoolConfig {

	/**
	 * Spring Session任务执行器配置
	 * 
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor springSessionRedisTaskExecutor() {
		ThreadPoolTaskExecutor springSessionRedisTaskExecutor = new ThreadPoolTaskExecutor();
		springSessionRedisTaskExecutor.setCorePoolSize(8);
		springSessionRedisTaskExecutor.setMaxPoolSize(16);
		springSessionRedisTaskExecutor.setKeepAliveSeconds(10);
		springSessionRedisTaskExecutor.setQueueCapacity(1000);
		springSessionRedisTaskExecutor.setThreadNamePrefix("spring-session-thread:");
		return springSessionRedisTaskExecutor;
	}
}