package com.c3stones.demo;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 解决方案03-CopyOnWrite
 * 
 * @author CL
 *
 */
public class Solution03 {

	public static void main(String[] args) {
		List<String> list = new CopyOnWriteArrayList<String>();
//		Set<String> set = new CopyOnWriteArraySet<String>();
//		Map<Integer, String> map = new ConcurrentHashMap<Integer, String>();

		// 启动30个线程
		for (int i = 0; i < 30; i++) {
			new Thread(() -> {
				list.add(UUID.randomUUID().toString().substring(0, 8));
				System.out.println(list);
			}, String.valueOf(i)).start();
		}
	}

}
