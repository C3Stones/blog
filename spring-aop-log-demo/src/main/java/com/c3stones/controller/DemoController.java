package com.c3stones.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.utils.R;

/**
 * 示例Controller
 * 
 * @author CL
 *
 */
@RestController
public class DemoController {

	/**
	 * 示例方法1
	 * 
	 * @return
	 */
	@RequestMapping(value = "demo1")
	public R<String> demo1(String str) {
		return R.ok("成功返回 -> " + str);
	}

	/**
	 * 示例方法2
	 * 
	 * @return
	 */
	@RequestMapping(value = "demo2")
	public R<String> demo2(String str, int num) {
		return R.failed("失败返回");
	}

	/**
	 * 示例方法3
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "demo3")
	public R<String> demo3() {

		int a = 1 / 0;

		return R.ok("模拟异常");
	}

}
