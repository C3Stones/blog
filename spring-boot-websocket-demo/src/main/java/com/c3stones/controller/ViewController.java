package com.c3stones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面视图Controller
 * 
 * @author CL
 *
 */
@Controller
public class ViewController {

	/**
	 * 跳转到<b> 自己给自己发送消息 </b>页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selfToSelf")
	public String selfToSelf() {
		return "selfToSelfClient";
	}

	/**
	 * 跳转到<b> 自己给其他用户发送消息 </b>页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selfToOther")
	public String selfToOther() {
		return "selfToOtherClient";
	}

	/**
	 * 跳转到<b> 自己给指定用户发送消息 </b>页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selfToSpecific")
	public String selfToSpecific() {
		return "selfToSpecificClient";
	}

}
