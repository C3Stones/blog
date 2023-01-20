package com.c3stones.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息 Mapper
 *
 * @author CL
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
