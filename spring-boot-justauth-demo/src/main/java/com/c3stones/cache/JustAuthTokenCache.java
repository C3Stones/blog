package com.c3stones.cache;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;

import me.zhyd.oauth.model.AuthToken;

/**
 * 授权Token缓存Redis
 * 
 * @author CL
 *
 */
@Configuration
public class JustAuthTokenCache {

	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;

	private BoundHashOperations<String, String, AuthToken> valueOperations;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		valueOperations = redisTemplate.boundHashOps("JUSTAUTH::TOKEN");
	}

	/**
	 * 保存Token
	 * 
	 * @param uuid     用户uuid
	 * @param authUser 授权用户
	 * @return
	 */
	public AuthToken saveorUpdate(String uuid, AuthToken authToken) {
		valueOperations.put(uuid, authToken);
		return authToken;
	}

	/**
	 * 根据用户uuid查询Token
	 * 
	 * @param uuid 用户uuid
	 * @return
	 */
	public AuthToken getByUuid(String uuid) {
		Object token = valueOperations.get(uuid);
		if (null == token) {
			return null;
		}
		return JSONObject.parseObject(JSONObject.toJSONString(token), AuthToken.class);
	}

	/**
	 * 查询所有Token
	 * 
	 * @return
	 */
	public List<AuthToken> listAll() {
		return new LinkedList<>(Objects.requireNonNull(valueOperations.values()));
	}

	/**
	 * 根据用户uuid移除Token
	 * 
	 * @param uuid 用户uuid
	 * @return
	 */
	public void remove(String uuid) {
		valueOperations.delete(uuid);
	}

}
