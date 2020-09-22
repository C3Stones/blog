package com.c3stones.auth.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.auth.entity.JustAuthUser;
import com.c3stones.auth.mapper.JustAuthUserMapper;
import com.c3stones.auth.service.JustAuthUserService;

/**
 * 授权用户Service实现
 * 
 * @author CL
 *
 */
@Service
public class JustAuthUserServiceImpl extends ServiceImpl<JustAuthUserMapper, JustAuthUser>
		implements JustAuthUserService {

}
