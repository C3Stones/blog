package com.c3stones.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.entity.Student;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/**
 * 文件Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "file")
public class FileController {

	/**
	 * 读取Excel
	 * 
	 * @return
	 */
	@RequestMapping(value = "readExcel")
	public List<Student> readExcel() {
		String fileName = "C:\\Users\\Administrator\\Desktop\\学生花名册.xlsx";
		ExcelReader reader = ExcelUtil.getReader(fileName);
		reader.addHeaderAlias("学号", "sno");
		reader.addHeaderAlias("姓名", "name");
		reader.addHeaderAlias("年龄", "age");
		reader.addHeaderAlias("性别", "gender");
		reader.addHeaderAlias("籍贯", "nativePlace");
		reader.addHeaderAlias("入学时间", "enrollmentTime");
		List<Student> studentList = reader.readAll(Student.class);
		return studentList;
	}

	/**
	 * 写入Excel
	 * 
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("serial")
	@RequestMapping(value = "writeExcel")
	public Boolean writeExcel() throws ParseException {
		String fileName = "C:\\Users\\Administrator\\Desktop\\学生花名册2.xlsx";
		List<Student> studentList = new ArrayList<Student>() {
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				add(new Student("2001", "张三2", 23, "男", "陕西西安", dateFormat.parse("2020-09-01")));
				add(new Student("2002", "李四2", 22, "女", "陕西渭南", dateFormat.parse("2020-09-01")));
			}
		};

		ExcelWriter writer = ExcelUtil.getWriter(fileName);
		writer.addHeaderAlias("sno", "学号");
		writer.addHeaderAlias("name", "姓名");
		writer.addHeaderAlias("age", "年龄");
		writer.addHeaderAlias("gender", "性别");
		writer.addHeaderAlias("nativePlace", "籍贯");
		writer.addHeaderAlias("enrollmentTime", "入学时间");

		CellRangeAddressList regions = new CellRangeAddressList(1, studentList.size(), 3, 3);
		writer.addSelect(regions, "男", "女");
		writer.setColumnWidth(5, 15);

		writer.write(studentList, true);
		writer.close();
		return true;
	}

}