package com.c3stones.config;

import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

/**
 * 校验配置类
 * 
 * @author CL
 *
 */
@Configuration
public class ValidationConfiguration {

	/**
	 * 构建 Validator Bean
	 * 
	 * @return Validator 对象
	 */
	@Bean
	public Validator validator(MessageSource messageSource) {
		LocalValidatorFactoryBean validator = ValidationAutoConfiguration.defaultValidator();
		validator.setValidationMessageSource(messageSource);
		return validator;
	}

}
