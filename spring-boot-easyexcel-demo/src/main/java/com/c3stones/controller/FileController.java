package com.c3stones.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.EasyExcel;
import com.c3stones.entity.Student;
import com.c3stones.listener.StudentListener;

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
		StudentListener studentListener = new StudentListener();
		EasyExcel.read(fileName, Student.class, studentListener).sheet().doRead();
		return studentListener.getStudentList();
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

		EasyExcel.write(fileName, Student.class).sheet("学生信息2").doWrite(studentList);
		return true;
	}

}
