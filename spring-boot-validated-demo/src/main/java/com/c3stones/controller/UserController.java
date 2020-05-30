package com.c3stones.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.dto.UserSaveDto;
import com.c3stones.entity.User;
import com.c3stones.entity.User.UserSaveGroup;
import com.c3stones.entity.User.UserUpdateGroup;

/**
 * 用户Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/get")
	public String get(@RequestParam("id") @Min(value = 1L, message = "id必须大于0") Integer id) {
		logger.info("获取用户信息，id：" + id);
		return "get success";
	}

	@PostMapping("/save")
	public String save(@Valid UserSaveDto saveDto) {
		logger.info("保存用户信息：", saveDto.toString());
		return "save success";
	}

	@PostMapping("/saveUser")
	public String saveUser(@Validated({ UserSaveGroup.class }) User user) {
		logger.info("保存用户信息：", user.toString());
		return "saveUser success";
	}

	@PutMapping("/updateUser")
	public String updateUser(@Validated({ UserUpdateGroup.class }) User user) {
		logger.info("更新用户信息：", user.toString());
		return "updateUser success";
	}

}