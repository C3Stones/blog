package com.c3stones.utils;

import java.io.Serializable;

import com.c3stones.constants.Global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 响应工具类
 *
 * @param <T>
 * @author CL
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Boolean code;

	@Getter
	@Setter
	private String msg;

	@Getter
	@Setter
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, Global.TRUE, null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, Global.TRUE, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, Global.TRUE, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, Global.FALSE, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, Global.FALSE, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, Global.FALSE, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, Global.FALSE, msg);
	}

	private static <T> R<T> restResult(T data, Boolean code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

}