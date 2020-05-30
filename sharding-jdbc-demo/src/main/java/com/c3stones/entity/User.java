package com.c3stones.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息
 * 
 * @author CL
 *
 */
@Data
@TableName(value = "t_user")
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> {

	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	@TableField(value = "org_code")
	private int orgCode;

}
