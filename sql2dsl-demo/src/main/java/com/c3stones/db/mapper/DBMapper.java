package com.c3stones.db.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * 数据库 通用 Mapper
 *
 * @author CL
 */
@Mapper
public interface DBMapper {

    /**
     * 集合
     *
     * @param sql SQL
     * @return {@link List}
     */
    @SelectProvider(method = "getSql", type = DBMapperProvider.class)
    List<Map<String, Object>> aggregation(String sql);

    /**
     * 查询
     *
     * @param sql SQL
     * @return {@link List}
     */
    @SelectProvider(method = "getSql", type = DBMapperProvider.class)
    List<Map<String, Object>> query(String sql);

}
