package com.c3stones.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置工具类
 * 
 * @author CL
 *
 */
@Component
@EnableSwagger2
public class Swagger2ConfigUtils {

	/**
	 * 配置模块
	 * 
	 * @param moduleCode  模块Code
	 * @param moduleName  模块名称
	 * @param basePackage 基础包（多个）
	 * @return
	 */
	public static Docket docket(String moduleCode, String moduleName, String... basePackage) {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo(moduleName)).groupName(moduleCode).select()
				.apis(Predicates.and(RequestHandlerSelectors.withMethodAnnotation(ResponseBody.class),
						basePackage(basePackage)))
				.build();
	}

	/**
	 * 声明基础包
	 * 
	 * @param basePackage 基础包路径
	 * @return
	 */
	public static Predicate<RequestHandler> basePackage(final String... basePackage) {
		return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
	}

	/**
	 * 校验基础包
	 * 
	 * @param basePackage 基础包路径
	 * @return
	 */
	private static Function<Class<?>, Boolean> handlerPackage(final String... basePackage) {
		return input -> {
			for (String strPackage : basePackage) {
				boolean isMatch = input.getPackage().getName().startsWith(strPackage);
				if (isMatch) {
					return true;
				}
			}
			return false;
		};
	}

	/**
	 * 检验基础包实例
	 * 
	 * @param requestHandler 请求处理类
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static Optional<? extends Class<?>> declaringClass(RequestHandler requestHandler) {
		return Optional.fromNullable(requestHandler.declaringClass());
	}

	/**
	 * 配置在线文档的基本信息
	 * 
	 * @return
	 */
	private static ApiInfo apiInfo(String moduleName) {
		return new ApiInfoBuilder().title(moduleName)
				.contact(new Contact("Powered By C3Stones", "https://www.cnblogs.com/cao-lei", null))
				.termsOfServiceUrl("https://www.cnblogs.com/cao-lei/").version("V1.0").build();
	}

}
