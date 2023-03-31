package com.c3stones.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 继承Thread类创建多线程单元测试
 *
 * @author CL
 */
@Slf4j
public class ThreadTest {

    @Test
    public void testThread() {
        for (int i = 1; i <= 5; i++) {
            new MyThread("thread-" + i).start();
        }
    }

    /**
     * 创建线程
     */
    class MyThread extends Thread {

        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            log.info(Thread.currentThread().getName() + " 正在执行任务...");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
