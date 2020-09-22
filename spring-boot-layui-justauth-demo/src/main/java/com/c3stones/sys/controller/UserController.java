package com.c3stones.sys.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c3stones.common.response.Response;
import com.c3stones.sys.entity.User;
import com.c3stones.sys.service.UserService;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 系统用户Controller
 * 
 * @author CL
 *
 */
@Controller
@RequestMapping(value = "user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "pages/userList";
	}

	/**
	 * 查询列表数据
	 * 
	 * @param user    系统用户
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Response<Page<User>> listData(User user, @RequestParam(name = "page") long current,
			@RequestParam(name = "limit") long size) {
		Page<User> page = userService.listData(user, current, size);
		return Response.success(page);
	}

	/**
	 * 删除
	 * 
	 * @param user 系统用户
	 * @return
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Response<Boolean> delete(User user) {
		Assert.notNull(user.getId(), "ID不能为空");
		boolean result = userService.removeById(user.getId());
		return Response.success(result);
	}

	/**
	 * 修改
	 * 
	 * @param user  系统用户
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit")
	public String edit(User user, Model model) {
		Assert.notNull(user.getId(), "ID不能为空");
		model.addAttribute("user", userService.getById(user.getId()));
		return "pages/userEdit";
	}

	/**
	 * 检验用户名称是否唯一
	 * 
	 * @param userName 用户名称
	 * @return
	 */
	@RequestMapping(value = "check")
	@ResponseBody
	public Response<Boolean> checkUserName(@NotNull String username) {
		Boolean checkResult = userService.checkUserName(username);
		return Response.success(checkResult);
	}

	/**
	 * 更新
	 * 
	 * @param user 系统用户
	 * @return
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	public Response<Boolean> update(User user) {
		Assert.notNull(user.getId(), "ID不能为空");
		boolean result = userService.updateById(user);
		return Response.success(result);
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	@RequestMapping(value = "add")
	public String add() {
		return "pages/userAdd";
	}
	
	/**
	 * 保存
	 * 
	 * @param user 系统用户
	 * @return
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public Response<Boolean> save(User user) {
		user.setPassword(BCrypt.hashpw(user.getPassword()));
		boolean result = userService.save(user);
		return Response.success(result);
	}

}