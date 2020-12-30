package com.c3stones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置类
 * 
 * @author CL
 *
 */
@Configuration
public class WebSocketConfig {

	/**
	 * 注入ServerEndpointExporter
	 * <p>
	 * 该Bean会自动注册添加@ServerEndpoint注解的WebSocket端点
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

}
