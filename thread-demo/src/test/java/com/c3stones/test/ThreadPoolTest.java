package com.c3stones.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 创建线程池单元测试
 *
 * @author CL
 */
@Slf4j
public class ThreadPoolTest {

    /**
     * 自定义线程池
     * <p>
     * 优点：可以自定义参数
     * </p>
     */
    @Test
    public void testNewThreadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                // 核心线程数
                3,
                // 最大线程数
                5,
                // 空闲线程最大存活时间
                60L,
                // 空闲线程最大存活时间单位
                TimeUnit.SECONDS,
                // 等待队列及大小
                new ArrayBlockingQueue<>(100),
                // 创建新线程时使用的工厂
                Executors.defaultThreadFactory(),
                // 当线程池达到最大时的处理策略
//                new ThreadPoolExecutor.AbortPolicy()          // 抛出RejectedExecutionHandler异常
                new ThreadPoolExecutor.CallerRunsPolicy()       // 交由调用者的线程执行
//                new ThreadPoolExecutor.DiscardOldestPolicy()  // 丢掉最早未处理的任务
//                new ThreadPoolExecutor.DiscardPolicy()        // 丢掉新提交的任务
        );

        // 总共5个任务
        for (int i = 1; i <= 5; i++) {
            int taskIndex = i;
            executor.execute(() -> {
                log.info("线程 " + Thread.currentThread().getName() + " 正在执行任务 " + taskIndex);

                // 每个任务耗时1秒
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }

    /**
     * 固定大小线程池
     * <p>
     * 优点：当任务执行较快，且任务较少时使用方便
     * </p>
     * <p>
     * 风险：当处理较慢时，等待队列的任务堆积会导致OOM
     * </p>
     */
    @Test
    public void testNewFixThreadPool() {
        // 3个固定线程
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 总共5个任务
        for (int i = 1; i <= 5; i++) {
            int taskIndex = i;
            executor.execute(() -> {
                log.info("线程 " + Thread.currentThread().getName() + " 正在执行任务 " + taskIndex);

                // 每个任务耗时1秒
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }

    /**
     * 单一线程池
     * <p>
     * 优势：保存任务按照提交的顺序执行
     * </p>
     * <p>
     * 风险：当处理较慢时，等待队列的任务堆积会导致OOM
     * </p>
     */
    @Test
    public void testNewSingleThreadExecutor() {
        // 1个线程
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // 总共5个任务
        for (int i = 1; i <= 5; i++) {
            int taskIndex = i;
            executor.execute(() -> {
                log.info("线程 " + Thread.currentThread().getName() + " 正在执行任务 " + taskIndex);

                // 每个任务耗时1秒
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }

    /**
     * 共享线程池
     * <p>
     * 优势：当在某一时间段内任务较多，且执行较快时方便使用
     * </p>
     * <p>
     * 风险：当处理较慢时，会创建大量的线程
     * </p>
     */
    @Test
    public void testNewCachedThreadPool() {
        ExecutorService executor = Executors.newCachedThreadPool();

        // 总共5个任务
        for (int i = 1; i <= 5; i++) {
            int taskIndex = i;
            executor.execute(() -> {
                log.info("线程 " + Thread.currentThread().getName() + " 正在执行任务 " + taskIndex);

                // 每个任务耗时1秒
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }

    /**
     * 定时线程池
     * <p>
     * 优点：可以定时执行某些任务
     * </p>
     * <p>
     * 风险：当处理较慢时，等待队列的任务堆积会导致OOM
     * </p>
     */
    @Test
    public void testNewScheduledThreadPool() {
//        // 单一线程
//        ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        // 指定核心线程数
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

        executor.schedule(() -> {
            log.info("3秒后开始执行，以后不再执行");

            // 每个任务耗时1秒
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 3, TimeUnit.SECONDS);
//
//        executor.scheduleAtFixedRate(() -> {
//            log.info("3秒后开始执行，以后每2秒执行一次");
//
//            // 每个任务耗时1秒
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, 3, 2, TimeUnit.SECONDS);
//
//        executor.scheduleWithFixedDelay(() -> {
//            log.info("3秒后开始执行，以后延迟2秒执行一次");
//
//            // 每个任务耗时1秒
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, 3, 2, TimeUnit.SECONDS);
    }

}
