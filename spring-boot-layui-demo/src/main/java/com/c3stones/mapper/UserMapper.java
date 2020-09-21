package com.c3stones.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.entity.User;

/**
 * 系统用户Mapper
 * 
 * @author CL
 *
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
