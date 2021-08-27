package com.c3stones.aspect;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.c3stones.constants.Global;
import com.c3stones.entity.SysLog;
import com.c3stones.service.SysLogService;
import com.c3stones.utils.ByteUtils;
import com.c3stones.utils.R;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import lombok.extern.log4j.Log4j2;

/**
 * 系统日志切面
 * 
 * @author CL
 *
 */
@Log4j2
@Aspect
@Component
public class SysLogAspect {

	private ThreadLocal<SysLog> sysLogThreadLocal = new ThreadLocal<>();

	@Autowired
	private Executor customThreadPoolTaskExecutor;

	@Autowired
	private SysLogService sysLogService;

	/**
	 * 日志切点
	 */
	@Pointcut("execution(public * com.c3stones.controller.*.*(..))")
	public void sysLogAspect() {
	}

	/**
	 * 前置通知
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before(value = "sysLogAspect()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) Objects
				.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

		SysLog sysLog = new SysLog();
		// 创建人信息请根据实际项目获取方式获取
		sysLog.setCreateUserCode("");
		sysLog.setCreateUserName("");
		sysLog.setStartTime(LocalDateTime.now());
		sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		sysLog.setRequestParams(formatParams(request.getParameterMap()));
		sysLog.setRequestMethod(request.getMethod());
		sysLog.setRequestIp(ServletUtil.getClientIP(request));
		sysLog.setServerAddress(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
		String userAgentStr = request.getHeader("User-Agent");
		sysLog.setUserAgent(userAgentStr);
		UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
		sysLog.setDeviceName(userAgent.getOs().getName());
		sysLog.setBrowserName(userAgent.getBrowser().getName());

		sysLogThreadLocal.set(sysLog);

		log.info("开始计时: {}  URI: {}  IP: {}", sysLog.getStartTime(), sysLog.getRequestUri(), sysLog.getRequestIp());
	}

	/**
	 * 返回通知
	 * 
	 * @param ret
	 */
	@AfterReturning(pointcut = "sysLogAspect()", returning = "ret")
	public void doAfterReturning(Object ret) {
		SysLog sysLog = sysLogThreadLocal.get();
		sysLog.setLogType(Global.LOG_INGO);
		sysLog.setEndTime(LocalDateTime.now());
		sysLog.setExecuteTime(Long.valueOf(ChronoUnit.MILLIS.between(sysLog.getStartTime(), sysLog.getEndTime())));
		R<?> r = Convert.convert(R.class, ret);
		if (r.getCode() == Global.TRUE) {
			sysLog.setIsException(Global.NO);
		} else {
			sysLog.setIsException(Global.YES);
			sysLog.setExceptionInfo(r.getMsg());
		}
		customThreadPoolTaskExecutor.execute(new SaveLogThread(sysLog, sysLogService));
		sysLogThreadLocal.remove();

		Runtime runtime = Runtime.getRuntime();
		log.info("计时结束: {}  用时: {}ms  URI: {}  总内存: {}  已用内存: {}", sysLog.getEndTime(), sysLog.getExecuteTime(),
				sysLog.getRequestUri(), ByteUtils.formatByteSize(runtime.totalMemory()),
				ByteUtils.formatByteSize(runtime.totalMemory() - runtime.freeMemory()));
	}

	/**
	 * 异常通知
	 * 
	 * @param e
	 */
	@AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
	public void doAfterThrowable(Throwable e) {
		SysLog sysLog = sysLogThreadLocal.get();
		sysLog.setLogType(Global.LOG_ERROR);
		sysLog.setEndTime(LocalDateTime.now());
		sysLog.setExecuteTime(Long.valueOf(ChronoUnit.MINUTES.between(sysLog.getStartTime(), sysLog.getEndTime())));
		sysLog.setIsException(Global.YES);
		sysLog.setExceptionInfo(e.getMessage());
		customThreadPoolTaskExecutor.execute(new SaveLogThread(sysLog, sysLogService));
		sysLogThreadLocal.remove();
		
		Runtime runtime = Runtime.getRuntime();
		log.info("计时结束: {}  用时: {}ms  URI: {}  总内存: {}  已用内存: {}", sysLog.getEndTime(), sysLog.getExecuteTime(),
				sysLog.getRequestUri(), ByteUtils.formatByteSize(runtime.totalMemory()),
				ByteUtils.formatByteSize(runtime.totalMemory() - runtime.freeMemory()));
	}

	/**
	 * 格式化参数
	 * 
	 * @param parameterMap
	 * @return
	 */
	private String formatParams(Map<String, String[]> parameterMap) {
		if (parameterMap == null) {
			return null;
		}
		StringBuilder params = new StringBuilder();
		for (Map.Entry<String, String[]> param : (parameterMap).entrySet()) {
			if (params.length() != 0) {
				params.append("&");
			}
			params.append(param.getKey() + "=");
			if (StrUtil.endWithIgnoreCase(param.getKey(), "password")) {
				params.append("*");
			} else if (param.getValue() != null) {
				params.append(ArrayUtil.join(param.getValue(), ","));
			}
		}
		return params.toString();
	}

	/**
	 * 保存日志线程
	 * 
	 * @author CL
	 *
	 */
	private static class SaveLogThread extends Thread {
		private SysLog sysLog;
		private SysLogService sysLogService;

		public SaveLogThread(SysLog sysLog, SysLogService sysLogService) {
			this.sysLog = sysLog;
			this.sysLogService = sysLogService;
		}

		@Override
		public void run() {
			sysLog.setCreateDate(LocalDateTime.now());
			sysLogService.save(sysLog);
		}
	}
}
