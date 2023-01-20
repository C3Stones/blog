package com.c3stones;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import com.alibaba.druid.DbType;
import com.c3stones.common.DataFactory;
import com.c3stones.db.mapper.DBMapper;
import com.c3stones.es.convert.DSLConvert;
import com.c3stones.es.convert.DSLSelectSyntax;
import com.c3stones.es.mapper.ESMapper;
import com.c3stones.es.mapper.ESMapperProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * SQL 转 DSL 单元测试
 *
 * @author CL
 */
@SpringBootTest(classes = Application.class)
public class SQL2DSLTest {

    @Autowired
    private DBMapper dbMapper;

    @Autowired
    private ESMapper esMapper;

    /**
     * 构造查询SQL
     *
     * @return {@link Stream < Arguments >}
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
     * 测试查询
     *
     * @param sql SQL
     */
    @ParameterizedTest
    @MethodSource(value = {"mysqlQuery"})
    public void testQuery(String sql) throws IOException {
        List<Map<String, Object>> dbResult = dbMapper.query(sql);

        DSLConvert dslConvert = new DSLConvert(new DSLSelectSyntax());
        ESMapperProvider provider = dslConvert.convert(sql, DbType.mysql);

        List<Map<String, Object>> esResult = esMapper.query(provider);

        Assertions.assertEquals(dbResult.size(), esResult.size());

        for (int i = 0; i < dbResult.size(); i++) {
            Map<String, Object> dbMap = dbResult.get(i);
            Map<String, Object> esMap;
            if (!Opt.ofNullable(provider).map(ESMapperProvider::getDsl).map(ESMapperProvider.DslModel::getSort).map(JSONArray::isEmpty).orElse(true)) {
                esMap = esResult.get(i);
            } else {
                esMap = esResult.stream().filter(map -> Objects.equals(dbMap.get("id"), map.get("id"))).findFirst().orElse(MapUtil.empty());
            }
            for (Map.Entry<String, Object> entry : dbMap.entrySet()) {
                Object expected = entry.getValue();
                Object actual = esMap.get(entry.getKey());
                if (expected instanceof Date || actual instanceof Date) {
                    expected = DateUtil.parse(expected.toString()).second();
                    actual = DateUtil.parse(actual.toString()).offset(DateField.HOUR, 8).second();
                }
                Assertions.assertEquals(expected, actual);
            }
        }
    }

    /**
     * 测试聚合
     *
     * @param sql SQL
     */
    @ParameterizedTest
    @MethodSource(value = {"mysqlAggregation"})
    public void testAggregation(String sql) throws IOException {
        List<Map<String, Object>> dbResult = dbMapper.aggregation(sql);

        DSLConvert dslConvert = new DSLConvert(new DSLSelectSyntax());
        ESMapperProvider provider = dslConvert.convert(sql, DbType.mysql);

        List<Map<String, Object>> esResult = esMapper.aggregation(provider);

        Assertions.assertEquals(dbResult.size(), esResult.size());

        for (Map<String, Object> dbMap : dbResult) {
            boolean match = esResult.stream().anyMatch(result -> dbMap.entrySet().stream().allMatch(
                    dbEntry -> {
                        String v1 = StrUtil.toStringOrNull(dbEntry.getValue());
                        String v2 = StrUtil.toStringOrNull(result.get(dbEntry.getKey()));
                        if (NumberUtil.isNumber(v1) && NumberUtil.isNumber(v2)) {
                            v1 = String.format("%.5f", NumberUtil.parseDouble(v1));
                            v2 = String.format("%.5f", NumberUtil.parseDouble(v2));
                        }
                        return StrUtil.equals(v1, v2);
                    }));
            Assertions.assertTrue(match);
        }
    }

}
