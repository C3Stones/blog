package com.c3stones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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

	/**
	 * 交换机名称
	 */
	public static final String EXCHANGE_NAME = "c3stones.direct";

	/**
	 * 路由键
	 */
	public static final String ROUNTING_KEY = "test";

	/**
	 * 队列名称
	 */
	public static final String QUEUE_NAME = "test.queue";

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

}
