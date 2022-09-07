package com.c3stones.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

/**
 * 本地缓存配置类
 *
 * @author CL
 */
@Configuration
public class NativeCacheConfig {

    private static final Logger log = LoggerFactory.getLogger(NativeCacheConfig.class);

    /**
     * 本地全局缓存
     *
     * @return
     */
    @Bean
    public Cache<String, Object> globalCache() {
        return Caffeine.newBuilder()
                // 最后一次写入后经过固定时间过期
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(10)
                // 缓存的最大条数
                .maximumSize(100)
                // 移除缓存监听
                .removalListener((k, v, cause) ->
                        log.warn("NativeCache RemovalListener key:{}, value:{}, cause:{}", k, v, cause))
                .build();
    }

    /**
     * 注入本地缓存管理器
     *
     * @return
     */
    @Bean
    public NativeCacheMagger nativeCacheManager() {
        return new NativeCacheMagger();
    }

    /**
     * 构建缓存
     *
     * @param cacheName    缓存名称
     * @param expireTime   过期时间
     * @param initCapacity 初始化大小
     * @param maxCapacity  最大大小
     * @return
     */
    public static org.springframework.cache.Cache buildCache(String cacheName,
                                                             Duration expireTime,
                                                             int initCapacity,
                                                             int maxCapacity) {
        notNull(cacheName, "缓存名称不能为空");
        notNull(expireTime, "过期时间不能为空");
        isTrue(initCapacity >= 0, "初始化大小为正整数");
        isTrue(maxCapacity >= 0, "最大大小为正整数");
        return new CaffeineCache(cacheName, Caffeine.newBuilder()
                // 最后一次写入后经过固定时间过期
                .expireAfterWrite(expireTime)
                // 初始的缓存空间大小
                .initialCapacity(initCapacity)
                // 缓存的最大条数
                .maximumSize(maxCapacity)
                // 设置值软引用
                .softValues()
                // 移除缓存监听
                .removalListener((k, v, cause) ->
                        log.warn("NativeCache RemovalListener key:{}, value:{}, cause:{}", k, v, cause))
                .build());
    }

    /**
     * 本地缓存管理器
     */
    public static class NativeCacheMagger extends AbstractCacheManager {

        /**
         * 缓存队列
         */
        private Collection<org.springframework.cache.Cache> caches = new CopyOnWriteArraySet<>();

        /**
         * 添加缓存到缓存管理器
         *
         * @param cache
         * @return
         */
        public NativeCacheMagger putCache(org.springframework.cache.Cache cache) {
            this.caches.add(cache);
            return this;
        }

        /**
         * 将缓存加载到缓存管理器
         */
        @Override
        public Collection<org.springframework.cache.Cache> loadCaches() {
            return this.caches;
        }
    }

}
