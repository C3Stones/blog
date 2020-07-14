package com.c3stones.security.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.c3stones.security.entity.SysUserDetails;
import com.c3stones.security.utils.JWTTokenUtil;
import com.c3stones.utils.ResponseUtils;

/**
 * 登录成功处理类
 * 
 * @author CL
 *
 */
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();
		String token = JWTTokenUtil.createAccessToken(sysUserDetails);
		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put("token", token);
		ResponseUtils.responseJson(response, ResponseUtils.response(200, "登录成功", tokenMap));
	}
}
