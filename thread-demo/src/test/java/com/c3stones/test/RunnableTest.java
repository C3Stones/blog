package com.c3stones.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 实现Runnable接口创建多线程单元测试
 *
 * @author CL
 */
@Slf4j
public class RunnableTest {

    @Test
    public void testRunnable() {
        MyRunnable myRunnable = new MyRunnable();
        for (int i = 1; i <= 5; i++) {
            new Thread(myRunnable, "thread-" + i).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // java8 lambda方式
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                log.info(Thread.currentThread().getName() + " 正在执行任务...");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "lambda-thread-" + i).start();
        }
    }

    /**
     * 创建线程
     */
    class MyRunnable implements Runnable {

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
