package com.c3stones.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 系统用户
 * 
 * @author CL
 *
 */
@Data
@TableName("sys_log")
public class SysLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
	private Long id;

	/**
	 * 日志类型
	 */
	private String logType;

	/**
	 * 创建用户编码
	 */
	private String createUserCode;

	/**
	 * 创建用户名称
	 */
	private String createUserName;

	/**
	 * 创建时间
	 */
	private LocalDateTime createDate;

	/**
	 * 请求URI
	 */
	private String requestUri;

	/**
	 * 请求方式
	 */
	private String requestMethod;

	/**
	 * 请求参数
	 */
	private String requestParams;

	/**
	 * 请求IP
	 */
	private String requestIp;

	/**
	 * 请求服务器地址
	 */
	private String serverAddress;

	/**
	 * 是否异常
	 */
	private String isException;

	/**
	 * 异常信息
	 */
	private String exceptionInfo;

	/**
	 * 开始时间
	 */
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	private LocalDateTime endTime;

	/**
	 * 执行时间
	 */
	private Long executeTime;

	/**
	 * 用户代理
	 */
	private String userAgent;

	/**
	 * 操作系统
	 */
	private String deviceName;

	/**
	 * 浏览器名称
	 */
	private String browserName;

}
