package com.c3stones.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.concurrent.*;

/**
 * CompletableFuture 单元测试
 *
 * @author CL
 */
@Slf4j
public class CompletableFutureTest {

    /**
     * 创建带有返回值的异步任务
     */
    @Test
    public void testSupplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("任务执行中...");
            return Boolean.TRUE;
        });

        Boolean result = completableFuture.get();
        log.info("执行结果：" + result);
    }

    /**
     * 创建带有返回值的异步任务，并指定线程池
     */
    @Test
    public void testSupplyAsync2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("任务执行中...");
            return Boolean.TRUE;
        }, executorService);

        Boolean result = completableFuture.get();
        log.info("执行结果：" + result);
    }

    /**
     * 创建无返回值的异步任务
     */
    @Test
    public void testRunAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            log.info("任务执行中...");
        });

        Void result = completableFuture.get();
        log.info("执行结果：" + result);
    }

    /**
     * 创建无返回值的异步任务，并指定线程池
     */
    @Test
    public void testRunAsync2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            log.info("任务执行中...");
        }, executorService);

        Void result = completableFuture.get();
        log.info("执行结果：" + result);
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数入参为任务1的返回结果，并带有返回值
     * <p>
     * 相同线程执行
     * </p>
     */
    @Test
    public void testThenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = completableFuture1.thenApply((result) -> {
            log.info("回调函数执行中...");
            return result * 10;
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数入参为任务1的返回结果，并带有返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testThenApplyAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = completableFuture1.thenApplyAsync((result) -> {
            log.info("回调函数执行中...");
            return result * 10;
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数入参为任务1的返回结果，无返回值
     * <p>
     * 相同线程执行
     * </p>
     */
    @Test
    public void testThenAccept() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Void> completableFuture2 = completableFuture1.thenAccept((result) -> {
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数入参为任务1的返回结果，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testThenAcceptAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Void> completableFuture2 = completableFuture1.thenAcceptAsync((result) -> {
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数无入参，无出参
     * <p>
     * 相同线程执行
     * </p>
     */
    @Test
    public void testThenRun() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Void> completableFuture2 = completableFuture1.thenRun(() -> {
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数无入参，无出参
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testThenRunAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Void> completableFuture2 = completableFuture1.thenRunAsync(() -> {
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行成功<br/>
     * 回调函数入参1为任务1的返回结果，入参2为null
     * <p>
     * 相同线程执行
     * </p>
     */
    @Test
    public void testWhenComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = completableFuture1.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("任务 1 返回异常：" + exception.getMessage());
            } else {
                log.info("任务 1 返回结果：" + result);
            }
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行抛出异常<br/>
     * 回调函数入参1为null.入参2为任务1抛出的异常
     * <p>
     * 相同线程执行
     * </p>
     */
    @Test
    public void testWhenCompleteException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            int a = 1 / 0;
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = completableFuture1.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("任务 1 返回异常：" + exception.getMessage());
            } else {
                log.info("任务 1 返回结果：" + result);
            }
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数入参1为null.入参2为任务1抛出的异常
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testWhenCompleteAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = completableFuture1.whenCompleteAsync((result, exception) -> {
            if (exception != null) {
                log.error("任务 1 返回异常：" + exception.getMessage());
            } else {
                log.info("任务 1 返回结果：" + result);
            }
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行抛出异常<br/>
     * 回调函数入参1为null.入参2为任务1抛出的异常
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testWhenCompleteAsyncException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            int a = 1 / 0;
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = completableFuture1.whenCompleteAsync((result, exception) -> {
            if (exception != null) {
                log.error("任务 1 返回异常：" + exception.getMessage());
            } else {
                log.info("任务 1 返回结果：" + result);
            }
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数入参1为任务1的返回结果，入参2为null，并带有返回值
     * <p>
     * 相同线程执行
     * </p>
     */
    @Test
    public void testHandle() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = completableFuture1.handle((result, exception) -> {
            if (exception != null) {
                log.error("任务 1 返回异常：" + exception.getMessage());
            } else {
                log.info("任务 1 返回结果：" + result);
            }
            return result * 10;
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行抛出异常<br/>
     * 回调函数入参1为任务1的返回结果，入参2为null，并带有返回值
     * <p>
     * 相同线程执行
     * </p>
     */
    @Test
    public void testHandleException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            int a = 1 / 0;
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = completableFuture1.handle((result, exception) -> {
            if (exception != null) {
                log.error("任务 1 返回异常：" + exception.getMessage());
            } else {
                log.info("任务 1 返回结果：" + result);
            }
            return result * 10;
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行完返回结果<br/>
     * 回调函数入参1为任务1的返回结果，入参2为null，并带有返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testHandleAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = completableFuture1.handleAsync((result, exception) -> {
            if (exception != null) {
                log.error("任务 1 返回异常：" + exception.getMessage());
            } else {
                log.info("任务 1 返回结果：" + result);
            }
            return result * 10;
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1执行抛出异常<br/>
     * 回调函数入参1为任务1的返回结果，入参2为null，并带有返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testHandleAsyncException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            int a = 1 / 0;
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = completableFuture1.handleAsync((result, exception) -> {
            if (exception != null) {
                log.error("任务 1 返回异常：" + exception.getMessage());
            } else {
                log.info("任务 1 返回结果：" + result);
            }
            return result * 10;
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("回调函数返回结果：" + completableFuture2.get());
    }

    /**
     * 任务1和任务2都执行成功，再执行回调函数<br/>
     * 回调函数入参为任务1和任务2的返回结果，并带有返回值
     * <p>
     * 回调函数公用两个任务其中一个线程执行
     * </p>
     */
    @Test
    public void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            return 20;
        });

        CompletableFuture<Integer> completableFuture3 = completableFuture1.thenCombine(completableFuture2, (result1, result2) -> {
            log.info("回调函数获取到 任务 1 结果：" + result1 + "，获取到任务 2 结果：" + result2);
            log.info("回调函数执行中...");
            return result1 + result2;
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数入参为任务1和任务2的返回结果，并带有返回值
     * <p>
     * 回调函数公用两个任务其中一个线程执行
     * </p>
     */
    @Test
    public void testThenCombineException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            int a = 1 / 0;
            return 20;
        });

        CompletableFuture<Integer> completableFuture3 = completableFuture1.thenCombine(completableFuture2, (result1, result2) -> {
            log.info("回调函数获取到 任务 1 结果：" + result1 + "，获取到任务 2 结果：" + result2);
            log.info("回调函数执行中...");
            return result1 + result2;
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1和任务2都执行成功，再执行回调函数<br/>
     * 回调函数入参为任务1和任务2的返回结果，并带有返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testThenCombineAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            return 20;
        }, executorService);

        CompletableFuture<Integer> completableFuture3 = completableFuture1.thenCombineAsync(completableFuture2, (result1, result2) -> {
            log.info("回调函数获取到 任务 1 结果：" + result1 + "，获取到任务 2 结果：" + result2);
            log.info("回调函数执行中...");
            return result1 + result2;
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数入参为任务1和任务2的返回结果，并带有返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testThenCombineAsyncException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            int a = 1 / 0;
            return 20;
        }, executorService);

        CompletableFuture<Integer> completableFuture3 = completableFuture1.thenCombineAsync(completableFuture2, (result1, result2) -> {
            log.info("回调函数获取到 任务 1 结果：" + result1 + "，获取到任务 2 结果：" + result2);
            log.info("回调函数执行中...");
            return result1 + result2;
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1和任务2都执行成功，再执行回调函数<br/>
     * 回调函数入参为任务1和任务2的返回结果，无返回值
     * <p>
     * 回调函数公用两个任务其中一个线程执行
     * </p>
     */
    @Test
    public void testThenAcceptBoth() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            return 20;
        });

        CompletableFuture<Void> completableFuture3 = completableFuture1.thenAcceptBoth(completableFuture2, (result1, result2) -> {
            log.info("回调函数获取到 任务 1 结果：" + result1 + "，获取到任务 2 结果：" + result2);
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数入参为任务1和任务2的返回结果，无返回值
     * <p>
     * 回调函数公用两个任务其中一个线程执行
     * </p>
     */
    @Test
    public void testThenAcceptBothException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            int a = 1 / 0;
            return 20;
        });

        CompletableFuture<Void> completableFuture3 = completableFuture1.thenAcceptBoth(completableFuture2, (result1, result2) -> {
            log.info("回调函数获取到 任务 1 结果：" + result1 + "，获取到任务 2 结果：" + result2);
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1和任务2都执行成功，再执行回调函数<br/>
     * 回调函数入参为任务1和任务2的返回结果，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testThenAcceptBothAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            return 20;
        }, executorService);

        CompletableFuture<Void> completableFuture3 = completableFuture1.thenAcceptBothAsync(completableFuture2, (result1, result2) -> {
            log.info("回调函数获取到 任务 1 结果：" + result1 + "，获取到任务 2 结果：" + result2);
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数入参为任务1和任务2的返回结果，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testThenAcceptBothAsyncException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            int a = 1 / 0;
            return 20;
        }, executorService);

        CompletableFuture<Void> completableFuture3 = completableFuture1.thenAcceptBothAsync(completableFuture2, (result1, result2) -> {
            log.info("回调函数获取到 任务 1 结果：" + result1 + "，获取到任务 2 结果：" + result2);
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1和任务2都执行成功，再执行回调函数<br/>
     * 回调函数无入参，无返回值
     * <p>
     * 回调函数公用两个任务其中一个线程执行
     * </p>
     */
    @Test
    public void testRunAfterBoth() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            return 20;
        });

        CompletableFuture<Void> completableFuture3 = completableFuture1.runAfterBoth(completableFuture2, () -> {
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数无入参，无返回值
     * <p>
     * 回调函数公用两个任务其中一个线程执行
     * </p>
     */
    @Test
    public void testRunAfterBothException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            int a = 1 / 0;
            return 20;
        });

        CompletableFuture<Void> completableFuture3 = completableFuture1.runAfterBoth(completableFuture2, () -> {
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1和任务2都执行成功，再执行回调函数<br/>
     * 回调函数无入参，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testRunAfterBothAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            return 20;
        }, executorService);

        CompletableFuture<Void> completableFuture3 = completableFuture1.runAfterBothAsync(completableFuture2, () -> {
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数无入参，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testRunAfterBothAsyncException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            int a = 1 / 0;
            return 20;
        }, executorService);

        CompletableFuture<Void> completableFuture3 = completableFuture1.runAfterBothAsync(completableFuture2, () -> {
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1或任务2执行成功，再执行回调函数<br/>
     * 回调函数入参为第一个完成的任务的返回结果，并带有返回值
     * <p>
     * 回调函数和第一个完成的任务使用相同线程执行
     * </p>
     */
    @Test
    public void testApplyToEither() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        });

        CompletableFuture<Integer> completableFuture3 = completableFuture1.applyToEither(completableFuture2, (result) -> {
            log.info("回调函数获取到结果：" + result);
            log.info("回调函数执行中...");
            return result * 10;
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数入参为第一个完成的任务的返回结果，并带有返回值
     * <p>
     * 回调函数和第一个完成的任务使用相同线程执行
     * </p>
     */
    @Test
    public void testApplyToEitherException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 20;
        });

        CompletableFuture<Integer> completableFuture3 = completableFuture1.applyToEither(completableFuture2, (result) -> {
            log.info("回调函数获取到结果：" + result);
            log.info("回调函数执行中...");
            return result * 10;
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1或任务2执行成功，再执行回调函数<br/>
     * 回调函数入参为第一个完成的任务的返回结果，并带有返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testApplyToEitherAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        }, executorService);

        CompletableFuture<Integer> completableFuture3 = completableFuture1.applyToEitherAsync(completableFuture2, (result) -> {
            log.info("回调函数获取到结果：" + result);
            log.info("回调函数执行中...");
            return result * 10;
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数入参为第一个完成的任务的返回结果，并带有返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testApplyToEitherAsyncException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 20;
        }, executorService);

        CompletableFuture<Integer> completableFuture3 = completableFuture1.applyToEitherAsync(completableFuture2, (result) -> {
            log.info("回调函数获取到结果：" + result);
            log.info("回调函数执行中...");
            return result * 10;
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1或任务2执行成功，再执行回调函数<br/>
     * 回调函数入参为第一个完成的任务的返回结果，无返回值
     * <p>
     * 回调函数和第一个完成的任务使用相同线程执行
     * </p>
     */
    @Test
    public void testAcceptEither() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        });

        CompletableFuture<Void> completableFuture3 = completableFuture1.acceptEither(completableFuture2, (result) -> {
            log.info("回调函数获取到结果：" + result);
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数入参为第一个完成的任务的返回结果，无返回值
     * <p>
     * 回调函数和第一个完成的任务使用相同线程执行
     * </p>
     */
    @Test
    public void testAcceptEitherException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 20;
        });

        CompletableFuture<Void> completableFuture3 = completableFuture1.acceptEither(completableFuture2, (result) -> {
            log.info("回调函数获取到结果：" + result);
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1或任务2执行成功，再执行回调函数<br/>
     * 回调函数入参为第一个完成的任务的返回结果，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testAcceptEitherAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        }, executorService);

        CompletableFuture<Void> completableFuture3 = completableFuture1.acceptEitherAsync(completableFuture2, (result) -> {
            log.info("回调函数获取到结果：" + result);
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数入参为第一个完成的任务的返回结果，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testAcceptEitherAsyncException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 20;
        }, executorService);

        CompletableFuture<Void> completableFuture3 = completableFuture1.acceptEitherAsync(completableFuture2, (result) -> {
            log.info("回调函数获取到结果：" + result);
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1或任务2执行成功，再执行回调函数<br/>
     * 回调函数无入参，无返回值
     * <p>
     * 回调函数和第一个完成的任务使用相同线程执行
     * </p>
     */
    @Test
    public void testRunAfterEither() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        });

        CompletableFuture<Void> completableFuture3 = completableFuture1.runAfterEither(completableFuture2, () -> {
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数无入参，无返回值
     * <p>
     * 回调函数和第一个完成的任务使用相同线程执行
     * </p>
     */
    @Test
    public void testRunAfterEitherException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 20;
        });

        CompletableFuture<Void> completableFuture3 = completableFuture1.runAfterEither(completableFuture2, () -> {
            log.info("回调函数执行中...");
        });

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1或任务2执行成功，再执行回调函数<br/>
     * 回调函数无入参，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testRunAfterEitherAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        }, executorService);

        CompletableFuture<Void> completableFuture3 = completableFuture1.runAfterEitherAsync(completableFuture2, () -> {
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1执行成功，任务2执行失败，再执行回调函数<br/>
     * 回调函数无入参，无返回值
     * <p>
     * 提交给线程池，不同线程执行
     * </p>
     */
    @Test
    public void testRunAfterEitherAsyncException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        }, executorService);

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 20;
        }, executorService);

        CompletableFuture<Void> completableFuture3 = completableFuture1.runAfterEitherAsync(completableFuture2, () -> {
            log.info("回调函数执行中...");
        }, executorService);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("回调函数返回结果：" + completableFuture3.get());
    }

    /**
     * 任务1、任务2、任务3都执行成功，返回null
     */
    @Test
    public void testAllOf() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        });

        CompletableFuture<Integer> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 3 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 30;
        });

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFuture1, completableFuture2, completableFuture3);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("任务 3 返回结果：" + completableFuture3.get());
        log.info("全部任务返回结果：" + completableFuture.get());
    }

    /**
     * 任务1、任务3执行成功，任务2执行异常
     */
    @Test
    public void testAllOfException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 20;
        });

        CompletableFuture<Integer> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 3 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 30;
        });

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFuture1, completableFuture2, completableFuture3);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("任务 3 返回结果：" + completableFuture3.get());
        log.info("全部任务返回结果：" + completableFuture.get());
    }

    /**
     * 任务1、任务2、任务3只要有一个任务执行成功，返回该任务结果
     */
    @Test
    public void testAnyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        });

        CompletableFuture<Integer> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 3 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 30;
        });

        CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(completableFuture1, completableFuture2, completableFuture3);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("任务 3 返回结果：" + completableFuture3.get());
        log.info("全部任务返回结果：" + completableFuture.get());
    }

    /**
     * 任务1、任务3执行成功，任务2执行异常
     */
    @Test
    public void testAnyOfException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 2 执行中...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 20;
        });

        CompletableFuture<Integer> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 3 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 30;
        });

        CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(completableFuture1, completableFuture2, completableFuture3);

        log.info("任务 1 返回结果：" + completableFuture1.get());
        log.info("任务 2 返回结果：" + completableFuture2.get());
        log.info("任务 3 返回结果：" + completableFuture3.get());
        log.info("全部任务返回结果：" + completableFuture.get());
    }

    /**
     * 测试获取结果
     */
    @Test
    public void testGet() {
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        try {
            log.info("任务 1 返回结果：" + completableFuture1.get());
        } catch (InterruptedException e) {
            log.error("中断异常");
        } catch (ExecutionException e) {
            log.error("执行异常");
        }

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");
    }

    /**
     * 测试获取结果，超时自动退出
     */
    @Test
    public void testGetTimeout() {
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        try {
            log.info("任务 1 返回结果：" + completableFuture1.get(2, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            log.error("中断异常");
        } catch (ExecutionException e) {
            log.error("执行异常");
        } catch (TimeoutException e) {
            log.error("超时异常");
        }

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");
    }

    /**
     * 测试获取结果，未完成返回期预期
     */
    @Test
    public void testGetNow() {
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        log.info("任务 1 返回结果：" + completableFuture1.getNow(-1));

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");
    }

    /**
     * 测试获取结果，不会抛出异常
     */
    @Test
    public void testJoin() {
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 10;
        });

        log.info("任务 1 返回结果：" + completableFuture1.join());

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");
    }

    /**
     * 测试通知任务完成，未完成返回预期值
     */
    @Test
    public void testComplete() {
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 10;
        });

        new Thread(() -> {
            completableFuture1.complete(-2);
        }, "其他线程").start();

        try {
            log.info("任务 1 返回结果：" + completableFuture1.get());
        } catch (InterruptedException e) {
            log.error("中断异常");
        } catch (ExecutionException e) {
            log.error("执行异常");
        }

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");
    }

    /**
     * 测试通知任务完成，未完成返回预期异常
     */
    @Test
    public void testCompleteExceptionally() {
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("任务 1 执行中...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            return 10;
        });

        new Thread(() -> {
            completableFuture1.completeExceptionally(new RuntimeException("提前完成异常"));
        }, "其他线程").start();

        try {
            log.info("任务 1 返回结果：" + completableFuture1.get());
        } catch (InterruptedException e) {
            log.error("中断异常");
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RuntimeException) {
                log.error("预期异常：{}", e.getCause().getMessage());
            } else {
                log.error("非预期异常");
            }
        }

        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeMillis() + " ms");
    }

}
