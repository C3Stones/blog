package com.c3stones.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.entity.User;
import com.c3stones.mapper.UserMapper;
import com.c3stones.service.UserService;

/**
 * 用户Service实现
 * 
 * @author CL
 *
 */
@Service
@DS("master")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	/**
	 * 查询全部用户
	 * 
	 * @return 用户列表
	 */
	@Override
	public List<User> selectMasterAll() {
		return baseMapper.selectList(new QueryWrapper<>());
	}

	/**
	 * 查询全部用户
	 * 
	 * @return 用户列表
	 */
	@DS("slave_1")
	@Override
	public List<User> selectSlave1All() {
		return baseMapper.selectList(new QueryWrapper<>());
	}

	/**
	 * 查询全部用户
	 * 
	 * @return 用户列表
	 */
	@DS("slave_2")
	@Override
	public List<User> selectSlave2All() {
		return baseMapper.selectList(new QueryWrapper<>());
	}

}
