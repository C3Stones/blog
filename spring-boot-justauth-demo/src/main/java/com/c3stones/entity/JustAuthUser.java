package com.c3stones.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;

/**
 * 授权用户信息
 * 
 * @author CL
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_ja_user")
public class JustAuthUser extends AuthUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户第三方系统的唯一id。在调用方集成该组件时，可以用uuid + source唯一确定一个用户
	 */
	@TableId(type = IdType.INPUT)
	private String uuid;

	/**
	 * 用户授权的token信息
	 */
	@TableField(exist = false)
	private AuthToken token;

	/**
	 * 第三方平台返回的原始用户信息
	 */
	@TableField(exist = false)
	private JSONObject rawUserInfo;

	/**
	 * 自定义构造函数
	 * 
	 * @param authUser 授权成功后的用户信息，根据授权平台的不同，获取的数据完整性也不同
	 */
	public JustAuthUser(AuthUser authUser) {
		super(authUser.getUuid(), authUser.getUsername(), authUser.getNickname(), authUser.getAvatar(),
				authUser.getBlog(), authUser.getCompany(), authUser.getLocation(), authUser.getEmail(),
				authUser.getRemark(), authUser.getGender(), authUser.getSource(), authUser.getToken(),
				authUser.getRawUserInfo());
	}

}
