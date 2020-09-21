package com.c3stones.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 全局异常处理
 * 
 * @author CL
 *
 */
@Controller
public class WebExceptionAdvice implements ErrorController {

	/**
	 * 获得异常路径
	 */
	@Override
	public String getErrorPath() {
		return "error";
	}

	/**
	 * 异常处理，跳转到响应的页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "error")
	public String handleError(HttpServletRequest request, Model model) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		model.addAttribute("message", throwable != null ? throwable.getMessage() : null);
		switch (statusCode) {
		case 400:
			return "error/400";
		case 403:
			return "error/403";
		case 404:
			return "error/404";
		default:
			return "error/500";
		}
	}

}