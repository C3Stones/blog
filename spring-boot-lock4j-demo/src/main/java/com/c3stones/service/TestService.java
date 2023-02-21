package com.c3stones.service;

import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.lock.executor.RedisTemplateLockExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 测试Service
 *
 * @author CL
 */
@Service
@RequiredArgsConstructor
public class TestService {

    /**
     * 测试无锁
     *
     * @param processTime 业务处理时间
     * @return {@link Long}
     */
    public Long test(long processTime) {
        try {
            Thread.sleep(processTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return processTime;
    }

    /**
     * 测试默认分布式锁
     *
     * @param processTime 业务处理时间
     * @return {@link Long}
     */
    @Lock4j
    public Long test1(long processTime) {
        return test(processTime);
    }

    /**
     * 测试自定义配置分布式锁
     * <p>
     * keys: 锁名称，全局唯一<br/>
     * expire: 过期时间，防止死锁<br/>
     * acquireTimeout: 等待时间，获取不到则执行失败策略<br/>
     * </p>
     *
     * @param processTime 业务处理时间
     * @return {@link Long}
     */
    @Lock4j(keys = {"#processTime"}, expire = 60000, acquireTimeout = 1000)
    public Long test2(long processTime) {
        return test(processTime);
    }

    private final LockTemplate lockTemplate;

    /**
     * 测试手动获取分布式锁
     *
     * @param processTime 业务处理时间
     * @return {@link Long}
     */
    public Long test3(long processTime) {
        // 获取锁
        final LockInfo lockInfo = lockTemplate.lock("custom:" + processTime, 30000L, 2000L, RedisTemplateLockExecutor.class);
        if (null == lockInfo) {
            throw new RuntimeException("处理中，请稍后");
        }

        // 获取锁成功
        try {
            test(processTime);
        } finally {
            //释放锁
            lockTemplate.releaseLock(lockInfo);
        }

        return processTime;
    }

    /**
     * 测试5秒内只能访问1次
     *
     * @param processTime 业务处理时间
     * @return {@link Long}
     */
    @Lock4j(keys = {"#processTime"}, acquireTimeout = 0, expire = 5000, autoRelease = false)
    public Long test4(long processTime) {
        return processTime;
    }

}
