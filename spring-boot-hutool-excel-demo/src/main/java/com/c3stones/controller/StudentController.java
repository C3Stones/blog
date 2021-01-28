package com.c3stones.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.c3stones.entity.Student;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

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
	 * 
	 * 导出学生信息
	 * 
	 * @param response
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("serial")
	@RequestMapping(value = "export")
	public void exportStudentInfos(HttpServletResponse response) throws ParseException, UnsupportedEncodingException {
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
		ExcelWriter writer = ExcelUtil.getWriter();
		writer.addHeaderAlias("sno", "学号");
		writer.addHeaderAlias("name", "姓名");
		writer.addHeaderAlias("age", "年龄");
		writer.addHeaderAlias("gender", "性别");
		writer.addHeaderAlias("nativePlace", "籍贯");
		writer.addHeaderAlias("enrollmentTime", "入学时间");

		writer.autoSizeColumn(5);
		CellRangeAddressList regions = new CellRangeAddressList(1, studentList.size(), 3, 3);
		writer.addSelect(regions, "男", "女");

		writer.write(studentList, true);

		try {
			writer.flush(response.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

	/**
	 * 导入学生信息
	 * 
	 * @param file
	 * @throws IOException
	 */
	@RequestMapping(value = "import")
	public List<Student> importStudentInfos(MultipartFile file) throws IOException {
		ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
		reader.addHeaderAlias("学号", "sno");
		reader.addHeaderAlias("姓名", "name");
		reader.addHeaderAlias("年龄", "age");
		reader.addHeaderAlias("性别", "gender");
		reader.addHeaderAlias("籍贯", "nativePlace");
		reader.addHeaderAlias("入学时间", "enrollmentTime");
		List<Student> studentList = reader.readAll(Student.class);
		return studentList;
	}

}
