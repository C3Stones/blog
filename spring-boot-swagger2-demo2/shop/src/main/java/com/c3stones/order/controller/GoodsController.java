package com.c3stones.order.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.order.entity.Goods;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 商品Controller
 * 
 * @author CL
 *
 */
@Api(tags = "Goods / 商品信息")
@RestController
@RequestMapping(value = "/goods")
public class GoodsController {

	/**
	 * 获取商品列表
	 * 
	 * @param user 商品信息
	 * @return
	 */
	@SuppressWarnings("serial")
	@RequestMapping(value = "/listData", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取商品列表")
	public List<Goods> listData() {
		return new ArrayList<Goods>() {
			{
				add(new Goods(1L, "手机", 1799.0));
				add(new Goods(2L, "电脑", 6899.0));
			}
		};
	}

}
