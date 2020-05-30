package com.c3stones.test;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.c3stones.Application;
import com.c3stones.exceptionhandle.TestAsyncExceptionService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试异步异常处理
 * 
 * @author CL
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestAsyncException {

	@Autowired
	private TestAsyncExceptionService asyncExceptionService;

	/**
	 * 测试异步异常
	 */
	@Test
	@SneakyThrows
	public void testAsyncException() {
		LocalDateTime startTime = LocalDateTime.now();

		asyncExceptionService.asyncException();

		// 异步调用成功
		Thread.sleep(5000);

		LocalDateTime endTime = LocalDateTime.now();

		log.info("异步异常调用，总耗时：" + Duration.between(startTime, endTime).toMillis() + " ms");
	}

}
