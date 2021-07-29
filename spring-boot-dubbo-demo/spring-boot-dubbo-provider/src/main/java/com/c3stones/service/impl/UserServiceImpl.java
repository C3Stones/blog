package com.c3stones.service.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.entity.User;
import com.c3stones.mapper.UserMapper;
import com.c3stones.service.UserService;

/**
 * 用户Service实现
 *
 * @author CL
 */
@Service
@Component
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
