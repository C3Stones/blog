package com.c3tones.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * 示例异步任务
 *
 * @author CL
 */
@Slf4j
@Component
@EnableAsync
public class DemoAsync {

    /**
     * 示例方法
     */
    @Async(value = "taskExecutor")
    public void demo() {
        log.info("线程 " + Thread.currentThread().getName() + " 正在执行Async中的方法");
    }

}
