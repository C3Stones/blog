package com.c3tones.controller;

import com.c3tones.async.DemoAsync;
import com.c3tones.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 示例Controller
 *
 * @author CL
 */
@Slf4j
@RestController
public class DemoController {

    @Resource
    private DemoService demoService;

    @Resource
    private DemoAsync demoAsync;

    /**
     * Service示例方法
     *
     * @return {@link String}
     */
    @RequestMapping("/service")
    public void service() {
        log.info("Service示例方法开始执行");
        demoService.demo();
        log.info("Service示例方法结束执行");
    }

    /**
     * 异步示例方法
     *
     * @return {@link String}
     */
    @RequestMapping("/async")
    public void async() {
        log.info("异步示例方法开始执行");
        demoAsync.demo();
        log.info("异步示例方法结束执行");
    }

}
