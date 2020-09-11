package com.c3stones.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.c3stones.common.Response;
import com.c3stones.entity.JustAuthUser;
import com.c3stones.service.JustAuthUserService;
import com.xkcoding.justauth.AuthRequestFactory;

import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
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
@RestController
@RequestMapping("/oauth")
public class AuthController {

	@Autowired
	private AuthRequestFactory factory;

	@Autowired
	private JustAuthUserService justAuthUserService;

	/**
	 * 登录
	 * 
	 * @param type     第三方系统类型，例如：gitee/baidu
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/login/{type}")
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
	@RequestMapping("/{type}/callback")
	public Response login(@PathVariable String type, AuthCallback callback) {
		AuthRequest authRequest = factory.get(type);
		AuthResponse<AuthUser> response = authRequest.login(callback);
		log.info("【response】= {}", JSON.toJSONString(response));

		if (response.ok()) {
			justAuthUserService.saveOrUpdate(new JustAuthUser(response.getData()));
			return Response.success(JSON.toJSONString(response));
		}
		return Response.error(response.getMsg());
	}
	
	/**
	 * 刷新
	 * 
	 * @param type 第三方系统类型，例如：gitee/baidu
	 * @param uuid 用户uuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/refresh/{type}/{uuid}")
	@ResponseBody
	public Response refresh(@PathVariable String type, @PathVariable String uuid) {
		AuthRequest authRequest = factory.get(type);

		JustAuthUser justAuthUser = justAuthUserService.getByUuid(uuid);
		if (null == justAuthUser) {
			return Response.error("用户不存在");
		}

		AuthResponse<AuthToken> response = null;
		try {
			response = authRequest.refresh(justAuthUser.getToken());
			if (response.ok()) {
				justAuthUser.setToken(response.getData());
				justAuthUserService.saveOrUpdate(justAuthUser);
				return Response.success("用户 [" + justAuthUser.getUsername() + "] 的 access token 已刷新！新的 accessToken: "
						+ response.getData().getAccessToken());
			}
			return Response.error("用户 [" + justAuthUser.getUsername() + "] 的 access token 刷新失败！" + response.getMsg());
		} catch (AuthException e) {
			return Response.error(e.getErrorMsg());
		}
	}

	/**
	 * 收回
	 * 
	 * @param type 第三方系统类型，例如：gitee/baidu
	 * @param uuid 用户uuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/revoke/{type}/{uuid}")
	public Response revoke(@PathVariable String type, @PathVariable String uuid) {
		AuthRequest authRequest = factory.get(type);

		JustAuthUser justAuthUser = justAuthUserService.getByUuid(uuid);
		if (null == justAuthUser) {
			return Response.error("用户不存在");
		}

		AuthResponse<AuthToken> response = null;
		try {
			response = authRequest.revoke(justAuthUser.getToken());
			if (response.ok()) {
				justAuthUserService.removeByUuid(justAuthUser.getUuid());
				return Response.success("用户 [" + justAuthUser.getUsername() + "] 的 授权状态 已收回！");
			}
			return Response.error("用户 [" + justAuthUser.getUsername() + "] 的 授权状态 收回失败！" + response.getMsg());
		} catch (AuthException e) {
			return Response.error(e.getErrorMsg());
		}
	}

}