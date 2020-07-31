package com.c3stones.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户信息
 * 
 * @author CL
 *
 */
@Data
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
