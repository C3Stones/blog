package com.c3stones.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.ListUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c3stones.common.vo.Response;
import com.c3stones.common.vo.TransferDataVo;
import com.c3stones.sys.entity.Role;
import com.c3stones.sys.entity.User;
import com.c3stones.sys.entity.UserRole;
import com.c3stones.sys.service.RoleService;

import cn.hutool.json.JSONUtil;

/**
 * 系统角色Controller
 * 
 * @author CL
 *
 */
@Controller
@RequestMapping(value = "role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/list', 'sys:role:view')")
	@RequestMapping(value = "list")
	public String list() {
		return "pages/sys/roleList";
	}

	/**
	 * 查询列表数据
	 * 
	 * @param role    系统角色
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/listData', 'sys:role:view')")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Response<Page<Role>> listData(Role role, @RequestParam(name = "page") long current,
			@RequestParam(name = "limit") long size) {
		Page<Role> page = roleService.listData(role, current, size);
		return Response.success(page);
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/add', 'sys:role:edit')")
	@RequestMapping(value = "add")
	public String add() {
		return "pages/sys/roleAdd";
	}

	/**
	 * 检验角色编码是否唯一
	 * 
	 * @param roleCode 角色编码
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/check', 'sys:role:edit')")
	@RequestMapping(value = "check")
	@ResponseBody
	public Response<Boolean> checkRoleCode(@NotNull String roleCode) {
		Boolean checkResult = roleService.checkRoleCode(roleCode);
		return Response.success(checkResult);
	}

	/**
	 * 保存
	 * 
	 * @param role 系统角色
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/save', 'sys:role:edit')")
	@RequestMapping(value = "save")
	@ResponseBody
	public Response<Boolean> save(Role role) {
		boolean result = roleService.save(role);
		return Response.success(result);
	}

	/**
	 * 修改
	 * 
	 * @param role  系统角色
	 * @param model
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/edit', 'sys:role:edit')")
	@RequestMapping(value = "edit")
	public String edit(Role role, Model model) {
		Assert.notNull(role.getId(), "ID不能为空");
		model.addAttribute("role", roleService.getById(role.getId()));
		return "pages/sys/roleEdit";
	}

	/**
	 * 更新
	 * 
	 * @param role 系统角色
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/update', 'sys:role:edit')")
	@RequestMapping(value = "update")
	@ResponseBody
	public Response<Boolean> update(Role role) {
		Assert.notNull(role.getId(), "ID不能为空");
		boolean result = roleService.updateById(role);
		return Response.success(result);
	}

	/**
	 * 删除
	 * 
	 * @param role 系统角色
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/delete', 'sys:role:edit')")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Response<Boolean> delete(Role role) {
		Assert.notNull(role.getId(), "ID不能为空");
		boolean result = roleService.removeById(role.getId());
		return Response.success(result);
	}

	/**
	 * 绑定用户
	 * 
	 * @param role  系统角色
	 * @param model
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/bind', 'sys:role:view')")
	@RequestMapping(value = "bind")
	public String bind(Role role, Model model) {
		Assert.notNull(role.getId(), "ID不能为空");
		model.addAttribute("role", role);
		return "pages/sys/roleBind";
	}

	/**
	 * 查询绑定用户列表数据
	 * 
	 * @param userRole 用户角色
	 * @param user     系统用户
	 * @param current  当前页
	 * @param size     每页显示条数
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/userListData', 'sys:role:view')")
	@RequestMapping(value = "userListData")
	@ResponseBody
	public Response<Page<User>> userListData(UserRole userRole, User user, @RequestParam(name = "page") long current,
			@RequestParam(name = "limit") long size) {
		Assert.notNull(userRole.getRoleId(), "角色ID不能为空");
		Page<User> page = roleService.userListData(userRole, user, current, size);
		return Response.success(page);
	}

	/**
	 * 取消用户
	 * 
	 * @param userRole 用户角色
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/remove', 'sys:role:edit')")
	@RequestMapping(value = "remove")
	@ResponseBody
	public Response<Boolean> remove(UserRole userRole) {
		Assert.notNull(userRole.getRoleId(), "角色ID不能为空");
		Assert.notNull(userRole.getUserId(), "用户ID不能为空");
		QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("role_id", userRole.getRoleId());
		queryWrapper.eq("user_id", userRole.getUserId());
		boolean result = userRole.delete(queryWrapper);
		return Response.success(result);
	}

	/**
	 * 添加用户
	 * 
	 * @param role  系统角色
	 * @param model
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/addUser', 'sys:role:edit')")
	@RequestMapping(value = "addUser")
	public String addUser(Role role, Model model) {
		Assert.notNull(role.getId(), "ID不能为空");
		model.addAttribute("role", role);
		return "pages/sys/roleAddUser";
	}

	/**
	 * 获取用户角色数据
	 * 
	 * @param role 系统角色
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/getUserRoleData', 'sys:role:view')")
	@RequestMapping(value = "getUserRoleData")
	@ResponseBody
	public Response<Map<String, Object>> getUserRoleData(Role role) {
		Assert.notNull(role.getId(), "ID不能为空");
		// 查询所有用户
		List<User> userAllList = new User().selectAll();
		// 查询已经选择的用户Id
		QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("role_id", role.getId());
		List<UserRole> userRoleList = new UserRole().selectList(queryWrapper);
		List<Integer> selectedList = userRoleList.stream().map(UserRole::getUserId).collect(Collectors.toList());

		Map<String, Object> resultMap = new HashMap<>(2);
		resultMap.put("userAllList", userAllList);
		resultMap.put("selectedList", selectedList);
		return Response.success(resultMap);
	}

	/**
	 * 保存用户角色信息
	 * 
	 * @param userRole 用户角色
	 * @param dataJson 角色绑定用户数据JSON
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/role/saveUserRole', 'sys:role:edit')")
	@RequestMapping(value = "saveUserRole")
	@ResponseBody
	public Response<Boolean> saveUserRole(UserRole userRole, String dataJson) {
		boolean result = true;
		// 格式化数据，并记录该角色选择的用户ID
		List<TransferDataVo> dataList = JSONUtil.toList(JSONUtil.parseArray(dataJson), TransferDataVo.class);
		List<Integer> resultList = dataList.stream().map(t -> {
			return (int) t.getValue();
		}).collect(Collectors.toList());

		// 查询已经选择的用户ID
		QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("role_id", userRole.getRoleId());
		List<UserRole> userRoleList = userRole.selectList(queryWrapper);
		List<Integer> selectedList = userRoleList.stream().map(UserRole::getUserId).collect(Collectors.toList());

		// 过滤出需要移除的用户
		List<Integer> removeIds = null;
		// 过滤出需要新增的用户
		List<Integer> addIds = null;
		if (ListUtils.isEmpty(resultList)) {
			removeIds = selectedList;
		} else if (!ListUtils.isEmpty(selectedList)) {
			removeIds = selectedList.stream().filter(id -> (!resultList.contains(id))).collect(Collectors.toList());
			addIds = resultList.stream().filter(id -> (!selectedList.contains(id))).collect(Collectors.toList());
		} else {
			addIds = resultList;
		}

		// 移除用户
		if (!ListUtils.isEmpty(removeIds)) {
			queryWrapper.in("user_id", removeIds);
			result = userRole.delete(queryWrapper);
		}

		// 新增用户
		if (!ListUtils.isEmpty(addIds) && result) {
			List<Boolean> insertResults = new ArrayList<>(addIds.size());
			addIds.forEach(id -> {
				userRole.setUserId(id);
				insertResults.add(userRole.insert());
			});
			if (insertResults.stream().filter(r -> !r).count() > 0) {
				result = false;
			}
		}
		return Response.success("提交" + (result ? "成功" : "失败"), result);
	}

}