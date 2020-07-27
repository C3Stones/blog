package com.c3stones.comtroller;

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
	 * 发送消息1
	 * 
	 * @param msg 消息内容
	 * @return
	 */
	@RequestMapping(value = "/send1", method = RequestMethod.GET)
	public boolean send1(String msg) {
		try {
			rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.BINDING_KEY_1, msg);
		} catch (AmqpException e) {
			log.error("发送消息1异常：{}", e);
			return false;
		}
		return true;
	}

	/**
	 * 发送消息2
	 * 
	 * @param msg 消息内容
	 * @return
	 */
	@RequestMapping(value = "/send2", method = RequestMethod.GET)
	public boolean send2(String msg) {
		try {
			rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.BINDING_KEY_2, msg);
		} catch (AmqpException e) {
			log.error("发送消息2异常：{}", e);
			return false;
		}
		return true;
	}
}
