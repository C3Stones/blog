package com.c3stones.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 解决方案02-Collections
 * 
 * @author CL
 *
 */
public class Solution02 {

	public static void main(String[] args) {
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
//		Set<String> set = Collections.synchronizedSet(new HashSet<String>());
//		Map<Integer, String> map = Collections.synchronizedMap(new HashMap<>());

		// 启动30个线程
		for (int i = 0; i < 30; i++) {
			new Thread(() -> {
				list.add(UUID.randomUUID().toString().substring(0, 8));
				System.out.println(list);
			}, String.valueOf(i)).start();
		}
	}

}
