package com.c3tones.sheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 示例定时任务
 *
 * @author CL
 */
@Slf4j
@Component
@EnableScheduling
public class DemoScheduled {

    /**
     * 示例方法
     */
    @Scheduled(cron = "0/3 * * * * ? ")
    public void demo() {
        StringBuilder sb = new StringBuilder().append("sadsa");
        log.info("线程 " + Thread.currentThread().getName() + " 正在执行Scheduled中的方法");
    }

}
