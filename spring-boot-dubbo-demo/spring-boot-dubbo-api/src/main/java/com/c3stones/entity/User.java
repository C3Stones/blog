package com.c3stones.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 用户Entity
 *
 * @author CL
 */
@TableName(value = "t_user")
public class User extends Model<User> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编码
	 */
	@TableId(type = IdType.INPUT)
	private String userCode;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 用户状态（0-可用，1-删除，2-禁用）
	 */
	private String status;

	public User() {
	}

	public User(String userCode, String userName) {
		this.userCode = userCode;
		this.userName = userName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
