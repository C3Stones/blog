package com.c3stones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 产品参数
 * 
 * @author CL
 *
 */
@Component
public class ProductProperties {

	/**
	 * 产品名称
	 */
	@Value("${product.name}")
	private String productName;

	/**
	 * 作者
	 */
	@Value("${product.author}")
	private String productAuthor;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductAuthor() {
		return productAuthor;
	}

	public void setProductAuthor(String productAuthor) {
		this.productAuthor = productAuthor;
	}

}
