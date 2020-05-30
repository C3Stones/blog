package com.c3stones.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	/**
	 * 用户保存分组
	 * 
	 */
	public interface UserSaveGroup {
	}

	/**
	 * 用户更新分组
	 * 
	 */
	public interface UserUpdateGroup {
	}

	/**
	 * ID
	 */
	@NotNull(message = "ID不能为空", groups = { UserUpdateGroup.class })
	private Integer id;

	/**
	 * 用户名称
	 */
	@NotEmpty(message = "用户名称不能为空", groups = { UserSaveGroup.class, UserUpdateGroup.class })
	private String username;

	/**
	 * 密码
	 */
	@NotEmpty(message = "密码不能为空", groups = { UserSaveGroup.class, UserUpdateGroup.class })
	private String password;
}
