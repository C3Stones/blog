package com.c3stones.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统权限
 * 
 * @author CL
 *
 */
@Data
@TableName("t_sys_auth")
@EqualsAndHashCode(callSuper = false)
public class Auth extends Model<Auth> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 权限ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 权限名称
	 */
	private String authName;

	/**
	 * 权限标识
	 */
	private String permission;

}
