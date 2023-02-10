package com.c3stones;

import com.c3stones.consumer.RepeatConsumer;
import com.c3stones.model.Message;
import com.c3stones.producer.Producer;
import com.lmax.disruptor.dsl.Disruptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * 重复消费 单元测试
 *
 * @author CL
 */
public class RepeatConsumerTest {

    private Disruptor<Message> disruptor;

    @Before
    public void init() {
        RepeatConsumer a = new RepeatConsumer(1);
        RepeatConsumer b = new RepeatConsumer(2);

        disruptor = new Disruptor(Message::new, 1024, Executors.defaultThreadFactory());

        disruptor.handleEventsWith(a, b);

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
