package com.c3stones.es.convert;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.DbType;
import com.c3stones.common.DataFactory;
import com.c3stones.es.mapper.ESMapperProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * SQL 转 DSL 单元测试
 *
 * @author CL
 */
@Slf4j
public class DSLConvertTest {

    /**
     * 构造查询SQL
     *
     * @return {@link Stream<Arguments>}
     */
    private static Stream<Arguments> mysqlQuery() {
        return DataFactory.mysqlQuery().stream().map(Arguments::of);
    }

    /**
     * 构造聚合SQL
     *
     * @return {@link Stream<Arguments>}
     */
    private static Stream<Arguments> mysqlAggregation() {
        return DataFactory.mysqlAggregation().stream().map(Arguments::of);
    }

    /**
     * 测试 SQL转 DSL
     *
     * @param sql SQL
     */
    @ParameterizedTest
    @MethodSource(value = {"mysqlQuery", "mysqlAggregation"})
    public void testConvert(String sql) {
        DSLConvert dslConvert = new DSLConvert(new DSLSelectSyntax());
        ESMapperProvider provider = dslConvert.convert(sql, DbType.mysql);

        log.debug(StrUtil.LF + "sql : " + sql + StrUtil.LF + provider);

        Assertions.assertTrue(Objects.nonNull(provider));
        Assertions.assertTrue(Objects.nonNull(provider.getIndex()));
        Assertions.assertTrue(Objects.nonNull(provider.getDsl()));
    }

}