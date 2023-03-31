package com.c3stones.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * CompletionService 单元测试
 *
 * @author CL
 */
@Slf4j
public class CompletionServiceTest {

    /**
     * 创建线程
     */
    @RequiredArgsConstructor
    class MyCallable implements Callable<Long> {

        /**
         * 任务执行时间
         */
        private final long execTime;

        @Override
        public Long call() throws Exception {
            Thread.sleep(execTime);
            return execTime;
        }
    }

    /**
     * Future：测试并行执行多个任务获取结果后继续业务处理
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testFuture() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Future<Long>> futureList = new ArrayList<>(3);

        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 创建5个任务
        futureList.add(executorService.submit(new MyCallable(5000)));
        futureList.add(executorService.submit(new MyCallable(4000)));
        futureList.add(executorService.submit(new MyCallable(3000)));
        futureList.add(executorService.submit(new MyCallable(2000)));
        futureList.add(executorService.submit(new MyCallable(1000)));

        // 阻塞获取结果
        for (Future<Long> future : futureList) {
            Long result = future.get();
            log.debug("执行结果：" + result);

            // 模拟获取结果后业务处理
            Thread.sleep(500);
        }

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");

        executorService.shutdown();
    }

    /**
     * CompletionService：测试并行执行多个任务获取结果后继续业务处理
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testCompletionService() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CompletionService<Long> completionService = new ExecutorCompletionService<>(executorService);

        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 创建5个任务
        completionService.submit(new MyCallable(5000));
        completionService.submit(new MyCallable(4000));
        completionService.submit(new MyCallable(3000));
        completionService.submit(new MyCallable(2000));
        completionService.submit(new MyCallable(1000));

        for (int i = 0; i < 5; i++) {
            Long result = completionService.take().get();
            log.debug("执行结果：" + result);

            // 模拟获取结果后业务处理
            Thread.sleep(500);
        }

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");

        executorService.shutdown();
    }

    /**
     * CompletionService：测试快速获取任意一个结果
     */
    @Test
    public void testFastResult() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletionService<Long> completionService = new ExecutorCompletionService<>(executorService);

        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 创建3个任务
        completionService.submit(new MyCallable(3000));
        completionService.submit(new MyCallable(2000));
        completionService.submit(new MyCallable(1000));

        Long result = completionService.take().get();
        log.debug("执行结果：" + result);

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");

        executorService.shutdown();
    }

}
