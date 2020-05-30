package com.c3stones.simple;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 简单测试Demo
 * 
 * @author CL
 *
 */
public class SimpleDemo {

	// 创建固定数目线程的线程池
	// 阿里巴巴Java开发手册中提到可能存在OOM（OutOfMemory）内存溢出异常
//	private static ExecutorService executorService = Executors.newFixedThreadPool(5);

	// 推荐使用com.google.guava的ThreadFactoryBuilder来创建线程池
	private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("simple-threadpool-%d")
			.build();
	/**
	 * java.util.concurrent.ThreadPoolExecutor.ThreadPoolExecutor(int corePoolSize,
	 * int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	 * BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
	 * RejectedExecutionHandler handler)
	 * 
	 * @param corePoolSize    线程池大小
	 * @param maximumPoolSize 最大线程数
	 * @param keepAliveTime   当线程大于线程池大小时，最长等待时间
	 * @param unit            时间单位
	 * @param workQueue       任务队列
	 * @param threadFactory   指定线程工厂
	 * @param handler         当线程池界限和队列容量时，阻止线程处理
	 */
	private static ExecutorService threadPool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(1024), threadFactory, new ThreadPoolExecutor.AbortPolicy());

	public static void main(String[] args) {
		// 提交线程到线程池
		for (int i = 0; i < 5; i++) {
			threadPool.execute(new SimpleThread1());
			threadPool.execute(new SimpleThread2());
		}

		// 关闭
		threadPool.shutdown();
	}
}

class SimpleThread1 implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(SimpleThread1.class);

	@Override
	public void run() {
		try {
			logger.info("线程 SimpleThread1 被调用！");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

class SimpleThread2 implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(SimpleThread2.class);

	@Override
	public void run() {
		try {
			logger.info("线程 SimpleThread2 被调用！");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
