package com.c3stones.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.c3stones.entity.User;

/**
 * 用户Service
 * 
 * @author CL
 *
 */
public interface UserService extends IService<User> {

	/**
	 * 查询全部用户
	 * 
	 * @return 用户列表
	 */
	List<User> selectMasterAll();

	/**
	 * 查询全部用户
	 * 
	 * @return 用户列表
	 */
	List<User> selectSlave1All();

	/**
	 * 查询全部用户
	 * 
	 * @return 用户列表
	 */
	List<User> selectSlave2All();

}
