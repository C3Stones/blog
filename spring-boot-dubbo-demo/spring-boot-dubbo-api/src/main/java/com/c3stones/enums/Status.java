package com.c3stones.enums;

/**
 * 状态枚举
 * 
 * @author CL
 *
 */
public enum Status {

	/**
	 * 正常状态
	 */
	NORMAL("0", "正常"),
	/**
	 * 删除状态
	 */
	DELETE("1", "删除"),
	/**
	 * 禁用状态
	 */
	DISABLE("2", "禁用");

	/**
	 * 状态值
	 */
	private String value;

	/**
	 * 状态标签
	 */
	private String label;

	private Status(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 翻译状态
	 * 
	 * @param value 状态值
	 * @return
	 */
	public static String transf(String value) {
		for (Status s : Status.values()) {
			if (value.equals(s.getValue())) {
				return s.label;
			}
		}
		return null;
	}

}
