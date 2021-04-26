package com.c3stones.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Value 注解获取参数值 Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "value")
public class ValueAnnotationController {

	@Value("${product.name}")
	private String productName;

	@Value("${product.author}")
	private String productAuthor;

	/**
	 * 获取参数
	 * 
	 * @return
	 */
	@RequestMapping(value = "get")
	public String get() {
		return "产品名称为：" + productName + ", 作者为：" + productAuthor;
	}

}
