package com.c3stones.entity;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

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
@HeadRowHeight(20)
@ColumnWidth(20)
@ContentRowHeight(15)
public class Student {

	@ExcelProperty(index = 0, value = "学号")
	private String sno;

	@ExcelProperty(index = 1, value = "姓名")
	private String name;

	@ExcelProperty(index = 2, value = "年龄")
	private Integer age;

	@ExcelProperty(index = 3, value = "性别")
	private String gender;

	@ExcelProperty(index = 4, value = "籍贯")
	private String nativePlace;

	@ExcelProperty(index = 5, value = "入学时间")
	private Date enrollmentTime;

}