package com.c3stones.compent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应实体
 *
 * @author CL
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    /**
     * 成功响应码
     */
    public static final int SUCCESS_CODE = 200;
    /**
     * 异常响应码
     */
    public static final int ERROR_CODE = 500;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息体
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 失败响应
     *
     * @param msg 响应消息体
     * @return
     */
    public static <T> Response<T> error(String msg) {
        return new Response<T>(ERROR_CODE, msg, null);
    }

    /**
     * 成功响应
     *
     * @return
     */
    public static <T> Response<T> success() {
        return new Response<T>(SUCCESS_CODE, null, null);
    }

    /**
     * 成功响应
     *
     * @param data 响应数据
     * @return
     */
    public static <T> Response<T> success(T data) {
        return new Response<T>(SUCCESS_CODE, null, data);
    }

    /**
     * 成功响应
     *
     * @param msg 响应消息体
     * @return
     */
    public static <T> Response<T> success(String msg) {
        return new Response<T>(SUCCESS_CODE, msg, null);
    }

    /**
     * 成功响应
     *
     * @param msg  响应消息体
     * @param data 响应数据
     * @return
     */
    public static <T> Response<T> success(String msg, T data) {
        return new Response<T>(SUCCESS_CODE, msg, data);
    }

}