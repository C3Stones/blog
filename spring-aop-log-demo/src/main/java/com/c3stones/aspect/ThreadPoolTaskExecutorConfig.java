package com.c3stones.aspect;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置类
 * 
 * @author CL
 *
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {

	@Bean
	public Executor customThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// Java虚拟机可用的处理器数
		int corePoolSize = Runtime.getRuntime().availableProcessors();
		// 配置核心线程数
		executor.setCorePoolSize(corePoolSize);
		// 配置最大线程数
		executor.setMaxPoolSize(corePoolSize * 2 + 1);
		// 配置队列大小
		executor.setQueueCapacity(100);
		// 空闲的多余线程最大存活时间
		executor.setKeepAliveSeconds(3);
		// 配置线程池中的线程的名称前缀
		executor.setThreadNamePrefix("thread-execute-");
		// 当线程池达到最大大小时，在调用者的线程中执行任务
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 执行初始化
		executor.initialize();
		return executor;
	}
}
