package com.c3stones.common.vo;

import java.io.PrintWriter;

import javax.servlet.ServletResponse;

import cn.hutool.json.JSONUtil;
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
public class Response<T> {

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
	 * @param code 响应码
	 * @param msg  响应消息体
	 * @param data 响应数据
	 * @return
	 */
	public static <T> Response<T> error(int code, String msg, T data) {
		return new Response<T>(code, msg, data);
	}

	/**
	 * 失败响应
	 * 
	 * @param msg 响应消息体
	 * @return
	 */
	public static <T> Response<T> error(String msg) {
		return new Response<T>(500, msg, null);
	}

	/**
	 * 成功响应
	 * 
	 * @param data 响应数据
	 * @return
	 */
	public static <T> Response<T> success(T data) {
		return new Response<T>(200, null, data);
	}

	/**
	 * 成功响应
	 * 
	 * @param msg 响应消息体
	 * @return
	 */
	public static <T> Response<T> success(String msg) {
		return new Response<T>(200, msg, null);
	}

	/**
	 * 成功响应
	 * 
	 * @param msg  响应消息体
	 * @param data 响应数据
	 * @return
	 */
	public static <T> Response<T> success(String msg, T data) {
		return new Response<T>(200, msg, data);
	}

	/**
	 * Response输出Json格式
	 * 
	 * @param response
	 * @param data     返回数据
	 */
	public static void responseJson(ServletResponse response, Object data) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			out = response.getWriter();
			out.println(JSONUtil.toJsonStr(data));
			out.flush();
		} catch (Exception e) {
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}