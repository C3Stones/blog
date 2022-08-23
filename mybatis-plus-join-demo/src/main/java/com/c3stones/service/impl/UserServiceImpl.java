package com.c3stones.service.impl;

import com.c3stones.entity.User;
import com.c3stones.mapper.UserMapper;
import com.c3stones.service.UserService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户 Service实现
 *
 * @author CL
 */
@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, User> implements UserService {
}
