package com.c3stones.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应工具类
 *
 * @param <T>
 * @author CL
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean code;

    private String msg;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, Boolean.TRUE, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, Boolean.TRUE, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, Boolean.TRUE, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, Boolean.FALSE, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, Boolean.FALSE, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, Boolean.FALSE, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, Boolean.FALSE, msg);
    }

    private static <T> R<T> restResult(T data, Boolean code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

}