package com.c3stones.controller;

import com.c3stones.common.R;
import com.c3stones.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试Controller
 *
 * @author CL
 */
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    /**
     * 测试无锁
     *
     * @param processTime 业务处理时间
     * @return {@link R}
     */
    @RequestMapping("/test")
    public R<Long> test(long processTime) {
        return R.ok(testService.test(processTime));
    }

    /**
     * 测试默认分布式锁
     *
     * @param processTime 业务处理时间
     * @return {@link R}
     */
    @RequestMapping("/test1")
    public R<Long> test1(long processTime) {
        return R.ok(testService.test1(processTime));
    }

    /**
     * 测试自定义配置分布式锁
     *
     * @param processTime 业务处理时间
     * @return {@link R}
     */
    @RequestMapping("/test2")
    public R<Long> test2(long processTime) {
        return R.ok(testService.test2(processTime));
    }

    /**
     * 测试手动获取分布式锁
     *
     * @param processTime 业务处理时间
     * @return {@link R}
     */
    @RequestMapping("/test3")
    public R<Long> test3(long processTime) {
        return R.ok(testService.test3(processTime));
    }

    /**
     * 测试5秒内只能访问1次
     *
     * @param processTime 业务处理时间
     * @return {@link R}
     */
    @RequestMapping("/test4")
    public R<Long> test4(long processTime) {
        return R.ok(testService.test4(processTime));
    }

}
