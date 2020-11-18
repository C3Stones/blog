package com.c3stones.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.c3stones.entity.Student;
import com.c3stones.listener.StudentListener;

/**
 * 学生Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping(value = "student")
public class StudentController {

	/**
	 * 导出学生信息
	 * 
	 * @param response
	 * @param request
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("serial")
	@RequestMapping(value = "export")
	public void exportStudentInfos(HttpServletResponse response, HttpServletRequest request)
			throws IOException, ParseException {
		// 设置响应类型
		response.setContentType("application/vnd.ms-excel");
		// 设置字符编码
		response.setCharacterEncoding("utf-8");
		// 设置响应头信息
		response.setHeader("Content-disposition",
				"attachment;filename*=utf-8''" + URLEncoder.encode("学生花名册", "UTF-8") + ".xlsx");

		List<Student> studentList = new ArrayList<Student>() {
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				add(new Student("1001", "张三", 23, "男", "陕西西安", dateFormat.parse("2020-09-01")));
				add(new Student("1002", "李四", 22, "女", "陕西渭南", dateFormat.parse("2020-09-01")));
			}
		};

		// 写入文件
		EasyExcel.write(response.getOutputStream(), Student.class).sheet("学生信息").doWrite(studentList);
	}

	/**
	 * 导入学生信息
	 * 
	 * @param file
	 * @throws IOException
	 */
	@RequestMapping(value = "import")
	public List<Student> importStudentInfos(MultipartFile file) throws IOException {
		StudentListener studentListener = new StudentListener();
		EasyExcel.read(file.getInputStream(), Student.class, studentListener).sheet().doRead();
		return studentListener.getStudentList();
	}

}
