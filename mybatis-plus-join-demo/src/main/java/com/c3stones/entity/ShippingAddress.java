package com.c3stones.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货地址信息
 *
 * @author CL
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_shipping_address")
public class ShippingAddress extends Model<ShippingAddress> {

	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id; // ID
	private Integer userId; // 用户ID
	private String address; // 地址
	private String isDefault; // 是否默认

}
