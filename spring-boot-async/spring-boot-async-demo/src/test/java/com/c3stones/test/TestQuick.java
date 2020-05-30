package com.c3stones.test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.c3stones.Application;
import com.c3stones.quick.TestQuickService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试快速入门
 * 
 * @author CL
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestQuick {

	@Autowired
	private TestQuickService quickService;

	/**
	 * 测试同步调用
	 */
	@Test
	public void testSync() {
		LocalDateTime startTime = LocalDateTime.now();

		quickService.get();
		quickService.get2();

		LocalDateTime endTime = LocalDateTime.now();

		log.info("同步调用，总耗时：" + Duration.between(startTime, endTime).toMillis() + " ms");
	}

	/**
	 * 测试异步调用
	 */
	@Test
	public void testAsync() {
		LocalDateTime startTime = LocalDateTime.now();

		quickService.asyncGet();
		quickService.asyncGet2();

		LocalDateTime endTime = LocalDateTime.now();

		log.info("异步调用，总耗时：" + Duration.between(startTime, endTime).toMillis() + " ms");
	}

	/**
	 * 测试异步调用并阻塞主线程
	 */
	@Test
	@SneakyThrows
	public void testAsyncFuture() {
		LocalDateTime startTime = LocalDateTime.now();

		// 执行
		Future<String> getFuture = quickService.asyncFutureGet();
		Future<String> get2Future = quickService.asyncFutureGet2();

		// 阻塞等待执行结果
		getFuture.get();
		get2Future.get();

		LocalDateTime endTime = LocalDateTime.now();

		log.info("异步调用，总耗时：" + Duration.between(startTime, endTime).toMillis() + " ms");
	}

}
