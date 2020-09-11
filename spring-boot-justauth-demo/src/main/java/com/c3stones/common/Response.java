package com.c3stones.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 响应实体
 * 
 * @author CL
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {

	/**
	 * 响应码
	 */
	private int code;

	/**
	 * 响应消息体
	 */
	private String msg;

	/**
	 * 失败响应
	 * 
	 * @param msg 响应消息体
	 * @return
	 */
	public static Response error(String msg) {
		return new Response(500, msg);
	}

	/**
	 * 成功响应
	 * 
	 * @param msg 响应消息体
	 * @return
	 */
	public static Response success(String msg) {
		return new Response(200, msg);
	}
	
}
