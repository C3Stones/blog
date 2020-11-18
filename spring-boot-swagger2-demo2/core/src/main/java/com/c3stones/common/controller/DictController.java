package com.c3stones.common.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.c3stones.common.entity.Dict;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户Controller
 * 
 * @author CL
 *
 */
@Api(tags = "Dict / 数据字典")
@RestController
@RequestMapping(value = "/dict")
public class DictController {

	@SuppressWarnings("serial")
	private static final List<Dict> dictList = new ArrayList<Dict>(3) {
		{
			add(new Dict(1L, "user_status", null, null, "用户状态"));
			add(new Dict(2L, null, "user_status", "0", "正常"));
			add(new Dict(3L, null, "user_status", "1", "删除"));
		}
	};

	/**
	 * 获取数据字典值
	 * 
	 * @param user 用户信息
	 * @return
	 */
	@RequestMapping(value = "/getValue", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取数据字典值")
	@ApiImplicitParams({ @ApiImplicitParam(name = "parentCode", value = "父级编码", required = true, type = "String"),
			@ApiImplicitParam(name = "dictKey", value = "字典Key", required = true, type = "String") })
	private Dict getValue(Dict dict) {
		List<Dict> list = dictList.stream().filter(
				d -> ((dict.getParentCode()).equals(d.getParentCode()) && (dict.getDictKey()).equals(d.getDictKey())))
				.collect(Collectors.toList());
		return CollectionUtil.isEmpty(list) ? null : list.get(0);
	}

}
