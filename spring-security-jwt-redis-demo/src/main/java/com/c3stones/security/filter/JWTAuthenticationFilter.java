package com.c3stones.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.c3stones.security.config.JWTConfig;
import com.c3stones.security.entity.SysUserDetails;
import com.c3stones.security.utils.JWTTokenUtils;
import com.c3stones.utils.AccessAddressUtils;
import com.c3stones.utils.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * JWT权限过滤器，用于验证Token是否合法
 * 
 * @author CL
 *
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// 取出Token
		String token = request.getHeader(JWTConfig.tokenHeader);

		if (token != null && token.startsWith(JWTConfig.tokenPrefix)) {
			// 是否在黑名单中
			if (JWTTokenUtils.isBlackList(token)) {
				ResponseUtils.responseJson(response, ResponseUtils.response(505, "Token已失效", "Token已进入黑名单"));
				return;
			}

			// 是否存在于Redis中
			if (JWTTokenUtils.hasToken(token)) {
				String ip = AccessAddressUtils.getIpAddress(request);
				String expiration = JWTTokenUtils.getExpirationByToken(token);
				String username = JWTTokenUtils.getUserNameByToken(token);

				// 判断是否过期
				if (JWTTokenUtils.isExpiration(expiration)) {
					// 加入黑名单
					JWTTokenUtils.addBlackList(token);

					// 是否在刷新期内
					String validTime = JWTTokenUtils.getRefreshTimeByToken(token);
					if (JWTTokenUtils.isValid(validTime)) {
						// 刷新Token，重新存入请求头
						String newToke = JWTTokenUtils.refreshAccessToken(token);

						// 删除旧的Token，并保存新的Token
						JWTTokenUtils.deleteRedisToken(token);
						JWTTokenUtils.setTokenInfo(newToke, username, ip);
						response.setHeader(JWTConfig.tokenHeader, newToke);

						log.info("用户{}的Token已过期，但为超过刷新时间，已刷新", username);

						token = newToke;
					} else {

						log.info("用户{}的Token已过期且超过刷新时间，不予刷新", username);

						// 加入黑名单
						JWTTokenUtils.addBlackList(token);
						ResponseUtils.responseJson(response, ResponseUtils.response(505, "Token已过期", "已超过刷新有效期"));
						return;
					}
				}

				SysUserDetails sysUserDetails = JWTTokenUtils.parseAccessToken(token);

				if (sysUserDetails != null) {
					// 校验IP
					if (!StringUtils.equals(ip, sysUserDetails.getIp())) {

						log.info("用户{}请求IP与Token中IP信息不一致", username);

						// 加入黑名单
						JWTTokenUtils.addBlackList(token);
						ResponseUtils.responseJson(response, ResponseUtils.response(505, "Token已过期", "可能存在IP伪造风险"));
						return;
					}

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							sysUserDetails, sysUserDetails.getId(), sysUserDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

}
