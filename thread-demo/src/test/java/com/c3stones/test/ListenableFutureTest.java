package com.c3stones.test;


import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Guava - ListenableFuture 单元测试
 *
 * @author CL
 */
@Slf4j
public class ListenableFutureTest {

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
            if (execTime <= 0) {
                throw new RuntimeException("执行时间必须大于0");
            }
            log.info("任务执行，耗时：" + execTime + " ms");
            Thread.sleep(execTime);
            return execTime;
        }
    }

    /**
     * 增加监听器
     */
    @Test
    public void testAddListener() throws InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<Long> future = listeningExecutorService.submit(new MyCallable(1000));

        // 增加监听器
        future.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("任务执行结束，结果为：" + future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, listeningExecutorService);

        // 为了验证测试结果，阻塞主线程，等待监听器任务异步执行
        // 真实使用不需要阻塞主线程
        Thread.sleep(3000);
        listeningExecutorService.shutdown();
    }

    /**
     * 测试任务执行成功回调
     */
    @Test
    public void addCallbackOfSuccess() throws InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<Long> future = listeningExecutorService.submit(new MyCallable(1000));

        // 增加回调
        FutureCallback<Long> futureCallback = new FutureCallback<Long>() {

            /**
             * 成功时调用
             * @param result
             */
            @Override
            public void onSuccess(Long result) {
                try {
                    log.info("任务执行成功，结果为：" + future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 失败时调用
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                log.error("任务执行失败，异常原因：" + t.getMessage());
            }

        };

        Futures.addCallback(future, futureCallback, listeningExecutorService);

        // 为了验证测试结果，阻塞主线程，等待监听器任务异步执行
        // 真实使用不需要阻塞主线程
        Thread.sleep(3000);
        listeningExecutorService.shutdown();
    }

    /**
     * 测试任务执行失败回调
     */
    @Test
    public void addCallbackOfFailure() throws InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<Long> future = listeningExecutorService.submit(new MyCallable(-1));

        // 增加回调
        FutureCallback<Long> futureCallback = new FutureCallback<Long>() {

            /**
             * 成功时调用
             * @param result
             */
            @Override
            public void onSuccess(Long result) {
                try {
                    log.info("任务执行成功，结果为：" + future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 失败时调用
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                log.error("任务执行失败，异常原因：" + t.getMessage());
            }

        };

        Futures.addCallback(future, futureCallback, listeningExecutorService);

        // 为了验证测试结果，阻塞主线程，等待监听器任务异步执行
        // 真实使用不需要阻塞主线程
        Thread.sleep(3000);
        listeningExecutorService.shutdown();
    }

    /**
     * 测试所有任务执行成功并获取结果集
     *
     * @throws InterruptedException
     */
    @Test
    public void testAsListSuccess() throws InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        // 创建3个任务，3个都成功
        ListenableFuture<Long> future1 = listeningExecutorService.submit(new MyCallable(1000));
        ListenableFuture<Long> future2 = listeningExecutorService.submit(new MyCallable(2000));
        ListenableFuture<Long> future3 = listeningExecutorService.submit(new MyCallable(3000));

        ListenableFuture<List<Long>> future = Futures.allAsList(future1, future2, future3);

        // 增加回调
        FutureCallback<List<Long>> futureCallback = new FutureCallback<List<Long>>() {

            /**
             * 成功时调用
             * @param result
             */
            @Override
            public void onSuccess(List<Long> result) {
                try {
                    log.info("任务执行成功，结果为：" + future.get().stream().map(String::valueOf).collect(Collectors.joining(",")));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 失败时调用
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                log.error("任务执行失败，异常原因：" + t.getMessage());
            }

        };

        Futures.addCallback(future, futureCallback, listeningExecutorService);

        // 为了验证测试结果，阻塞主线程，等待监听器任务异步执行
        // 真实使用不需要阻塞主线程
        Thread.sleep(5000);
        listeningExecutorService.shutdown();
    }

    /**
     * 测试包含失败任务执行并获取结果集
     *
     * @throws InterruptedException
     */
    @Test
    public void testAsListFailure() throws InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        // 创建3个任务，2个都成功，1个失败
        ListenableFuture<Long> future1 = listeningExecutorService.submit(new MyCallable(1000));
        ListenableFuture<Long> future2 = listeningExecutorService.submit(new MyCallable(-2));
        ListenableFuture<Long> future3 = listeningExecutorService.submit(new MyCallable(3000));

        ListenableFuture<List<Long>> future = Futures.allAsList(future1, future2, future3);

        // 增加回调
        FutureCallback<List<Long>> futureCallback = new FutureCallback<List<Long>>() {

            /**
             * 成功时调用
             * @param result
             */
            @Override
            public void onSuccess(List<Long> result) {
                try {
                    log.info("任务执行成功，结果为：" + future.get().stream().map(String::valueOf).collect(Collectors.joining(",")));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 失败时调用
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                log.error("任务执行失败，异常原因：" + t.getMessage());
            }

        };

        Futures.addCallback(future, futureCallback, listeningExecutorService);

        // 为了验证测试结果，阻塞主线程，等待监听器任务异步执行
        // 真实使用不需要阻塞主线程
        Thread.sleep(5000);
        listeningExecutorService.shutdown();
    }

    /**
     * 测试所有任务执行成功或失败获取结果，失败结果替换为null
     *
     * @throws InterruptedException
     */
    @Test
    public void testSuccessfulAsList() throws InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        // 创建3个任务，2个都成功，1个失败
        ListenableFuture<Long> future1 = listeningExecutorService.submit(new MyCallable(1000));
        ListenableFuture<Long> future2 = listeningExecutorService.submit(new MyCallable(-2));
        ListenableFuture<Long> future3 = listeningExecutorService.submit(new MyCallable(3000));

        ListenableFuture<List<Long>> future = Futures.successfulAsList(future1, future2, future3);

        // 增加回调
        FutureCallback<List<Long>> futureCallback = new FutureCallback<List<Long>>() {

            /**
             * 成功时调用
             * @param result
             */
            @Override
            public void onSuccess(List<Long> result) {
                try {
                    log.info("任务执行成功，结果为：" + future.get().stream().map(String::valueOf).collect(Collectors.joining(",")));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 失败时调用
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                log.error("任务执行失败，异常原因：" + t.getMessage());
            }

        };

        Futures.addCallback(future, futureCallback, listeningExecutorService);

        // 为了验证测试结果，阻塞主线程，等待监听器任务异步执行
        // 真实使用不需要阻塞主线程
        Thread.sleep(5000);
        listeningExecutorService.shutdown();
    }

    /**
     * 测试返回结果同步转换
     */
    @Test
    public void testTransform() throws InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<Long> future1 = listeningExecutorService.submit(new MyCallable(1000));

        // 将返回结果转换为字符串
        ListenableFuture<String> transform = Futures.transform(future1, new Function<Long, String>() {

            @Override
            public String apply(Long input) {
                String result = String.valueOf(input);
                log.info("将Long[" + input + "] 转换为 String[" + result + "]");
                return "String -> " + result;
            }

        }, listeningExecutorService);

        // 增加回调
        FutureCallback<String> futureCallback = new FutureCallback<String>() {

            /**
             * 成功时调用
             * @param result
             */
            @Override
            public void onSuccess(String result) {
                log.info("任务执行成功，结果为：" + result);
            }

            /**
             * 失败时调用
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                log.error("任务执行失败，异常原因：" + t.getMessage());
            }

        };

        Futures.addCallback(transform, futureCallback, listeningExecutorService);

        // 为了验证测试结果，阻塞主线程，等待监听器任务异步执行
        // 真实使用不需要阻塞主线程
        Thread.sleep(3000);
        listeningExecutorService.shutdown();
    }

    /**
     * 测试返回结果异步转换
     */
    @Test
    public void testTransformAsync() throws InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<Long> future1 = listeningExecutorService.submit(new MyCallable(1000));

        // 将返回结果转换为字符串
        ListenableFuture<String> transform = Futures.transformAsync(future1, new AsyncFunction<Long, String>() {

            @Override
            public ListenableFuture<String> apply(Long input) throws Exception {
                String result = String.valueOf(input);
                log.info("将Long[" + input + "] 转换为 String[" + result + "]");
                return Futures.immediateFuture("String -> " + result);
            }

        }, listeningExecutorService);

        // 增加回调
        FutureCallback<String> futureCallback = new FutureCallback<String>() {

            /**
             * 成功时调用
             * @param result
             */
            @Override
            public void onSuccess(String result) {
                log.info("任务执行成功，结果为：" + result);
            }

            /**
             * 失败时调用
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                log.error("任务执行失败，异常原因：" + t.getMessage());
            }

        };

        Futures.addCallback(transform, futureCallback, listeningExecutorService);

        // 为了验证测试结果，阻塞主线程，等待监听器任务异步执行
        // 真实使用不需要阻塞主线程
        Thread.sleep(3000);
        listeningExecutorService.shutdown();
    }

}
