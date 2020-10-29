package com.c3stones.sys.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.sys.entity.RoleAuth;
import com.c3stones.sys.mapper.RoleAuthMapper;
import com.c3stones.sys.service.RoleAuthService;

/**
 * 角色权限关系Service实现
 * 
 * @author CL
 *
 */
@Service
public class RoleAuthSerivceImpl extends ServiceImpl<RoleAuthMapper, RoleAuth> implements RoleAuthService {

}