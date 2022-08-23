package com.c3stones.dto;

import com.c3stones.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户收货地址 DTO
 *
 * @author CL
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserShippingAddressDto extends User {

	private static final long serialVersionUID = 1L;

	private String address; // 地址
	private String def; // 是否默认

}
