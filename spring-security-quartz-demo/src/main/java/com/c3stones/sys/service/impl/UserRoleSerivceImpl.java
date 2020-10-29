package com.c3stones.sys.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.sys.entity.UserRole;
import com.c3stones.sys.mapper.UserRoleMapper;
import com.c3stones.sys.service.UserRoleService;

/**
 * 用户角色关系Service实现
 * 
 * @author CL
 *
 */
@Service
public class UserRoleSerivceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
