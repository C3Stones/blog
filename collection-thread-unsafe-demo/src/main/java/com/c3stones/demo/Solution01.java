package com.c3stones.demo;

import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * 解决方案01-Vector
 * 
 * @author CL
 *
 */
public class Solution01 {

	public static void main(String[] args) {
		List<String> list = new Vector<String>();

		// 启动30个线程
		for (int i = 0; i < 30; i++) {
			new Thread(() -> {
				list.add(UUID.randomUUID().toString().substring(0, 8));
				System.out.println(list);
			}, String.valueOf(i)).start();
		}
	}

}
