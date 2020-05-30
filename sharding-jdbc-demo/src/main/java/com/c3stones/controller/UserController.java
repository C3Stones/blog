package com.c3stones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.entity.User;
import com.c3stones.service.UserService;

/**
 * 用户Controller
 * 
 * @author CL
 *
 */
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "save")
	public boolean save(User user) {
		return userService.save(user);
	}

	@GetMapping(value = "list")
	public List<User> findList() {
		return userService.findList();
	}
}
