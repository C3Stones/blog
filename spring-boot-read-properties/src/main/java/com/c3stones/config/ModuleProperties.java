package com.c3stones.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 模块参数
 * 
 * @author CL
 *
 */
@Component
@ConfigurationProperties(prefix = "sys")
@PropertySource(value = { "classpath:application.yml" })
public class ModuleProperties {

	/**
	 * 模块包装类
	 */
	private List<Module> module;

	public List<Module> getModule() {
		return module;
	}

	public void setModule(List<Module> module) {
		this.module = module;
	}

	/**
	 * 模块 Entity
	 * 
	 * @author CL
	 *
	 */
	public static class Module {

		/**
		 * 编码
		 */
		private String code;

		/**
		 * 是否启用
		 */
		private Boolean enable;

		/**
		 * 版本
		 */
		private Integer version;

		public Module() {
			super();
		}

		public Module(String code, Boolean enable, Integer version) {
			super();
			this.code = code;
			this.enable = enable;
			this.version = version;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Boolean getEnable() {
			return enable;
		}

		public void setEnable(Boolean enable) {
			this.enable = enable;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

		@Override
		public String toString() {
			return "模块编码：" + code + " - 是否启用：" + enable + " - 版本：" + version;
		}

	}
}
