package com.c3stones.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 
 * @author CL
 *
 */
@Configuration
public class RabbitMqConfig {

	private static Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

	@Autowired
	private ConnectionFactory connectionFactory;

	/**
	 * 交换机名称
	 */
	public static final String EXCHANGE_NAME = "c3stones.confirm.direct";

	/**
	 * 路由键
	 */
	public static final String ROUNTING_KEY = "test.confirm.key";

	/**
	 * 队列名称
	 */
	public static final String QUEUE_NAME = "test.confirm.queue";

	/**
	 * 配置Direct交换机
	 * 
	 * @return
	 */
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}

	/**
	 * 配置队列
	 * 
	 * @return
	 */
	@Bean
	public Queue testQueue() {
		return new Queue(QUEUE_NAME);
	}

	/**
	 * 将队列与交换机通过路由键绑定
	 * 
	 * @return
	 */
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(testQueue()).to(directExchange()).with(ROUNTING_KEY);
	}

	/**
	 * 配置消息发送模板
	 * 
	 * @return
	 */
	@Bean
	public RabbitTemplate createRabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

		// 确认消息已发送到交换机
		rabbitTemplate.setConfirmCallback(new ConfirmCallback() {

			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (!ack) {
					log.error("发送到交换机失败！原因：{}", cause);
				}
			}

		});

		// 强制调用回调方法
		rabbitTemplate.setMandatory(true);

		// 确认消息已发送到队列
		rabbitTemplate.setReturnCallback(new ReturnCallback() {

			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
					String routingKey) {
				log.error("绑定到队列异常，消息：{}，回应码：{}，回应文本：{}，交换机：{}，路由键：{}", message, replyCode, replyText, exchange,
						routingKey);
			}

		});

		return rabbitTemplate;
	}

}
