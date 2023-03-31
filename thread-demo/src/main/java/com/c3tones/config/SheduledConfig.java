package com.c3tones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义定时任务调度配置类
 *
 * @author CL
 */
@Configuration
public class SheduledConfig implements SchedulingConfigurer {

    /**
     * 配置定时任务
     *
     * @param scheduledTaskRegistrar 配置任务注册器
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskScheduler());

//        // 第二种方式
//        scheduledTaskRegistrar.setScheduler(scheduledExecutorService());
    }

    /**
     * 自定义任务调度器
     *
     * @return {@link TaskScheduler}
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(5);
        executor.setThreadNamePrefix("custom-scheduler-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

//    /**
//     * 自定义任务线程池
//     *
//     * @return {@link ScheduledExecutorService}
//     */
//    @Bean
//    public ScheduledExecutorService scheduledExecutorService() {
//        return Executors.newScheduledThreadPool(5);
//    }

}
