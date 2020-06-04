package com.c3stones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 * 
 * @author CL
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsulConfigApplication.class, args);
	}

}
