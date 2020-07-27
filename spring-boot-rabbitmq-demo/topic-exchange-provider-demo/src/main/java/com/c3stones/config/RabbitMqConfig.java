package com.c3stones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
	public static final String EXCHANGE_NAME = "c3stones.topic";

	/**
	 * 绑定键1
	 */
	public static final String BINDING_KEY_1 = "topic.key1";

	/**
	 * 绑定键2
	 */
	public static final String BINDING_KEY_2 = "topic.key2";

	/**
	 * 绑定键前缀，即以topic.开头的键值都会被监听
	 */
	public static final String BINDING_KEY_PREFIX = "topic.#";

	/**
	 * 队列1名称
	 */
	public static final String QUEUE_NAME_1 = "test1.queue";

	/**
	 * 队列2名称
	 */
	public static final String QUEUE_NAME_2 = "test2.queue";

	/**
	 * 配置Direct交换机
	 * 
	 * @return
	 */
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(EXCHANGE_NAME);
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
	 * 将队列1与交换机通过绑定键1绑定
	 * 
	 * @return
	 */
	@Bean
	public Binding bindingQueue1() {
		return BindingBuilder.bind(test1Queue()).to(topicExchange()).with(BINDING_KEY_1);
	}

	/**
	 * 将队列2与交换机通过绑定键前缀绑定
	 * 
	 * @return
	 */
	@Bean
	public Binding bindingQueue2() {
		return BindingBuilder.bind(test2Queue()).to(topicExchange()).with(BINDING_KEY_PREFIX);
	}

}
