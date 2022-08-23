package com.c3stones.mapper;

import com.c3stones.entity.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * @author CL
 */
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {
}
