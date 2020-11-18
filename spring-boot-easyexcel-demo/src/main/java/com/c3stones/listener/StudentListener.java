package com.c3stones.listener;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.c3stones.entity.Student;

import lombok.Getter;

/**
 * 学生读取类
 * 
 * @author CL
 *
 */
public class StudentListener extends AnalysisEventListener<Student> {

	@Getter
	private List<Student> studentList = new ArrayList<Student>();

	public StudentListener() {
		super();
		studentList.clear();
	}

	/**
	 * 每一条数据解析都会调用
	 */
	@Override
	public void invoke(Student student, AnalysisContext context) {
		studentList.add(student);
	}

	/**
	 * 所有数据解析完成都会调用
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		studentList.forEach(System.out::println);
	}

}
