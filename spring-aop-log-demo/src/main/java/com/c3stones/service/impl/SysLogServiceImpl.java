package com.c3stones.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.dao.SysLogDao;
import com.c3stones.entity.SysLog;
import com.c3stones.service.SysLogService;

/**
 * 系统日志Service实现
 * 
 * @author CL
 *
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLog> implements SysLogService {

}
