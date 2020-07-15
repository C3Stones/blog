package com.c3stones.entity;

import java.io.Serializable;

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
@TableName("sys_user")
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 状态（0-正常，1-删除，2-禁用）
	 */
	private String status;
}
