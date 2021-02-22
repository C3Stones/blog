package com.c3stones.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.service.SendService;

/**
 * 发送Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "/send")
public class SendController {

	private static final Logger logger = LoggerFactory.getLogger(SendController.class);

	@Autowired
	private SendService sendService;

	/**
	 * 发送短信
	 * 
	 * @return
	 */
	@RequestMapping(value = "")
	public String send() {
		long startTime = System.currentTimeMillis();
		String result = sendService.sms();
		long endTime = System.currentTimeMillis();
		logger.info("总耗时：" + (endTime - startTime) / 1000 + " s");
		return result;
	}

}
