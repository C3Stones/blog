package com.c3stones.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.c3stones.sys.entity.Auth;
import com.c3stones.sys.entity.Role;
import com.c3stones.sys.entity.RoleAuth;
import com.c3stones.sys.service.AuthService;

import cn.hutool.json.JSONUtil;

/**
 * 系统权限Controller
 * 
 * @author CL
 *
 */
@Controller
@RequestMapping(value = "auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/list', 'sys:auth:view')")
	@RequestMapping(value = "list")
	public String list() {
		return "pages/sys/authList";
	}

	/**
	 * 查询列表数据
	 * 
	 * @param auth    系统权限
	 * @param current 当前页
	 * @param size    每页显示条数
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/listData', 'sys:auth:view')")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Response<Page<Auth>> listData(Auth auth, @RequestParam(name = "page") long current,
			@RequestParam(name = "limit") long size) {
		Page<Auth> page = authService.listData(auth, current, size);
		return Response.success(page);
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/add', 'sys:auth:edit')")
	@RequestMapping(value = "add")
	public String add() {
		return "pages/sys/authAdd";
	}

	/**
	 * 保存
	 * 
	 * @param auth 系统权限
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/save', 'sys:auth:edit')")
	@RequestMapping(value = "save")
	@ResponseBody
	public Response<Boolean> save(Auth auth) {
		boolean result = authService.save(auth);
		return Response.success(result);
	}

	/**
	 * 修改
	 * 
	 * @param auth  系统权限
	 * @param model
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/edit', 'sys:auth:edit')")
	@RequestMapping(value = "edit")
	public String edit(Auth auth, Model model) {
		Assert.notNull(auth.getId(), "ID不能为空");
		model.addAttribute("auth", authService.getById(auth.getId()));
		return "pages/sys/authEdit";
	}

	/**
	 * 更新
	 * 
	 * @param auth 系统权限
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/update', 'sys:auth:edit')")
	@RequestMapping(value = "update")
	@ResponseBody
	public Response<Boolean> update(Auth auth) {
		Assert.notNull(auth.getId(), "ID不能为空");
		boolean result = authService.updateById(auth);
		return Response.success(result);
	}

	/**
	 * 删除
	 * 
	 * @param auth 系统权限
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/delete', 'sys:auth:edit')")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Response<Boolean> delete(Auth auth) {
		Assert.notNull(auth.getId(), "ID不能为空");
		boolean result = authService.removeById(auth.getId());
		return Response.success(result);
	}

	/**
	 * 绑定角色
	 * 
	 * @param auth  系统权限
	 * @param model
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/bind', 'sys:auth:view')")
	@RequestMapping(value = "bind")
	public String bind(Auth auth, Model model) {
		Assert.notNull(auth.getId(), "ID不能为空");
		model.addAttribute("auth", auth);
		return "pages/sys/authBind";
	}

	/**
	 * 查询绑定角色列表数据
	 * 
	 * @param roleAuth 角色权限
	 * @param role     系统角色
	 * @param current  当前页
	 * @param size     每页显示条数
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/roleListData', 'sys:auth:view')")
	@RequestMapping(value = "roleListData")
	@ResponseBody
	public Response<Page<Role>> roleListData(RoleAuth roleAuth, Role role, @RequestParam(name = "page") long current,
			@RequestParam(name = "limit") long size) {
		Assert.notNull(roleAuth.getAuthId(), "权限ID不能为空");
		Page<Role> page = authService.roleListData(roleAuth, role, current, size);
		return Response.success(page);
	}

	/**
	 * 取消角色
	 * 
	 * @param roleAuth 角色权限
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/remove', 'sys:auth:edit')")
	@RequestMapping(value = "remove")
	@ResponseBody
	public Response<Boolean> remove(RoleAuth roleAuth) {
		Assert.notNull(roleAuth.getAuthId(), "权限ID不能为空");
		Assert.notNull(roleAuth.getRoleId(), "角色ID不能为空");
		QueryWrapper<RoleAuth> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("auth_id", roleAuth.getAuthId());
		queryWrapper.eq("role_id", roleAuth.getRoleId());
		boolean result = roleAuth.delete(queryWrapper);
		return Response.success(result);
	}

	/**
	 * 添加用户
	 * 
	 * @param auth  系统权限
	 * @param model
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/addRole', 'sys:auth:edit')")
	@RequestMapping(value = "addRole")
	public String addRole(Auth auth, Model model) {
		Assert.notNull(auth.getId(), "ID不能为空");
		model.addAttribute("auth", auth);
		return "pages/sys/authAddRole";
	}

	/**
	 * 获取角色权限数据
	 * 
	 * @param auth 系统权限
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/getRoleAuthData', 'sys:auth:view')")
	@RequestMapping(value = "getRoleAuthData")
	@ResponseBody
	public Response<Map<String, Object>> getRoleAuthData(Auth auth) {
		Assert.notNull(auth.getId(), "ID不能为空");
		// 查询所有角色
		List<Role> roleAllList = new Role().selectAll();
		// 查询已经选择的角色Id
		QueryWrapper<RoleAuth> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("auth_id", auth.getId());
		List<RoleAuth> roleAuthList = new RoleAuth().selectList(queryWrapper);
		List<Integer> selectedList = roleAuthList.stream().map(RoleAuth::getRoleId).collect(Collectors.toList());

		Map<String, Object> resultMap = new HashMap<>(2);
		resultMap.put("roleAllList", roleAllList);
		resultMap.put("selectedList", selectedList);
		return Response.success(resultMap);
	}

	/**
	 * 保存角色权限信息
	 * 
	 * @param roleAuth 角色权限
	 * @param dataJson 权限绑定角色数据JSON
	 * @return
	 */
	@PreAuthorize(value = "hasPermission('/auth/saveRoleAuth', 'sys:auth:edit')")
	@RequestMapping(value = "saveRoleAuth")
	@ResponseBody
	public Response<Boolean> saveRoleAuth(RoleAuth roleAuth, String dataJson) {
		boolean result = true;
		// 格式化数据，并记录该权限选择的角色ID
		List<TransferDataVo> dataList = JSONUtil.toList(JSONUtil.parseArray(dataJson), TransferDataVo.class);
		List<Integer> resultList = dataList.stream().map(t -> {
			return (int) t.getValue();
		}).collect(Collectors.toList());

		// 查询已经选择的用户ID
		QueryWrapper<RoleAuth> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("auth_id", roleAuth.getAuthId());
		List<RoleAuth> roleAuthList = roleAuth.selectList(queryWrapper);
		List<Integer> selectedList = roleAuthList.stream().map(RoleAuth::getRoleId).collect(Collectors.toList());

		// 过滤出需要移除的角色
		List<Integer> removeIds = null;
		// 过滤出需要新增的角色
		List<Integer> addIds = null;
		if (ListUtils.isEmpty(resultList)) {
			removeIds = selectedList;
		} else if (!ListUtils.isEmpty(selectedList)) {
			removeIds = selectedList.stream().filter(id -> (!resultList.contains(id))).collect(Collectors.toList());
			addIds = resultList.stream().filter(id -> (!selectedList.contains(id))).collect(Collectors.toList());
		} else {
			addIds = resultList;
		}

		// 移除角色
		if (!ListUtils.isEmpty(removeIds)) {
			queryWrapper.in("role_id", removeIds);
			result = roleAuth.delete(queryWrapper);
		}

		// 新增角色
		if (!ListUtils.isEmpty(addIds) && result) {
			List<Boolean> insertResults = new ArrayList<>(addIds.size());
			addIds.forEach(id -> {
				roleAuth.setRoleId(id);
				insertResults.add(roleAuth.insert());
			});
			if (insertResults.stream().filter(r -> !r).count() > 0) {
				result = false;
			}
		}
		return Response.success("提交" + (result ? "成功" : "失败"), result);
	}

}
