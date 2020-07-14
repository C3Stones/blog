package com.c3stones.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 系统角色
 * 
 * @author CL
 *
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableId
	private Long id;

	/**
	 * 角色名称
	 */
	private String roleName;

}
