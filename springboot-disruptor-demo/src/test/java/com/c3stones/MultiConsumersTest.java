package com.c3stones;

import com.c3stones.consumer.Consumer;
import com.c3stones.model.Message;
import com.c3stones.producer.Producer;
import com.lmax.disruptor.dsl.Disruptor;
import org.junit.Test;

import java.util.concurrent.Executors;

/**
 * 多个消费者 单元测试
 *
 * @author CL
 */
public class MultiConsumersTest {

    /**
     * 测试链路模式
     * <p>
     * p => a -> b -> c
     * </p>
     */
    @Test
    public void testChain() throws InterruptedException {
        Consumer a = new Consumer("a");
        Consumer b = new Consumer("b");
        Consumer c = new Consumer("c");

        Disruptor<Message> disruptor = new Disruptor(Message::new, 1024, Executors.defaultThreadFactory());

        disruptor.handleEventsWith(a).then(b).then(c);

        disruptor.start();

        Producer producer = new Producer(disruptor);

        producer.send("Chain");

        Thread.sleep(1000);

        disruptor.shutdown();
    }

    /**
     * 测试钻石模式
     * <p>
     *           a
     *        ↗     ↘
     *  p =>            c
     *        ↘     ↗
     *           b
     * </p>
     */
    @Test
    public void testDiamond() throws InterruptedException {
        Consumer a = new Consumer("a");
        Consumer b = new Consumer("b");
        Consumer c = new Consumer("c");

        Disruptor<Message> disruptor = new Disruptor(Message::new, 1024, Executors.defaultThreadFactory());

        disruptor.handleEventsWithWorkerPool(a, b).then(c);

        disruptor.start();

        Producer producer = new Producer(disruptor);

        producer.send("Diamond1");
        producer.send("Diamond2");

        Thread.sleep(1000);

        disruptor.shutdown();
    }

}
