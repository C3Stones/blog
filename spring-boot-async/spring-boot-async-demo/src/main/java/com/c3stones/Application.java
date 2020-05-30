package com.c3stones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * 
 * @author CL
 *
 */
@SpringBootApplication
//@EnableAsync // 开启异步支持
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
