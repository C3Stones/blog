package com.c3stones.entity;

import java.io.Serializable;

/**
 * 配置类
 * 
 * @author CL
 *
 */
public class Config implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 配置键
	 */
	private String configKey;

	/**
	 * 配置值
	 */
	private String configValue;

	public Config() {
		super();
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

}
