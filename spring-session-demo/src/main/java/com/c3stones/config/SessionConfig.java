package com.c3stones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Session配置类
 * 
 * @EnableRedisHttpSession：启用支持Redis存储Session</br>
 * 												maxInactiveIntervalInSeconds：Session最大过期时间
 * 
 * @author CL
 *
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class SessionConfig {

	@Value("${spring.redis.database}")
	private Integer database;

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private Integer port;

	@Value("${spring.redis.password}")
	private String password;

	/**
	 * 注入Redis连接工厂
	 * 
	 * @return
	 */
	@Bean
	public JedisConnectionFactory connectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
		configuration.setHostName(host);
		configuration.setPort(port);
		configuration.setPassword(password);
		configuration.setDatabase(database);
		return new JedisConnectionFactory(configuration);
	}

}