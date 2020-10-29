package com.c3stones.common.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * SQL注入包装类
 * 
 * @author CL
 *
 */
@Slf4j
public class SqlInjectHttpServletRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 构造请求对象
	 * 
	 * @param request
	 */
	public SqlInjectHttpServletRequestWrapper(HttpServletRequest request) {
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
		return sqlFilter(header);
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
		return sqlFilter(param);
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

		// 富文本内容不过滤
		String richTextParam = "remarks";
		if ((richTextParam).equals(v)) {
			return values;
		}

		int length = values.length;
		String[] resultValues = new String[length];
		for (int i = 0; i < length; i++) {
			// 过滤特殊字符
			resultValues[i] = sqlFilter(values[i]);
			if (!(resultValues[i]).equals(values[i])) {
				log.debug("SQL注入过滤器 => 过滤前：{} => 过滤后：{}", values[i], resultValues[i]);
			}
		}
		return resultValues;
	}

	/**
	 * 预编译SQL过滤正则表达式
	 */
	private Pattern sqlPattern = Pattern.compile(
			"(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)",
			Pattern.CASE_INSENSITIVE);

	/**
	 * SQL过滤
	 * 
	 * @param v 参数值
	 * @return
	 */
	private String sqlFilter(String v) {
		if (v != null) {
			String resultVal = v;
			Matcher matcher = sqlPattern.matcher(resultVal);
			if (matcher.find()) {
				resultVal = matcher.replaceAll("");
			}
			if (!resultVal.equals(v)) {
				return "";
			}
			return resultVal;
		}
		return null;
	}
}