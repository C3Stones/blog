package com.c3stones.clients;

import com.c3stones.config.NativeCacheConfig.NativeCacheMagger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.c3stones.config.NativeCacheConfig.buildCache;

/**
 * 缓存客户端
 *
 * @author CL
 */
@Component
public class CacheClient {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private NativeCacheMagger nativeCacheMagger;
    @Autowired
    private com.github.benmanes.caffeine.cache.Cache<String, Object> globalCache;

    /**
     * 缓存数据
     *
     * @param key   缓存键
     * @param value 数据
     */
    public void putOfNative(String key, Object value) {
        if (!StringUtils.hasLength(key) || Objects.isNull(value)) {
            return;
        }
        globalCache.put(key, value);
    }

    /**
     * 移除缓存
     *
     * @param key 缓存键
     */
    public void removeOfNative(String key) {
        if (!StringUtils.hasLength(key)) {
            return;
        }
        globalCache.invalidate(key);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @return
     */
    public Object getByNative(String key) {
        if (!StringUtils.hasLength(key)) {
            return null;
        }
        return globalCache.getIfPresent(key);
    }

    /**
     * 获取缓存
     *
     * @param key   缓存键
     * @param clazz 数据类型
     * @param <T>
     * @return
     */
    public <T> T getByNative(String key, Class<T> clazz) {
        if (Objects.nonNull(clazz)) {
            Object obj = getByNative(key);
            try {
                if (Objects.nonNull(obj) && obj.getClass().isAssignableFrom(clazz)) {
                    return MAPPER.convertValue(obj, clazz);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 获取全部缓存
     *
     * @return
     */
    public Map<String, Object> getAllByNative() {
        return globalCache.asMap();
    }


    /**
     * 缓存数据
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param value     缓存值
     */
    public void putOfNative(String cacheName, String key, Object value) {
        if (Objects.isNull(value)) {
            return;
        }
        putOfNative(Optional.ofNullable(nativeCacheMagger.getCache(cacheName))
                .orElse(buildCache(cacheName, Duration.ofHours(1), 20, 100)), key, value);
    }

    /**
     * 缓存数据
     *
     * @param cache 缓存
     * @param key   缓存键
     * @param value 数据
     */
    public void putOfNative(org.springframework.cache.Cache cache, String key, Object value) {
        if (Objects.isNull(value)) {
            return;
        }
        cache.put(key, value);
        nativeCacheMagger.putCache(cache).initializeCaches();
    }

    /**
     * 移除缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     */
    public void removeOfNative(String cacheName, String key) {
        if (!StringUtils.hasLength(cacheName)) {
            return;
        }
        removeOfNative(nativeCacheMagger.getCache(cacheName), key);
    }

    /**
     * 移除缓存
     *
     * @param cache 缓存
     * @param key   缓存键
     */
    public void removeOfNative(Cache cache, String key) {
        if (Objects.isNull(cache) || !StringUtils.hasLength(key)) {
            return;
        }
        cache.evict(key);
    }

    /**
     * 移除全部
     *
     * @param cache
     * @return
     */
    public boolean removeAllOfNative(Cache cache) {
        if (Objects.isNull(cache)) {
            return true;
        }
        return cache.invalidate();
    }

    /**
     * 移除全部
     *
     * @param cacheName
     * @return
     */
    public boolean removeAllOfNative(String cacheName) {
        if (!StringUtils.hasLength(cacheName)) {
            return true;
        }
        return removeAllOfNative(nativeCacheMagger.getCache(cacheName));
    }

    /**
     * 加载所有缓存
     *
     * @return
     */
    public Collection<Cache> loadCaches() {
        return nativeCacheMagger.loadCaches();
    }

    /**
     * 获取缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @return
     */
    public Object getByNative(String cacheName, String key) {
        if (!StringUtils.hasLength(cacheName)) {
            return null;
        }
        return getByNative(nativeCacheMagger.getCache(cacheName), key);
    }

    /**
     * 获取缓存
     *
     * @param cache 缓存
     * @param key   缓存键
     * @return
     */
    public Object getByNative(Cache cache, String key) {
        if (Objects.isNull(cache) || !StringUtils.hasLength(key)) {
            return null;
        }
        return cache.get(key);
    }

    /**
     * 获取缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param clazz     数据类型
     * @param <T>
     * @return
     */
    public <T> T getByNative(String cacheName, String key, Class<T> clazz) {
        if (!StringUtils.hasLength(cacheName)) {
            return null;
        }
        return getByNative(nativeCacheMagger.getCache(cacheName), key, clazz);
    }

    /**
     * 获取缓存
     *
     * @param cache 缓存
     * @param key   缓存键
     * @param clazz 数据类型
     * @param <T>
     * @return
     */
    public <T> T getByNative(Cache cache, String key, Class<T> clazz) {
        if (Objects.isNull(cache) || Objects.isNull(key) || Objects.isNull(clazz)) {
            return null;
        }
        return cache.get(key, clazz);
    }

    /**
     * 获取全部缓存
     *
     * @param cache 缓存
     * @return
     */
    public List<Object> getAllByNative(Cache cache) {
        if (Objects.nonNull(cache) && cache instanceof CaffeineCache) {
            return ((CaffeineCache) cache).getNativeCache().asMap().values().stream().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 获取全部缓存
     *
     * @param cache 缓存
     * @param clazz 数据类型
     * @param <T>
     * @return
     */
    public <T> List<T> getAllByNative(Cache cache, Class<T> clazz) {
        return getAllByNative(cache).stream().map(obj -> {
            if (obj.getClass().isAssignableFrom(clazz)) {
                return MAPPER.convertValue(obj, clazz);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取全部缓存
     *
     * @param cacheName 缓存名称
     * @return
     */
    public List<Object> getAllByNative(String cacheName) {
        return getAllByNative(nativeCacheMagger.getCache(cacheName));
    }

    /**
     * 获取全部缓存
     *
     * @param cacheName 缓存名称
     * @param clazz     数据类型
     * @return
     */
    public <T> List<T> getAllByNative(String cacheName, Class<T> clazz) {
        return getAllByNative(nativeCacheMagger.getCache(cacheName), clazz);
    }
    
}
