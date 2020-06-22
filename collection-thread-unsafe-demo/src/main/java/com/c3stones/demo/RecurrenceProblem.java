package com.c3stones.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 复现问题
 * 
 * @author CL
 *
 */
public class RecurrenceProblem {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();

		// 启动30个线程
		for (int i = 0; i < 30; i++) {
			new Thread(() -> {
				list.add(UUID.randomUUID().toString().substring(0, 8));
				System.out.println(list);
			}, String.valueOf(i)).start();
		}
	}

}
