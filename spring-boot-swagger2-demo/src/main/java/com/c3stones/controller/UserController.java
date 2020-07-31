package com.c3stones.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户Controller
 * 
 * @author CL
 *
 */
@Api(tags = "User / 用户信息")
@RestController
@RequestMapping(value = "/user")
public class UserController {

	/**
	 * 获取用户信息
	 * 
	 * @param user 用户信息
	 * @return
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取用户信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", required = true, type = "Long"),
			@ApiImplicitParam(name = "username", value = "用户名称", type = "String"),
			@ApiImplicitParam(name = "age", value = "年龄", type = "Integer") })
	public User get(User user) {
		return new User(1L, "C3Stones", 23);
	}

	/**
	 * 获取用户列表
	 * 
	 * @param user 用户信息
	 * @return
	 */
	@SuppressWarnings("serial")
	@RequestMapping(value = "/listData", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取用户列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", type = "Long"),
			@ApiImplicitParam(name = "username", value = "用户名称", type = "String"),
			@ApiImplicitParam(name = "age", value = "年龄", type = "Integer") })
	public List<User> listData(User user, @ApiIgnore HttpRequest request) {
		return new ArrayList<User>() {
			{
				add(new User(1L, "张三", 23));
				add(new User(2L, "李四", 24));
				add(new User(3L, "王五", 25));
			}
		};
	}
	
}
