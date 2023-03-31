package com.c3stones.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * Future接口实现类FutureTask单元测试
 *
 * @author CL
 */
@Slf4j
public class FutureTaskTest {

    /**
     * 创建线程
     */
    class MyRunnable implements Runnable {

        @Override
        public void run() {
            log.info("正在执行任务...");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
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
            log.info("正在执行任务...");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "result";
        }
    }

    /**
     * Callable + FutureTask
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testCallable() throws ExecutionException, InterruptedException {
        MyCallable myCallable = new MyCallable();
        FutureTask<String> futureTask = new FutureTask<>(myCallable);
        new Thread(futureTask).start();
        String result = futureTask.get();
        log.info("返回结果：" + result);

        // 使用Executors将Runnable转换为Callable
        // Executors.callable(Runnable task, T result) 可以自定义返回值
        // Executors.callable(Runnable task) 返回值为null
        MyRunnable myRunnable = new MyRunnable();
        Callable myCallable2 = Executors.callable(myRunnable, "custom result2");
        FutureTask<String> futureTask2 = new FutureTask<>(myCallable2);
        new Thread(futureTask2).start();
        String result2 = futureTask2.get();
        log.info("返回结果2：" + result2);
    }

    /**
     * Runnable + FutureTask
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testRunnable() throws ExecutionException, InterruptedException {
        MyRunnable myRunnable = new MyRunnable();
        FutureTask<String> futureTask = new FutureTask<>(myRunnable, "custom result");
        new Thread(futureTask).start();
        String result = futureTask.get();
        log.info("返回结果：" + result);
    }

    /**
     * Future + ExecutorService
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testFuture() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        MyCallable myCallable = new MyCallable();
        Future<String> future = executorService.submit(myCallable);
        String result = future.get();
        log.info("返回结果：" + result);

        MyRunnable myRunnable = new MyRunnable();
        Future<String> future2 = executorService.submit(myRunnable, "custom result2");
        String result2 = future2.get();
        log.info("返回结果2：" + result2);

        executorService.shutdown();
    }

    /**
     * FutureTask + ExecutorService
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testFutureTask() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        MyCallable myCallable = new MyCallable();
        FutureTask<String> futureTask = new FutureTask<>(myCallable);
        executorService.submit(futureTask);
        String result = futureTask.get();
        log.info("返回结果：" + result);

        MyRunnable myRunnable = new MyRunnable();
        FutureTask<String> futureTask2 = new FutureTask<>(myRunnable, "custom result2");
        executorService.submit(futureTask2);
        String result2 = futureTask2.get();
        log.info("返回结果2：" + result2);

        executorService.shutdown();
    }

}
