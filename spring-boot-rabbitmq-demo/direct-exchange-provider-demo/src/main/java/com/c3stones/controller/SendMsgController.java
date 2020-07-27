package com.c3stones.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.config.RabbitMqConfig;

/**
 * 发送消息Controller
 * 
 * @author CL
 *
 */
@RestController
public class SendMsgController {

	private static Logger log = LoggerFactory.getLogger(SendMsgController.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 发送消息
	 * 
	 * @param msg 消息内容
	 * @return
	 */
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public boolean send(String msg) {
		try {
			rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUNTING_KEY, msg);
		} catch (AmqpException e) {
			log.error("发送消息异常：{}", e);
			return false;
		}
		return true;
	}
}
