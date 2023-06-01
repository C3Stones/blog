package com.c3stones.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author CL
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 创建表
     */
    void createTable();

    /**
     * 清除表
     */
    void dropTable();

}