package com.c3stones.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步配置
 * 
 * @author CL
 *
 */
@Configuration
@EnableAsync // 开启异步的支持
public class AsyncConfig {

	/**
	 * 执行器1
	 */
	public static final String EXECUTOR_ONE = "executor-one";
	/**
	 * 执行器2
	 */
	public static final String EXECUTOR_TWO = "executor-two";

	/**
	 * 配置执行器1
	 * 
	 * @author CL
	 *
	 */
	@Configuration
	public static class ExecutorOneConfiguration {

		@Bean(name = EXECUTOR_ONE + "-properties")
		@ConfigurationProperties(prefix = "spring.task.execution-one")
		@Primary
		public TaskExecutionProperties taskExecutionProperties() {
			return new TaskExecutionProperties();
		}

		@Bean(name = EXECUTOR_ONE)
		public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
			// 通过TaskExecutorBuilder对象创建ThreadPoolTaskExecutor
			TaskExecutorBuilder builder = createTaskExecutorBuilder(this.taskExecutionProperties());
			return builder.build();
		}

	}

	/**
	 * 配置执行器2
	 * 
	 * @author CL
	 *
	 */
	@Configuration
	public static class ExecutorTwoConfiguration {

		@Bean(name = EXECUTOR_TWO + "-properties")
		@ConfigurationProperties(prefix = "spring.task.execution-two")
		public TaskExecutionProperties taskExecutionProperties() {
			return new TaskExecutionProperties();
		}

		@Bean(name = EXECUTOR_TWO)
		public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
			// 通过TaskExecutorBuilder对象创建ThreadPoolTaskExecutor
			TaskExecutorBuilder builder = createTaskExecutorBuilder(this.taskExecutionProperties());
			return builder.build();
		}

	}

	/**
	 * 创建TaskExecutorBuilder
	 * 
	 * @param properties 配置，从配置文件读取
	 * @return
	 */
	private static TaskExecutorBuilder createTaskExecutorBuilder(TaskExecutionProperties properties) {
		TaskExecutionProperties.Pool pool = properties.getPool();
		TaskExecutorBuilder builder = new TaskExecutorBuilder();
		// 配置属性，与配置文件对应
		// 其它基本属性
		builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
		// Pool 属性
		builder = builder.corePoolSize(pool.getCoreSize());
		builder = builder.maxPoolSize(pool.getMaxSize());
		builder = builder.keepAlive(pool.getKeepAlive());
		builder = builder.queueCapacity(pool.getQueueCapacity());
		builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
		// Shutdown 属性
		TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
		builder = builder.awaitTermination(shutdown.isAwaitTermination());
		builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
		return builder;
	}

}