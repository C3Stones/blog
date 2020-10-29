package com.c3stones.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限关系
 * 
 * @author CL
 *
 */
@Data
@TableName("t_sys_role_auth")
@EqualsAndHashCode(callSuper = false)
public class RoleAuth extends Model<RoleAuth> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 角色ID
	 */
	private Integer roleId;

	/**
	 * 权限ID
	 */
	private Integer authId;

}
