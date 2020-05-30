package com.c3stones.test;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.c3stones.Application;
import com.c3stones.service.TestExecutorService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试自定义执行器
 * 
 * @author CL
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestExecutor {

	@Autowired
	private TestExecutorService executorService;

	/**
	 * 自定义执行器
	 */
	@Test
	@SneakyThrows
	public void testExecute() {
		LocalDateTime startTime = LocalDateTime.now();

		executorService.get();
		executorService.get2();

		Thread.sleep(2000);

		LocalDateTime endTime = LocalDateTime.now();

		log.info("同步调用，总耗时：" + Duration.between(startTime, endTime).toMillis() + " ms");
	}

}