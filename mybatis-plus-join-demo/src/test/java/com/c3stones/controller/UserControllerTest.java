package com.c3stones.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 单元测试
 *
 * @author CL
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

	@Autowired
	private UserController userController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	/**
	 * /user/listData 接口测试
	 */
	@Test
	public void listDataTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/listData")).andExpect(status().isOk()) // 增加断言
				.andExpect(jsonPath("$[0].id").value(1)) // 增加断言
				.andExpect(jsonPath("$[1].id").value(2)) // 增加断言
//                .andDo(print())                                                     // 打印结果
				.andReturn();
	}

	/**
	 * /user/pageData 接口测试
	 */
	@Test
	public void pageDataTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/pageData").param("current", "1").param("size", "10"))
				.andExpectAll(status().isOk()).andExpect(jsonPath("$.total").value(2))
//                .andDo(print())                                                     // 打印结果
				.andReturn();

		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/pageData").param("current", "1").param("size", "10").param("id", "1"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.total").value(1))
				.andExpect(jsonPath("$.records[0].id").value(1))
//                .andDo(print())                                                     // 打印结果
				.andReturn();

		mockMvc.perform(MockMvcRequestBuilders.get("/user/pageData").param("current", "1").param("size", "10")
				.param("username", "张")).andExpect(status().isOk()).andExpect(jsonPath("$.total").value(1))
				.andExpect(jsonPath("$.records[0].username").value("张三"))
//                .andDo(print())                                                     // 打印结果
				.andReturn();
	}

	/**
	 * /user/default 接口测试
	 */
	@Test
	public void userDefaultShippingAddressTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/default").param("id", "1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.def").value("t"))
//                .andDo(print())                                                     // 打印结果
				.andReturn();

		mockMvc.perform(MockMvcRequestBuilders.get("/user/default").param("id", "999"))
				.andExpect(status().is2xxSuccessful())
//                .andDo(print())                                                     // 打印结果
				.andReturn();
	}

	/**
	 * /user/pageAddressData 接口测试
	 */
	@Test
	public void pageUserShippingAddressDataTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/pageAddressData").param("current", "1").param("size", "10"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.total").value(3))
//                .andDo(print())                                                     // 打印结果
				.andReturn();
	}

	/**
	 * /user/countAddress 接口测试
	 */
	@Test
	public void countUserShippingAddressTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/countAddress").param("id", "1")).andExpect(status().isOk())
				.andExpect(content().string("2"))
//                .andDo(print())                                                     // 打印结果
				.andReturn();

		mockMvc.perform(MockMvcRequestBuilders.get("/user/countAddress").param("id", "2")).andExpect(status().isOk())
				.andExpect(content().string("1"))
//                .andDo(print())                                                     // 打印结果
				.andReturn();
	}

}
