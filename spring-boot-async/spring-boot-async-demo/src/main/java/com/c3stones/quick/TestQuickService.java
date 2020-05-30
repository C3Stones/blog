package com.c3stones.quick;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 快速入门
 * 
 * @author CL
 *
 */
@Service
@Slf4j
public class TestQuickService {

	@SneakyThrows
	public String get() {
		log.info("调用TestQuickService.get()！");
		Thread.sleep(2000);
		return "get";
	}

	@SneakyThrows
	public String get2() {
		log.info("调用TestQuickService.get2()！");
		Thread.sleep(5000);
		return "get2";
	}

	@Async
	public String asyncGet() {
		return this.get();
	}

	@Async
	public String asyncGet2() {
		return this.get2();
	}

	@Async
	public Future<String> asyncFutureGet() {
		return AsyncResult.forValue(this.get());
	}

	@Async
	public Future<String> asyncFutureGet2() {
		return AsyncResult.forValue(this.get2());
	}
}
