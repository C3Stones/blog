package com.c3stones.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.c3stones.entity.JustAuthUser;

/**
 * 授权用户Service
 * 
 * @author CL
 *
 */
public interface JustAuthUserService extends IService<JustAuthUser> {

	/**
	 * 保存或更新授权用户
	 * 
	 * @param justAuthUser 授权用户
	 * @return
	 */
	boolean saveOrUpdate(JustAuthUser justAuthUser);

	/**
	 * 根据用户uuid查询信息
	 * 
	 * @param uuid 用户uuid
	 * @return
	 */
	JustAuthUser getByUuid(String uuid);

	/**
	 * 根据用户uuid移除信息
	 * 
	 * @param uuid 用户uuid
	 * @return
	 */
	boolean removeByUuid(String uuid);

}
