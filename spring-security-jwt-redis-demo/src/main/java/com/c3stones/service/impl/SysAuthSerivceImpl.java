package com.c3stones.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.dao.SysAuthDao;
import com.c3stones.entity.SysAuth;
import com.c3stones.service.SysAuthService;

/**
 * 系统权限Service实现
 * 
 * @author CL
 *
 */
@Service
public class SysAuthSerivceImpl extends ServiceImpl<SysAuthDao, SysAuth> implements SysAuthService {

}
