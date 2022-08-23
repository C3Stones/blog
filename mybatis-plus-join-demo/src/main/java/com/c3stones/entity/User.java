package com.c3stones.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息
 *
 * @author CL
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_user")
public class User extends Model<User> {

	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id; // ID
	@TableField(condition = SqlCondition.LIKE)
	private String username; // 用户名称
	private String sex; // 性别
	private String phone; // 手机号

}
