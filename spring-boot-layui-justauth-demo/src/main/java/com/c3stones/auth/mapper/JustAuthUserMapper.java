package com.c3stones.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.auth.entity.JustAuthUser;

/**
 * 授权用户Mapper
 * 
 * @author CL
 *
 */
@Mapper
public interface JustAuthUserMapper extends BaseMapper<JustAuthUser> {

}
