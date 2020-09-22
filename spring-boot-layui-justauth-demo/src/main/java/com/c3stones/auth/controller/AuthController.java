package com.c3stones.auth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.c3stones.auth.entity.JustAuthUser;
import com.c3stones.auth.service.JustAuthUserService;
import com.c3stones.common.response.Response;
import com.c3stones.sys.entity.User;
import com.c3stones.sys.service.UserService;
import com.xkcoding.justauth.AuthRequestFactory;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;

/**
 * 授权Controller
 * 
 * @author CL
 *
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
public class AuthController {

	@Autowired
	private AuthRequestFactory factory;

	@Autowired
	private JustAuthUserService justAuthUserService;

	@Autowired
	private UserService userService;

	/**
	 * 登录
	 * 
	 * @param type     第三方系统类型，例如：gitee/baidu
	 * @param response
	 * @throws IOException
	 */
	@GetMapping(value = "/login/{type}")
	public void login(@PathVariable String type, HttpServletResponse response) throws IOException {
		AuthRequest authRequest = factory.get(type);
		response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
	}

	/**
	 * 登录回调
	 * 
	 * @param type     第三方系统类型，例如：gitee/baidu
	 * @param callback
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{type}/callback")
	public String login(@PathVariable String type, AuthCallback callback, Model model, HttpSession session) {
		AuthRequest authRequest = factory.get(type);
		AuthResponse<AuthUser> response = authRequest.login(callback);
		log.info("登录回调 => {}", JSON.toJSONString(response));

		if (response.ok()) {
			JustAuthUser justAuthUser = new JustAuthUser(response.getData());
			JustAuthUser queryJustAuthUser = justAuthUserService.getById(justAuthUser.getUuid());

			// 无授权用户或者该授权用户与系统用户无绑定关系
			if (queryJustAuthUser == null || queryJustAuthUser.getUserId() == null) {
				justAuthUserService.saveOrUpdate(justAuthUser);
				model.addAttribute("justAuthUser", justAuthUser);
				return "userBinder";
			}
			session.setAttribute("user", userService.getById(queryJustAuthUser.getUserId()));
			return "redirect:/index";
		}
		return "error/403";
	}

	/**
	 * 授权用户和系统用户绑定
	 * 
	 * @param uuid    授权用户Uuid
	 * @param user    系统用户
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userBinder/{uuid}")
	@ResponseBody
	public Response<String> userBinder(@PathVariable String uuid, User user, HttpSession session) {
		if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
			return Response.error("用户名称或密码不能为空");
		}

		boolean checkUserNameResult = userService.checkUserName(user.getUsername());
		if (checkUserNameResult) {
			return Response.error("用户不存在，请输入系统中已存在的用户");
		}

		User queryUser = new User();
		queryUser.setUsername(user.getUsername());
		queryUser = userService.getOne(new QueryWrapper<>(queryUser));
		if (queryUser == null || !StrUtil.equals(queryUser.getUsername(), user.getUsername())
				|| !BCrypt.checkpw(user.getPassword(), queryUser.getPassword())) {
			return Response.error("用户名称或密码错误");
		}

		JustAuthUser justAuthUser = new JustAuthUser();
		justAuthUser.setUuid(uuid);
		justAuthUser.setUserId(queryUser.getId());
		boolean update = justAuthUserService.updateById(justAuthUser);
		log.info("授权用户（uuid）{} 与系统用户（id）绑定 {}", uuid, queryUser.getId());
		if (update) {
			session.setAttribute("user", queryUser);
			return Response.success("登录成功");
		}
		return Response.error("绑定系统用户异常");
	}

}