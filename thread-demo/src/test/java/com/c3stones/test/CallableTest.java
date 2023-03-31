package com.c3stones.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 实现Callable接口创建多线程单元测试
 *
 * @author CL
 */
@Slf4j
public class CallableTest {

    @Test
    public void testCallable() {
        MyCallable myCallable = new MyCallable();
        for (int i = 1; i <= 5; i++) {
            FutureTask<String> futureTask = new FutureTask<>(myCallable);
            new Thread(futureTask, "thread-" + i).start();

            try {
                String result = futureTask.get();
                log.info("thread-" + i + " 返回结果：" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // java8 lambda方式
        for (int i = 1; i <= 5; i++) {
            FutureTask<String> futureTask = new FutureTask<>(() -> {
                log.info(Thread.currentThread().getName() + " 正在执行任务...");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return Thread.currentThread().getName();
            });


            new Thread(futureTask, "lambda-thread-" + i).start();

            try {
                String result = futureTask.get();
                log.info("lambda-thread-" + i + " 返回结果：" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建线程
     */
    class MyCallable implements Callable<String> {

        @Override
        public String call() {
            log.info(Thread.currentThread().getName() + " 正在执行任务...");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return Thread.currentThread().getName();
        }
    }

}
