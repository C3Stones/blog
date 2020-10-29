package com.c3stones.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import com.c3stones.security.UserAuthenticationProvider;
import com.c3stones.security.UserPermissionEvaluator;
import com.c3stones.security.handle.UserAccessDeniedHandler;
import com.c3stones.security.handle.UserLoginFailureHandler;
import com.c3stones.security.handle.UserLoginSuccessHandler;
import com.c3stones.security.handle.UserLogoutSuccessHandler;
import com.c3stones.security.handle.UserNotLoginHandler;

import lombok.Setter;

/**
 * 系统安全核心配置
 * 
 * @author CL
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConfigurationProperties(prefix = "security.web")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 忽略的URL
	 */
	@Setter
	private List<String> excludes;

	/**
	 * 无权限处理类
	 */
	@Autowired
	private UserAccessDeniedHandler userAccessDeniedHandler;

	/**
	 * 用户未登录处理类
	 */
	@Autowired
	private UserNotLoginHandler userNotLoginHandler;

	/**
	 * 用户登录成功处理类
	 */
	@Autowired
	private UserLoginSuccessHandler userLoginSuccessHandler;

	/**
	 * 用户登录失败处理类
	 */
	@Autowired
	private UserLoginFailureHandler userLoginFailureHandler;

	/**
	 * 用户登出成功处理类
	 */
	@Autowired
	private UserLogoutSuccessHandler userLogoutSuccessHandler;

	/**
	 * 用户登录验证
	 */
	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;

	/**
	 * 用户权限注解
	 */
	@Autowired
	private UserPermissionEvaluator userPermissionEvaluator;

	/**
	 * 加密方式
	 * 
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 注入自定义PermissionEvaluator
	 * 
	 * @return
	 */
	@Bean
	public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setPermissionEvaluator(userPermissionEvaluator);
		return handler;
	}

	/**
	 * 用户登录验证
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(userAuthenticationProvider);
	}

	/**
	 * 安全权限配置
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin() // 可以相同域名页面的frame中展示
				.and().authorizeRequests() // 权限配置
				.antMatchers(excludes.toArray(new String[excludes.size()])).permitAll()// 获取白名单（不进行权限验证）
				.anyRequest().authenticated() // 其他的需要登陆后才能访问
				.and().httpBasic().authenticationEntryPoint(userNotLoginHandler) // 配置未登录处理类
				.and().formLogin().loginPage("/login").loginProcessingUrl("/login") // 配置登录URL
				.successHandler(userLoginSuccessHandler) // 配置登录成功处理类
				.failureHandler(userLoginFailureHandler) // 配置登录失败处理类
				.and().logout().logoutUrl("/logout")// 配置登出地址
				.logoutSuccessHandler(userLogoutSuccessHandler) // 配置用户登出处理类
				.and().exceptionHandling().accessDeniedHandler(userAccessDeniedHandler)// 配置没有权限处理类
				.and().csrf().disable(); // 禁用跨站请求伪造防护
	}

}