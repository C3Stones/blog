package com.c3stones.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * Session初始化，向Servlet容器中添加SpringSessionRepositoryFilter
 * 
 * @author CL
 *
 */
public class SessionInitializer extends AbstractHttpSessionApplicationInitializer {

	public SessionInitializer() {
		super(SessionConfig.class);
	}

}