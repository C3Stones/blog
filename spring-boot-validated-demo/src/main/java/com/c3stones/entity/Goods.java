package com.c3stones.entity;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品
 * 
 * @author CL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {

	/**
	 * 商品ID
	 */
	@NotNull(message = "{Goods.id.NotNull}")
	private Integer id;

	/**
	 * 商品名称
	 */
	private String name;

}
