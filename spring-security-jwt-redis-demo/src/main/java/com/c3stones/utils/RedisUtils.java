package com.c3stones.utils;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis工具类
 * 
 * @author CL
 *
 */
@Component
public class RedisUtils {

	@Autowired
	private StringRedisTemplate redisTemplate;

	private static RedisUtils redisUtils;

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		redisUtils = this;
		redisUtils.redisTemplate = this.redisTemplate;
	}

	/**
	 * 查询key，支持模糊查询
	 *
	 * @param key
	 */
	public static Set<String> keys(String key) {
		return redisUtils.redisTemplate.keys(key);
	}

	/**
	 * 获取值
	 * 
	 * @param key
	 */
	public static Object get(String key) {
		return redisUtils.redisTemplate.opsForValue().get(key);
	}

	/**
	 * 设置值
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		redisUtils.redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 设置值，并设置过期时间
	 * 
	 * @param key
	 * @param value
	 * @param expire 过期时间，单位秒
	 */
	public static void set(String key, String value, Integer expire) {
		redisUtils.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
	}

	/**
	 * 删出key
	 * 
	 * @param key
	 */
	public static void delete(String key) {
		redisUtils.redisTemplate.opsForValue().getOperations().delete(key);
	}

	/**
	 * 设置对象
	 * 
	 * @param key     key
	 * @param hashKey hashKey
	 * @param object  对象
	 */
	public static void hset(String key, String hashKey, Object object) {
		redisUtils.redisTemplate.opsForHash().put(key, hashKey, object);
	}

	/**
	 * 设置对象
	 * 
	 * @param key     key
	 * @param hashKey hashKey
	 * @param object  对象
	 * @param expire  过期时间，单位秒
	 */
	public static void hset(String key, String hashKey, Object object, Integer expire) {
		redisUtils.redisTemplate.opsForHash().put(key, hashKey, object);
		redisUtils.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
	}

	/**
	 * 设置HashMap
	 *
	 * @param key key
	 * @param map map值
	 */
	public static void hset(String key, HashMap<String, Object> map) {
		redisUtils.redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * key不存在时设置值
	 * 
	 * @param key
	 * @param hashKey
	 * @param object
	 */
	public static void hsetAbsent(String key, String hashKey, Object object) {
		redisUtils.redisTemplate.opsForHash().putIfAbsent(key, hashKey, object);
	}

	/**
	 * 获取Hash值
	 *
	 * @param key
	 * @param hashKey
	 * @return
	 */
	public static Object hget(String key, String hashKey) {
		return redisUtils.redisTemplate.opsForHash().get(key, hashKey);
	}

	/**
	 * 获取key的所有值
	 *
	 * @param key
	 * @return
	 */
	public static Object hget(String key) {
		return redisUtils.redisTemplate.opsForHash().entries(key);
	}

	/**
	 * 删除key的所有值
	 *
	 * @param key
	 */
	public static void deleteKey(String key) {
		redisUtils.redisTemplate.opsForHash().getOperations().delete(key);
	}

	/**
	 * 判断key下是否有值
	 *
	 * @param key
	 */
	public static Boolean hasKey(String key) {
		return redisUtils.redisTemplate.opsForHash().getOperations().hasKey(key);
	}

	/**
	 * 判断key和hasKey下是否有值
	 *
	 * @param key
	 * @param hasKey
	 */
	public static Boolean hasKey(String key, String hasKey) {
		return redisUtils.redisTemplate.opsForHash().hasKey(key, hasKey);
	}

}
