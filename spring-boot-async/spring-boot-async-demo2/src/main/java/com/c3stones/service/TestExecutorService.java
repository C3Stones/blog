package com.c3stones.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.c3stones.config.AsyncConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义执行器
 * 
 * @author CL
 *
 */
@Service
@Slf4j
public class TestExecutorService {

	/**
	 * 指定执行器1
	 * 
	 * @return
	 */
	@Async(AsyncConfig.EXECUTOR_ONE)
	public String get() {
		log.info("调用TestExecutorService.get()！");
		return "get";
	}

	/**
	 * 指定执行器2
	 * 
	 * @return
	 */
	@Async(AsyncConfig.EXECUTOR_TWO)
	public String get2() {
		log.info("调用TestExecutorService.get2()！");
		return "get2";
	}

}