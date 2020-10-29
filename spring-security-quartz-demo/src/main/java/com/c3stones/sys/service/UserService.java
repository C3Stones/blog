package com.c3stones.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.c3stones.sys.entity.User;

/**
 * 系统用户Service
 * 
 * @author CL
 *
 */
public interface UserService extends IService<User> {

	/**
	 * 查询列表数据
	 * 
	 * @param user    系统用户
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	public Page<User> listData(User user, long current, long size);

	/**
	 * 检验用户名称是否唯一
	 * 
	 * @param userName 用户名称
	 * @return
	 */
	public Boolean checkUserName(String userName);

}
