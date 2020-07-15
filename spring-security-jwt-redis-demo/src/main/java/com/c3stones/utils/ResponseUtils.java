package com.c3stones.utils;

import java.io.PrintWriter;

import javax.servlet.ServletResponse;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 响应结果工具类
 * 
 * @author CL
 *
 */
@Slf4j
@Data
@AllArgsConstructor
public class ResponseUtils {

	/**
	 * 返回编码
	 */
	private Integer code;

	/**
	 * 返回消息
	 */
	private String msg;

	/**
	 * 返回数据
	 */
	private Object data;

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
			out.println(JSON.toJSONString(data));
			out.flush();
		} catch (Exception e) {
			log.error("Response输出Json异常：" + e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 返回信息
	 * 
	 * @param code 返回编码
	 * @param msg  返回消息
	 * @param data 返回数据
	 * @return
	 */
	public static ResponseUtils response(Integer code, String msg, Object data) {
		return new ResponseUtils(code, msg, data);
	}

	/**
	 * 返回成功
	 * 
	 * @param data 返回数据
	 * @return
	 */
	public static ResponseUtils success(Object data) {
		return ResponseUtils.response(200, "成功", data);
	}

	/**
	 * 返回失败
	 * 
	 * @param data 返回数据
	 * @return
	 */
	public static ResponseUtils fail(Object data) {
		return ResponseUtils.response(500, "失败", data);
	}

}