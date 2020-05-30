package com.c3stones.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.entity.Goods;

/**
 * 商品Controller
 * 
 * @author CL
 *
 */
@RestController
@RequestMapping("/goods")
@Validated
public class GoodsController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PostMapping("/save")
	public String save(@Valid Goods goods) {
		logger.info("保存商品信息：", goods.toString());
		return "save success";
	}

}
