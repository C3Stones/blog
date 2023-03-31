package com.c3tones.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 示例Service实现
 *
 * @author CL
 */
@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

    @Resource
    private TaskExecutor taskExecutor;

    /**
     * 示例方法
     */
    @Override
    public void demo() {
        taskExecutor.execute(() -> {
            log.info("线程 " + Thread.currentThread().getName() + " 正在执行Service中的方法");
        });
    }

}
