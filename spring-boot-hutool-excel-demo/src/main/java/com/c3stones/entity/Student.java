package com.c3stones.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生实体
 * 
 * @author CL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

	/**
	 * 学号
	 */
	private String sno;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 年龄
	 */
	private Integer age;

	/**
	 * 性别
	 */
	private String gender;

	/**
	 * 籍贯
	 */
	private String nativePlace;

	/**
	 * 入学时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date enrollmentTime;

}
