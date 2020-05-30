package com.c3stones.exceptionhandle;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 测试异步异常
 * 
 * @author CL
 *
 */
@Service
public class TestAsyncExceptionService {

	@Async
	public String asyncException() {
		throw new RuntimeException("TestAsyncExceptionService.asyncException抛出异常！");
	}

}
