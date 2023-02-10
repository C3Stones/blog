package com.c3stones.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Object data;

    @Override
    public String toString() {
        return data.toString();
    }

}
