package com.c3stones.common.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import lombok.extern.slf4j.Slf4j;

/**
 * Xss包装类
 * 
 * @author CL
 *
 */
@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 构造请求对象
	 * 
	 * @param request
	 */
	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 获取头部参数
	 * 
	 * @param v 参数值
	 */
	@Override
	public String getHeader(String v) {
		String header = super.getHeader(v);
		if (header == null || "".equals(header)) {
			return header;
		}
		return Jsoup.clean(super.getHeader(v), Whitelist.relaxed());
	}

	/**
	 * 获取参数
	 * 
	 * @param v 参数值
	 */
	@Override
	public String getParameter(String v) {
		String param = super.getParameter(v);
		if (param == null || "".equals(param)) {
			return param;
		}
		return Jsoup.clean(super.getParameter(v), Whitelist.relaxed());
	}

	/**
	 * 获取参数值
	 * 
	 * @param v 参数值
	 */
	@Override
	public String[] getParameterValues(String v) {
		String[] values = super.getParameterValues(v);
		if (values == null) {
			return values;
		}
		int length = values.length;
		String[] resultValues = new String[length];
		for (int i = 0; i < length; i++) {
			// 过滤特殊字符
			resultValues[i] = Jsoup.clean(values[i], Whitelist.relaxed()).trim();
			if (!(resultValues[i]).equals(values[i])) {
				log.debug("XSS过滤器 => 过滤前：{} => 过滤后：{}", values[i], resultValues[i]);
			}
		}
		return resultValues;
	}
}