package com.c3stones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
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
	public static final String EXCHANGE_NAME = "c3stones.fanout";

	/**
	 * 队列1名称
	 */
	public static final String QUEUE_NAME_1 = "test1.fanout.queue";

	/**
	 * 队列2名称
	 */
	public static final String QUEUE_NAME_2 = "test2.fanout.queue";

	/**
	 * 队列3名称
	 */
	public static final String QUEUE_NAME_3 = "test3.fanout.queue";

	/**
	 * 配置Direct交换机
	 * 
	 * @return
	 */
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(EXCHANGE_NAME);
	}

	/**
	 * 配置队列1
	 * 
	 * @return
	 */
	@Bean
	public Queue test1Queue() {
		return new Queue(QUEUE_NAME_1);
	}

	/**
	 * 配置队列2
	 * 
	 * @return
	 */
	@Bean
	public Queue test2Queue() {
		return new Queue(QUEUE_NAME_2);
	}

	/**
	 * 配置队列3
	 * 
	 * @return
	 */
	@Bean
	public Queue test3Queue() {
		return new Queue(QUEUE_NAME_3);
	}

	/**
	 * 将队列1与交换机绑定
	 * 
	 * @return
	 */
	@Bean
	public Binding bindingQueue1() {
		return BindingBuilder.bind(test1Queue()).to(fanoutExchange());
	}

	/**
	 * 将队列2与交换机绑定
	 * 
	 * @return
	 */
	@Bean
	public Binding bindingQueue2() {
		return BindingBuilder.bind(test2Queue()).to(fanoutExchange());
	}

	/**
	 * 将队列3与交换机绑定
	 * 
	 * @return
	 */
	@Bean
	public Binding bindingQueue3() {
		return BindingBuilder.bind(test3Queue()).to(fanoutExchange());
	}

}
