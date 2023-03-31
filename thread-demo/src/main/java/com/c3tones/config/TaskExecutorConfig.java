package com.c3tones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池配置类
 *
 * @author CL
 */
@Configuration
public class TaskExecutorConfig {

    /**
     * 自定义任务执行器
     *
     * @return {@link TaskExecutor}
     */
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数，默认1
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(corePoolSize);
        // 最大线程数，默认Integer.MAX_VALUE
        executor.setMaxPoolSize(corePoolSize * 2 + 1);
        // 空闲线程最大存活时间，默认60秒
        executor.setKeepAliveSeconds(3);
        // 等待队列及大小，默认Integer.MAX_VALUE
        executor.setQueueCapacity(500);
        // 线程的名称前缀，默认该Bean名称简写：org.springframework.util.ClassUtils.getShortName(java.lang.Class<?>)
        executor.setThreadNamePrefix("custom-thread-");
        // 当线程池达到最大时的处理策略，默认抛出RejectedExecutionHandler异常
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());         // 抛出RejectedExecutionHandler异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());    // 交由调用者的线程执行
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy()); // 丢掉最早未处理的任务
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());       // 丢掉新提交的任务
        // 等待所有任务结束后再关闭线程池，默认false
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待所有任务结束最长等待时间，默认0毫秒
        executor.setAwaitTerminationSeconds(10);
        // 执行初始化
        executor.initialize();
        return executor;
    }

}