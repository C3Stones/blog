package com.c3stones.filter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SQL注入过滤器
 * 
 * @author CL
 *
 */
@Component
@ConfigurationProperties(prefix = "security.sql")
@WebFilter(filterName = "SqlInjectFilter", urlPatterns = "/*")
public class SqlInjectFilter implements Filter {

	/**
	 * 过滤器配置对象
	 */
	FilterConfig filterConfig = null;

	/**
	 * 是否启用
	 */
	private boolean enable;

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * 忽略的URL
	 */
	private List<String> excludes;

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	/**
	 * 初始化
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	/**
	 * 拦截
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		// 不启用或者已忽略的URL不拦截
		if (!enable || isExcludeUrl(request.getServletPath())) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		SqlInjectHttpServletRequestWrapper sqlInjectHttpServletRequestWrapper = new SqlInjectHttpServletRequestWrapper(
				request);
		filterChain.doFilter(sqlInjectHttpServletRequestWrapper, servletResponse);
	}

	/**
	 * 销毁
	 */
	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	/**
	 * 判断是否为忽略的URL
	 * 
	 * @param urlPath URL路径
	 * @return true-忽略，false-过滤
	 */
	private boolean isExcludeUrl(String url) {
		if (excludes == null || excludes.isEmpty()) {
			return false;
		}
		return excludes.stream().map(pattern -> Pattern.compile("^" + pattern)).map(p -> p.matcher(url))
				.anyMatch(Matcher::find);
	}
}