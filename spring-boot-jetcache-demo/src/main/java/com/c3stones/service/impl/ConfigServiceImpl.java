package com.c3stones.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.c3stones.entity.Config;
import com.c3stones.service.ConfigService;

/**
 * 配置Service 实现
 * 
 * @author CL
 *
 */
@Service
public class ConfigServiceImpl implements ConfigService {

	private static Map<String, String> configMap = new HashMap<>();

	/**
	 * 新增配置
	 * 
	 * @param config 配置类
	 */
	@Override
	public void add(Config config) {
		System.out.println("新增配置 => " + config.getConfigKey());
		configMap.put(config.getConfigKey(), config.getConfigValue());
	}

	/**
	 * 获取配置值
	 * 
	 * @param configKey 配置键
	 * @return
	 */
	@Override
	@Cached(name = "configCache", key = "#configKey", expire = 3600, cacheType = CacheType.BOTH)
	public String get(String configKey) {
		System.out.println("获取配置值 => " + configKey);
		return configMap.get(configKey);
	}

	/**
	 * 更新配置
	 * 
	 * @param config 配置类
	 */
	@Override
	@CacheUpdate(name = "configCache", key = "#config.configKey", value = "#config.configValue")
	public void update(Config config) {
		System.out.println("更新配置 => " + config.getConfigKey());
		configMap.replace(config.getConfigKey(), config.getConfigValue());
	}

	/**
	 * 删除配置
	 * 
	 * @param configKey 配置键
	 */
	@Override
	@CacheInvalidate(name = "configCache", key = "#configKey")
	public void delete(String configKey) {
		System.out.println("删除配置 => " + configKey);
		configMap.remove(configKey);
	}

}
