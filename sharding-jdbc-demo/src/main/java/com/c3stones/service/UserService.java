package com.c3stones.service;

import java.util.List;

import com.c3stones.entity.User;

/**
 * 用户Service
 * 
 * @author CL
 *
 */
public interface UserService {

	/**
	 * 查询用户列表
	 * 
	 * @return
	 */
	List<User> findList();

	/**
	 * 保存用户信息
	 * 
	 * @param user
	 * @return
	 */
	boolean save(User user);

}
