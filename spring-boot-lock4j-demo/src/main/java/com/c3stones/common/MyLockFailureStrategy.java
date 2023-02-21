package com.c3stones.common;

import com.baomidou.lock.LockFailureStrategy;
import com.baomidou.lock.exception.LockFailureException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 自定义获取锁失败策略
 *
 * @author CL
 */
@Component
public class MyLockFailureStrategy implements LockFailureStrategy {

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        throw new LockFailureException("处理中，请稍后");
    }

}
