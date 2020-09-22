package com.c3stones.sys.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.sys.entity.User;

/**
 * 系统用户Mapper
 * 
 * @author CL
 *
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
