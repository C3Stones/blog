package com.c3stones.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.c3stones.Application;
import com.c3stones.entity.User;
import com.c3stones.service.UserService;

/**
 * 用户Service测试
 * 
 * @author CL
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class UserServiceImplTest {

	@Autowired
	private UserService userService;

	/**
	 * 测试查询master库用户信息
	 */
	@Test
	public void selectMasterAllTest() {
		List<User> list = userService.selectMasterAll();
		list.forEach(System.out::println);
	}

	/**
	 * 测试查询slave_1库用户信息
	 */
	@Test
	public void selectSlave1AllTest() {
		List<User> list = userService.selectSlave1All();
		list.forEach(System.out::println);
	}

	/**
	 * 测试查询slave_2库用户信息
	 */
	@Test
	public void selectSlave2AllTest() {
		List<User> list = userService.selectSlave2All();
		list.forEach(System.out::println);
	}

}
