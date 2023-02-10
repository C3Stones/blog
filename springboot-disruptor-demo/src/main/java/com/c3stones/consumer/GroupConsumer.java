package com.c3stones.consumer;

import com.c3stones.model.Message;
import com.lmax.disruptor.WorkHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 分组消费
 *
 * @author CL
 */
@Slf4j
@RequiredArgsConstructor
public class GroupConsumer implements WorkHandler<Message> {

    /**
     * 消费着编号
     */
    private final Integer number;

    /**
     * 分组消费：每个生产者生产的数据只能被一个消费者消费
     *
     * @param message 消息
     */
    @Override
    public void onEvent(Message message) {
        log.info("Group Consumer number: {}, message: {}", number, message);
    }

}
