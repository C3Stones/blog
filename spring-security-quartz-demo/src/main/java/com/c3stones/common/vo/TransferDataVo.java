package com.c3stones.common.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 穿梭框数据Vo
 * 
 * @author CL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDataVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 值
	 */
	private Object value;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	/**
	 * 是否选中
	 */
	private Boolean checked;

}