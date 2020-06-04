package com.c3stones.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务Controller
 * 
 * @author CL
 *
 */
@RestController
public class BookController {

	/**
	 * 业务接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get")
	public String get() {
		return "Consul-2";
	}

}
