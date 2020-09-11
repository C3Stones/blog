package com.c3stones.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.cache.JustAuthTokenCache;
import com.c3stones.entity.JustAuthUser;
import com.c3stones.mapper.JustAuthUserMapper;
import com.c3stones.service.JustAuthUserService;

/**
 * 授权用户Service实现
 * 
 * @author CL
 *
 */
@Service
public class JustAuthUserServiceImpl extends ServiceImpl<JustAuthUserMapper, JustAuthUser>
		implements JustAuthUserService {

	@Autowired
	private JustAuthTokenCache justAuthTokenCache;

	/**
	 * 保存或更新授权用户
	 * 
	 * @param justAuthUser 授权用户
	 * @return
	 */
	@Override
	public boolean saveOrUpdate(JustAuthUser justAuthUser) {
		justAuthTokenCache.saveorUpdate(justAuthUser.getUuid(), justAuthUser.getToken());
		return super.saveOrUpdate(justAuthUser);
	}

	/**
	 * 根据用户uuid查询信息
	 * 
	 * @param uuid 用户uuid
	 * @return
	 */
	@Override
	public JustAuthUser getByUuid(String uuid) {
		JustAuthUser justAuthUser = super.getById(uuid);
		if (justAuthUser != null) {
			justAuthUser.setToken(justAuthTokenCache.getByUuid(uuid));
		}
		return justAuthUser;
	}

	/**
	 * 根据用户uuid移除信息
	 * 
	 * @param uuid 用户uuid
	 * @return
	 */
	@Override
	public boolean removeByUuid(String uuid) {
		justAuthTokenCache.remove(uuid);
		return super.removeById(uuid);
	}

}
