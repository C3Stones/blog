package com.c3stones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * 启动类
 * 
 * <p>
 * @EnableRetry ： 开启重试机制
 * </p>
 * 
 * @author CL
 *
 */
@SpringBootApplication
@EnableRetry
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
