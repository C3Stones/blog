package com.c3stones.security.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.c3stones.security.config.JWTConfig;
import com.c3stones.security.entity.SysUserDetails;
import com.c3stones.security.service.SysUserDetailsService;
import com.c3stones.utils.RedisUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT生产Token工具类
 * 
 * @author CL
 *
 */
@Slf4j
@Component
public class JWTTokenUtils {
	/**
	 * 时间格式化
	 */
	private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private SysUserDetailsService sysUserDetailsService;

	private static JWTTokenUtils jwtTokenUtils;

	@PostConstruct
	public void init() {
		jwtTokenUtils = this;
		jwtTokenUtils.sysUserDetailsService = this.sysUserDetailsService;
	}

	/**
	 * 创建Token
	 * 
	 * @param sysUserDetails 用户信息
	 * @return
	 */
	public static String createAccessToken(SysUserDetails sysUserDetails) {
		String token = Jwts.builder()// 设置JWT
				.setId(sysUserDetails.getId().toString()) // 用户Id
				.setSubject(sysUserDetails.getUsername()) // 主题
				.setIssuedAt(new Date()) // 签发时间
				.setIssuer("C3Stones") // 签发者
				.setExpiration(new Date(System.currentTimeMillis() + JWTConfig.expiration)) // 过期时间
				.signWith(SignatureAlgorithm.HS512, JWTConfig.secret) // 签名算法、密钥
				.claim("authorities", JSON.toJSONString(sysUserDetails.getAuthorities()))// 自定义其他属性，如用户组织机构ID，用户所拥有的角色，用户权限信息等
				.claim("ip", sysUserDetails.getIp()).compact();
		return JWTConfig.tokenPrefix + token;
	}

	/**
	 * 刷新Token
	 * 
	 * @param oldToken 过期但未超过刷新时间的Token
	 * @return
	 */
	public static String refreshAccessToken(String oldToken) {
		String username = JWTTokenUtils.getUserNameByToken(oldToken);
		SysUserDetails sysUserDetails = (SysUserDetails) jwtTokenUtils.sysUserDetailsService
				.loadUserByUsername(username);
		sysUserDetails.setIp(JWTTokenUtils.getIpByToken(oldToken));
		return createAccessToken(sysUserDetails);
	}

	/**
	 * 解析Token
	 * 
	 * @param token Token信息
	 * @return
	 */
	public static SysUserDetails parseAccessToken(String token) {
		SysUserDetails sysUserDetails = null;
		if (StringUtils.isNotEmpty(token)) {
			try {
				// 去除JWT前缀
				token = token.substring(JWTConfig.tokenPrefix.length());

				// 解析Token
				Claims claims = Jwts.parser().setSigningKey(JWTConfig.secret).parseClaimsJws(token).getBody();

				// 获取用户信息
				sysUserDetails = new SysUserDetails();
				sysUserDetails.setId(Long.parseLong(claims.getId()));
				sysUserDetails.setUsername(claims.getSubject());

				// 获取角色
				Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
				String authority = claims.get("authorities").toString();
				if (StringUtils.isNotEmpty(authority)) {
					List<Map<String, String>> authorityList = JSON.parseObject(authority,
							new TypeReference<List<Map<String, String>>>() {
							});
					for (Map<String, String> role : authorityList) {
						if (!role.isEmpty()) {
							authorities.add(new SimpleGrantedAuthority(role.get("authority")));
						}
					}
				}

				sysUserDetails.setAuthorities(authorities);

				// 获取IP
				String ip = claims.get("ip").toString();
				sysUserDetails.setIp(ip);
			} catch (Exception e) {
				log.error("解析Token异常：" + e);
			}
		}
		return sysUserDetails;
	}

	/**
	 * 保存Token信息到Redis中
	 * 
	 * @param token    Token信息
	 * @param username 用户名
	 * @param ip       IP
	 */
	public static void setTokenInfo(String token, String username, String ip) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());

			Integer refreshTime = JWTConfig.refreshTime;
			LocalDateTime localDateTime = LocalDateTime.now();

			RedisUtils.hset(token, "username", username, refreshTime);
			RedisUtils.hset(token, "ip", ip, refreshTime);
			RedisUtils.hset(token, "refreshTime",
					df.format(localDateTime.plus(JWTConfig.refreshTime, ChronoUnit.MILLIS)), refreshTime);
			RedisUtils.hset(token, "expiration", df.format(localDateTime.plus(JWTConfig.expiration, ChronoUnit.MILLIS)),
					refreshTime);
		}
	}

	/**
	 * 将Token放到黑名单中
	 * 
	 * @param token Token信息
	 */
	public static void addBlackList(String token) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());
			RedisUtils.hset("blackList", token, df.format(LocalDateTime.now()));
		}
	}

	/**
	 * Redis中删除Token
	 * 
	 * @param token Token信息
	 */
	public static void deleteRedisToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());
			RedisUtils.deleteKey(token);
		}
	}

	/**
	 * 判断当前Token是否在黑名单中
	 * 
	 * @param token Token信息
	 */
	public static boolean isBlackList(String token) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());
			return RedisUtils.hasKey("blackList", token);
		}
		return false;
	}

	/**
	 * 是否过期
	 * 
	 * @param expiration 过期时间，字符串
	 * @return 过期返回True，未过期返回false
	 */
	public static boolean isExpiration(String expiration) {
		LocalDateTime expirationTime = LocalDateTime.parse(expiration, df);
		LocalDateTime localDateTime = LocalDateTime.now();
		if (localDateTime.compareTo(expirationTime) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 是否有效
	 * 
	 * @param refreshTime 刷新时间，字符串
	 * @return 有效返回True，无效返回false
	 */
	public static boolean isValid(String refreshTime) {
		LocalDateTime validTime = LocalDateTime.parse(refreshTime, df);
		LocalDateTime localDateTime = LocalDateTime.now();
		if (localDateTime.compareTo(validTime) > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 检查Redis中是否存在Token
	 * 
	 * @param token Token信息
	 * @return
	 */
	public static boolean hasToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());
			return RedisUtils.hasKey(token);
		}
		return false;
	}

	/**
	 * 从Redis中获取过期时间
	 * 
	 * @param token Token信息
	 * @return 过期时间，字符串
	 */
	public static String getExpirationByToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());
			return RedisUtils.hget(token, "expiration").toString();
		}
		return null;
	}

	/**
	 * 从Redis中获取刷新时间
	 * 
	 * @param token Token信息
	 * @return 刷新时间，字符串
	 */
	public static String getRefreshTimeByToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());
			return RedisUtils.hget(token, "refreshTime").toString();
		}
		return null;
	}

	/**
	 * 从Redis中获取用户名
	 * 
	 * @param token Token信息
	 * @return
	 */
	public static String getUserNameByToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());
			return RedisUtils.hget(token, "username").toString();
		}
		return null;
	}

	/**
	 * 从Redis中获取IP
	 * 
	 * @param token Token信息
	 * @return
	 */
	public static String getIpByToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			// 去除JWT前缀
			token = token.substring(JWTConfig.tokenPrefix.length());
			return RedisUtils.hget(token, "ip").toString();
		}
		return null;
	}

}
