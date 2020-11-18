package com.c3stones.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品信息
 * 
 * @author CL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品价格
	 */
	private Double price;

}
