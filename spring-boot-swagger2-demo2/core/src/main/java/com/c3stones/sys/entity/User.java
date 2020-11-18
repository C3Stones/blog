package com.c3stones.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 * 
 * @author CL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	/**
	 * 用户ID
	 */
	private Long id;

	/**
	 * 用户名称
	 */
	private String username;

	/**
	 * 年龄
	 */
	private Integer age;

}
