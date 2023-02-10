package com.c3stones.producer;

import com.c3stones.model.Message;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.RequiredArgsConstructor;

/**
 * 生产者
 *
 * @author CL
 */
@RequiredArgsConstructor
public class Producer {

    private final Disruptor disruptor;

    /**
     * 发送数据
     *
     * @param data 数据
     */
    public void send(Object data) {
        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        // 获取可以生成的位置
        long next = ringBuffer.next();
        try {
            Message msg = ringBuffer.get(next);
            msg.setData(data);
        } finally {
            ringBuffer.publish(next);
        }
    }

}
