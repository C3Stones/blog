package com.c3stones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

/**
 * 启动类
 * 
 * @author CL
 *
 */
@SpringBootApplication
@EnableMethodCache(basePackages = "com.c3stones")
@EnableCreateCacheAnnotation
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

}
