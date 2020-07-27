package com.c3stones.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

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
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "test.confirm.queue", durable = "true"), key = "test.confirm.key", exchange = @Exchange("c3stones.confirm.direct")))
	public void handle1(Message message, Channel channel) {
		try {
			log.info("方法1已接收到消息：{}", message.getBody());

			// 模拟处理异常
//			int a = 1 / 0;

			// 正常消费，手动应答
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			log.info("方法1处理消息异常：{}", e);

			// 正常消费，将消息重新放入队列里
			try {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			} catch (IOException e1) {
				log.info("将消息重新放入队列里异常：{}", e1);
			}
		}
	}

}
