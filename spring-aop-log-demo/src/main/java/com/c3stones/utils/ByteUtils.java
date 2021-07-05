package com.c3stones.utils;

/**
 * 字节转换工具类
 * 
 * @author CL
 */
public class ByteUtils {

	private static final int UNIT = 1024;

	/**
	 * 格式化字节大小
	 * 
	 * @param byteSize 字节大小
	 * @return
	 */
	public static String formatByteSize(long byteSize) {

		if (byteSize <= -1) {
			return String.valueOf(byteSize);
		}

		double size = 1.0 * byteSize;

		String type = "B";
		if ((int) Math.floor(size / UNIT) <= 0) { // 不足1KB
			type = "B";
			return format(size, type);
		}

		size = size / UNIT;
		if ((int) Math.floor(size / UNIT) <= 0) { // 不足1MB
			type = "KB";
			return format(size, type);
		}

		size = size / UNIT;
		if ((int) Math.floor(size / UNIT) <= 0) { // 不足1GB
			type = "MB";
			return format(size, type);
		}

		size = size / UNIT;
		if ((int) Math.floor(size / UNIT) <= 0) { // 不足1TB
			type = "GB";
			return format(size, type);
		}

		size = size / UNIT;
		if ((int) Math.floor(size / UNIT) <= 0) { // 不足1PB
			type = "TB";
			return format(size, type);
		}

		size = size / UNIT;
		if ((int) Math.floor(size / UNIT) <= 0) {
			type = "PB";
			return format(size, type);
		}
		return ">PB";
	}

	/**
	 * 格式化字节大小为指定单位
	 * 
	 * @param size 字节大小
	 * @param type 单位类型
	 * @return
	 */
	private static String format(double size, String type) {
		int precision = 0;

		if (size * 1000 % 10 > 0) {
			precision = 3;
		} else if (size * 100 % 10 > 0) {
			precision = 2;
		} else if (size * 10 % 10 > 0) {
			precision = 1;
		} else {
			precision = 0;
		}

		String formatStr = "%." + precision + "f";

		if ("KB".equals(type)) {
			return String.format(formatStr, (size)) + "KB";
		} else if ("MB".equals(type)) {
			return String.format(formatStr, (size)) + "MB";
		} else if ("GB".equals(type)) {
			return String.format(formatStr, (size)) + "GB";
		} else if ("TB".equals(type)) {
			return String.format(formatStr, (size)) + "TB";
		} else if ("PB".equals(type)) {
			return String.format(formatStr, (size)) + "PB";
		}
		return String.format(formatStr, (size)) + "B";
	}

}
