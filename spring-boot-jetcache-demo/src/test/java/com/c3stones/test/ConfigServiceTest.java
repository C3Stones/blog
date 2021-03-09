package com.c3stones.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.c3stones.Application;
import com.c3stones.entity.Config;
import com.c3stones.service.ConfigService;

/**
 * 配置Service 测试类
 * 
 * @author CL
 *
 */
@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConfigServiceTest {

	@Autowired
	private ConfigService configService;

	/**
	 * 测试配置Service
	 */
	@Test
	public void configTest() {
		String configKey = "KEY1";
		Config config = new Config();
		config.setConfigKey(configKey);
		config.setConfigValue("1");
		configService.add(config);

		String configVlue1 = configService.get(configKey);
		String configVlue2 = configService.get(configKey);
		String configVlue3 = configService.get(configKey);
		System.out.println(configKey + " => " + configVlue1);
		System.out.println(configKey + " => " + configVlue2);
		System.out.println(configKey + " => " + configVlue3);

		config.setConfigValue("123");
		configService.update(config);

		String configVlue4 = configService.get(configKey);
		System.out.println(configKey + " => " + configVlue4);

		configService.delete(configKey);

		String configVlue5 = configService.get(configKey);
		System.out.println(configKey + " => " + configVlue5);

		try {
			// 配置文件中配置每一分钟控制台打印统计数据
			Thread.sleep(70000);
		} catch (InterruptedException e) {
		}
	}

}
