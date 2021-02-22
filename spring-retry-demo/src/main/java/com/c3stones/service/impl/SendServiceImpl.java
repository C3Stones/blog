package com.c3stones.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.c3stones.exceptions.BusinessException;
import com.c3stones.service.SendService;

/**
 * 发送Service 实现
 * 
 * @author CL
 *
 */
@Service
public class SendServiceImpl implements SendService {

	private static final Logger logger = LoggerFactory.getLogger(SendServiceImpl.class);

	/**
	 * 发送短信
	 * 
	 * <p>
	 * @Retryable ： 需要重试
	 * 		value ： 当抛出指定异常时，开始重试
	 * 		maxAttempts ： 最大重试次数，默认为3
	 * 
	 * @Backoff	： 重试中的退避策略
	 * 		delay ： 延迟时间，默认为0
	 * 		maxDelay ： 最大延迟时间，默认为0
	 *  	multiplier ： 此次延时时间和上一次延迟时间的倍数，默认为0
	 * </p>
	 */
	@Override
	@Retryable(value = BusinessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 10000, multiplier = 2))
	public String sms() {
		logger.info("开始发送短信！");
		throw new BusinessException("发送短信异常");
	}

	/**
	 * 兜底方法，即多次重试后仍失败则执行此方法
	 * 
	 * @param e
	 * @return
	 */
	@Recover
	public String recover(BusinessException e) {
		logger.info("重试发送失败");
		return e.getMessage();
	}

}
