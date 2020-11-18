package com.c3stones.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字段信息
 * 
 * @author CL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dict {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 父级编码
	 */
	private String parentCode;

	/**
	 * 字典Key
	 */
	private String dictKey;

	/**
	 * 字典Value
	 */
	private String dictValue;

}
