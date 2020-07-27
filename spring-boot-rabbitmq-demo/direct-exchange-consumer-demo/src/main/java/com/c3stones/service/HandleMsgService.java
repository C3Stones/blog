package com.c3stones.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 处理消息Service
 * 
 * @author CL
 *
 */
@Component
public class HandleMsgService {

	private static Logger log = LoggerFactory.getLogger(HandleMsgService.class);

	/**
	 * 方法1-处理消息
	 * 
	 * @param msg 消息内容
	 */
	@RabbitListener(queues = "test.queue")
	public void handle1(String msg) {
		log.info("方法1已接收到消息：{}", msg);
	}

	/**
	 * 方法2-处理消息
	 * 
	 * @param msg 消息内容
	 */
	@RabbitListener(queues = "test.queue")
	public void handle2(String msg) {
		log.info("方法2已接收到消息：{}", msg);
	}

	/**
	 * 方法3-处理消息
	 * 
	 * @param msg 消息内容
	 */
	@RabbitListener(queues = "test.queue")
	public void handle3(String msg) {
		log.info("方法3已接收到消息：{}", msg);
	}

}
