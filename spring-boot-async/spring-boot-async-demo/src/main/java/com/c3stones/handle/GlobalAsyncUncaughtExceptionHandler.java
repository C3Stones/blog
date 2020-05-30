package com.c3stones.handle;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异步异常处理器
 * 
 * @author CL
 *
 */
@Component
@Slf4j
public class GlobalAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

	/**
	 * 处理未捕获的异步调用异常
	 */
	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		log.error("方法：{}, 参数：{}，调用异常：{}", method, params, ex.getMessage());
	}

}
