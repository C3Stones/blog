package com.c3stones.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Future（FutureTask）优缺点单元测试
 *
 * @author CL
 */
@Slf4j
public class FutureTest {

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
     * 优点：并行执行多个任务
     */
    @Test
    public void testConcurrent() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Future<Long>> futureList = new ArrayList<>(3);

        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 创建3个任务
        futureList.add(executorService.submit(new MyCallable(3000)));
        futureList.add(executorService.submit(new MyCallable(3000)));
        futureList.add(executorService.submit(new MyCallable(3000)));

        // 阻塞获取结果
        futureList.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");
    }

    /**
     * 缺点：获取结果会一直阻塞
     */
    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        FutureTask<Long> futureTask = new FutureTask<>(new MyCallable(60_000));
        executorService.submit(futureTask);

        Long result = futureTask.get();

        log.info("返回结果：" + result);
    }

    /**
     * 缺点：超时会抛出TimeoutException异常
     */
    @Test
    public void testGetTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        FutureTask<Long> futureTask = new FutureTask<>(new MyCallable(60_000));
        executorService.submit(futureTask);

        Long result = futureTask.get(1000, TimeUnit.MILLISECONDS);


        log.info("返回结果：" + result);
    }

    /**
     * 缺点：轮询获取造成CPU空转，浪费资源
     */
    @Test
    public void testRoundGet() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        FutureTask<Long> futureTask = new FutureTask<>(new MyCallable(60_000));
        executorService.submit(futureTask);

        // 可以做其他事
        // ...

        // 最后轮询判断任务是否结束，结束后获取结果
        Long result;
        while (true) {
            if (futureTask.isDone()) {
                result = futureTask.get();
                break;
            }
        }

        log.info("返回结果：" + result);
    }

}
