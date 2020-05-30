package com.c3stones.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * 用户保存DTO
 *
 * @author CL
 */
@Data
public class UserSaveDto {

	/**
	 * 用户名称
	 */
	@NotEmpty(message = "用户名称不能为空")
	@Length(min = 6, max = 12, message = "账号长度为 6-12 位")
	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "用户名称格式为数字或字母")
	private String username;

	/**
	 * 密码
	 */
	@NotEmpty(message = "密码不能为空")
	@Length(min = 6, max = 18, message = "密码长度为 6-18 位")
	private String password;
}
