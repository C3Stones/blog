package com.c3stones.consumer;

import com.c3stones.model.Message;
import com.lmax.disruptor.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 重复消费
 *
 * @author CL
 */
@Slf4j
@RequiredArgsConstructor
public class RepeatConsumer implements EventHandler<Message> {

    /**
     * 消费着编号
     */
    private final Integer number;

    /**
     * 重复消费：每个消费者重复消费生产者生产的数据
     *
     * @param message    消息
     * @param sequence   当前序列号
     * @param endOfBatch 批次结束标识（常用于将多个消费着的数据依次组合到最后一个消费者统一处理）
     */
    @Override
    public void onEvent(Message message, long sequence, boolean endOfBatch) {
        log.info("Repeat Consumer number: {}, message: {}, curr sequence: {}, is end: {}",
                number, message, sequence, endOfBatch);
    }

}
