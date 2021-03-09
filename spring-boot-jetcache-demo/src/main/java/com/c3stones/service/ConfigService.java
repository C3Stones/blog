package com.c3stones.service;

import com.c3stones.entity.Config;

/**
 * 配置Service
 * 
 * @author CL
 *
 */
public interface ConfigService {

	/**
	 * 新增配置
	 * 
	 * @param config 配置类
	 * @return
	 */
	void add(Config config);

	/**
	 * 获取配置值
	 * 
	 * @param configKey 配置键
	 * @return
	 */
	String get(String configKey);

	/**
	 * 更新配置
	 * 
	 * @param config 配置类
	 */
	void update(Config config);

	/**
	 * 删除配置
	 * 
	 * @param configKey 配置键
	 */
	void delete(String configKey);

}
