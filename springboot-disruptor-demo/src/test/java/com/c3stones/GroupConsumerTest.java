package com.c3stones;

import com.c3stones.consumer.GroupConsumer;
import com.c3stones.model.Message;
import com.c3stones.producer.Producer;
import com.lmax.disruptor.dsl.Disruptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * 分组消费 单元测试
 *
 * @author CL
 */
public class GroupConsumerTest {

    private Disruptor<Message> disruptor;

    @Before
    public void init() {
        GroupConsumer a = new GroupConsumer(1);
        GroupConsumer b = new GroupConsumer(2);

        disruptor = new Disruptor(Message::new, 1024, Executors.defaultThreadFactory());

        disruptor.handleEventsWithWorkerPool(a, b);

        disruptor.start();
    }

    @After
    public void close() {
        disruptor.shutdown();
    }

    @Test
    public void test() {
        Producer producer = new Producer(disruptor);

        Arrays.asList("aaa", "bbb").forEach(data -> producer.send(data));
    }

}
