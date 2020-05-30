package com.c3stones.callback;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试回调
 * 
 * @author CL
 *
 */
@Service
@Slf4j
public class TestCallbackService {

	@SneakyThrows
	public String get() {
		log.info("调用TestCallbackService.get()！");
		Thread.sleep(2000);
		return "get";
	}

	@Async
	public ListenableFuture<String> asyncCallback() {
		try {
			return AsyncResult.forValue(this.get());
		} catch (Throwable ex) {
			return AsyncResult.forExecutionException(ex);
		}
	}
}
