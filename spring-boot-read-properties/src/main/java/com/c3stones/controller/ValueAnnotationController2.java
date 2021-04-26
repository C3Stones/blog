package com.c3stones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.config.ProductProperties;

/**
 * Value 注解获取参数值 Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "value")
public class ValueAnnotationController2 {

	@Autowired
	private ProductProperties productProperties;

	/**
	 * 获取参数
	 * 
	 * @return
	 */
	@RequestMapping(value = "get2")
	public String get() {
		return "产品名称为：" + productProperties.getProductName() + ", 作者为：" + productProperties.getProductAuthor();
	}

}
