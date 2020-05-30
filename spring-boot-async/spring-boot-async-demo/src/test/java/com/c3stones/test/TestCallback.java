package com.c3stones.test;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import com.c3stones.Application;
import com.c3stones.callback.TestCallbackService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试异步回调
 * 
 * @author CL
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestCallback {

	@Autowired
	private TestCallbackService callbackService;

	/**
	 * 测试异步回调
	 */
	@Test
	@SneakyThrows
	public void testCallback() {
		LocalDateTime startTime = LocalDateTime.now();

		ListenableFuture<String> listenableFuture = callbackService.asyncCallback();
		log.info("返回类型为：" + listenableFuture.getClass().getSimpleName());

		// 分别增加成功和异常的回调
		listenableFuture.addCallback(new SuccessCallback<String>() {

			@Override
			public void onSuccess(String result) {
				log.info("执行成功！");
			}

		}, new FailureCallback() {

			@Override
			public void onFailure(Throwable ex) {
				log.error("执行异常：" + ex);
			}

		});
		// 增加统一的成功和异常回调
		listenableFuture.addCallback(new ListenableFutureCallback<String>() {

			@Override
			public void onSuccess(String result) {
				log.info("执行成功！");
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("执行异常：" + ex);
			}

		});
		// 阻塞主线程
		listenableFuture.get();

		LocalDateTime endTime = LocalDateTime.now();

		log.info("异步调用，总耗时：" + Duration.between(startTime, endTime).toMillis() + " ms");
	}

}
