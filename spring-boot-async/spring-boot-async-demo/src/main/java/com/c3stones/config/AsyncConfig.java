package com.c3stones.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import com.c3stones.handle.GlobalAsyncUncaughtExceptionHandler;

/**
 * 异步配置类
 * 
 * @author CL
 *
 */
@Configuration
@EnableAsync // 开启异步支持（和Application启动类中保留一个即可，建议保留在此处）
public class AsyncConfig implements AsyncConfigurer {

	@Autowired
	private GlobalAsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler;

	/**
	 * 返回 Spring Task异步任务的默认执行器。<br/>
	 * 返回了null，则使用
	 * TaskExecutionAutoConfiguration自动化配置类创建的ThreadPoolTaskExecutor任务执行器作为默认执行器
	 */
	@Override
	public Executor getAsyncExecutor() {
		return null;
	}

	/**
	 * 返回自定义的全局异步异常处理器
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return asyncUncaughtExceptionHandler;
	}

}