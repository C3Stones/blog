package com.c3stones.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统角色
 * 
 * @author CL
 *
 */
@Data
@TableName("t_sys_role")
@EqualsAndHashCode(callSuper = false)
public class Role extends Model<Role> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色编码
	 */
	private String roleCode;

}
