package com.c3stones.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.sys.entity.Auth;

/**
 * 系统权限Mapper
 * 
 * @author CL
 *
 */
@Mapper
public interface AuthMapper extends BaseMapper<Auth> {

	/**
	 * 根据用户ID查询
	 * 
	 * @param userId 用户ID
	 * @return 系统权限列表
	 */
	@Select("SELECT a.* FROM t_sys_auth a LEFT JOIN t_sys_role_auth ra ON ra.auth_id = a.id LEFT JOIN t_sys_user_role ur ON ur.role_id = ra.role_id WHERE ur.user_id = #{userId}")
	List<Auth> findByUserId(Integer userId);

}