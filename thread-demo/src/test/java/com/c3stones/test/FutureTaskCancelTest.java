package com.c3stones.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * FutureTask 取消任务单元测试
 *
 * @author CL
 */
@Slf4j
public class FutureTaskCancelTest {

    /**
     * 创建线程
     */
    @RequiredArgsConstructor
    class MyCallable implements Callable<String> {

        private final String taskName;

        @Override
        public String call() throws Exception {
            log.info(taskName + "开始执行");
            Thread.sleep(1000);
            log.info(taskName + "结束执行");
            return null;
        }
    }

    /**
     * 任务1开始执行，任务2未执行。<br/>
     * 取消任务1，mayInterruptIfRunning设置为true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testCancalTask1ForTrue() throws ExecutionException, InterruptedException {
        FutureTask futureTask1 = new FutureTask(new MyCallable("任务1"));
        FutureTask futureTask2 = new FutureTask(new MyCallable("任务2"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(futureTask1);
        executorService.submit(futureTask2);

        // 保证任务1开始执行
        Thread.sleep(500);

        futureTask1.cancel(true);
        futureTask2.get();

        executorService.shutdown();
    }

    /**
     * 任务1开始执行，任务2未执行。<br/>
     * 取消任务1，mayInterruptIfRunning设置为false
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testCancalTask1ForFalse() throws ExecutionException, InterruptedException {
        FutureTask futureTask1 = new FutureTask(new MyCallable("任务1"));
        FutureTask futureTask2 = new FutureTask(new MyCallable("任务2"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(futureTask1);
        executorService.submit(futureTask2);

        // 保证任务1开始执行
        Thread.sleep(500);

        futureTask1.cancel(false);
        futureTask2.get();

        executorService.shutdown();
    }

    /**
     * 任务1开始执行，任务2未执行。<br/>
     * 取消任务2，mayInterruptIfRunning设置为true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testCancalTask2ForTrue() throws ExecutionException, InterruptedException {
        FutureTask futureTask1 = new FutureTask(new MyCallable("任务1"));
        FutureTask futureTask2 = new FutureTask(new MyCallable("任务2"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(futureTask1);
        executorService.submit(futureTask2);

        // 保证任务1开始执行
        Thread.sleep(500);

        futureTask1.get();
        futureTask2.cancel(true);

        executorService.shutdown();
    }

    /**
     * 任务1开始执行，任务2未执行。<br/>
     * 取消任务2，mayInterruptIfRunning设置为false
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testCancalTask2ForFalse() throws ExecutionException, InterruptedException {
        FutureTask futureTask1 = new FutureTask(new MyCallable("任务1"));
        FutureTask futureTask2 = new FutureTask(new MyCallable("任务2"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(futureTask1);
        executorService.submit(futureTask2);

        // 保证任务1开始执行
        Thread.sleep(500);

        futureTask1.get();
        futureTask2.cancel(false);

        executorService.shutdown();
    }

}
