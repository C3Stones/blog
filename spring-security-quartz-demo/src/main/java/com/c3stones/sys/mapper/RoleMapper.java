package com.c3stones.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c3stones.sys.entity.Role;

/**
 * 系统角色Mapper
 * 
 * @author CL
 *
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

	/**
	 * 根据用户ID查询
	 * 
	 * @param userId 用户ID
	 * @return 系统角色列表
	 */
	@Select("SELECT r.* FROM t_sys_role r LEFT JOIN t_sys_user_role ur ON ur.role_id = r.id WHERE ur.user_id = #{userId}")
	List<Role> findByUserId(Integer userId);

}