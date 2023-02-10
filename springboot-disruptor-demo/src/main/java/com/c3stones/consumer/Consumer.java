package com.c3stones.consumer;

import com.c3stones.model.Message;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者
 *
 * @author CL
 */
@Slf4j
@RequiredArgsConstructor
public class Consumer implements WorkHandler<Message>, EventHandler<Message> {

    private final String desc;

    /**
     * 重复消费：每个消费者重复消费生产者生产的数据
     *
     * @param message    消息
     * @param sequence   当前序列号
     * @param endOfBatch 批次结束标识（常用于将多个消费着的数据依次组合到最后一个消费者统一处理）
     */
    @Override
    public void onEvent(Message message, long sequence, boolean endOfBatch) {
        this.onEvent(message);
    }

    /**
     * 分组消费：每个生产者生产的数据只能被一个消费者消费
     *
     * @param message 消息
     */
    @Override
    public void onEvent(Message message) {
        log.info("Group Consumer describe: {}, message: {}", desc, message);
    }

}
