package com.c3stones.json.mapper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.DbType;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.c3stones.Application;
import com.c3stones.db.mapper.DBMapper;
import com.c3stones.user.entity.User;
import com.c3stones.json.mapper.model.DataModel;
import com.c3stones.json.parser.convert.JQLConvert;
import com.c3stones.json.parser.select.JSONSelectFactory;
import com.c3stones.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测试SQL转JSONSQL并查询
 *
 * @author CL
 */
@Slf4j
@SpringBootTest(classes = Application.class)
public class JQLMapperTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DBMapper dbMapper;

    private static List<DataModel> dataList;

    /**
     * 构造查询SQL
     *
     * @return {@link Stream < Arguments >}
     */
    private static Stream<Arguments> query() {
        return Stream.of(
                "select * from test_user",
                "select * from test_user where id = 1 ",
                "select * from test_user where id = 1 and name = '张三'",
                "select * from test_user where id = 2 or name = '张三'",
                "select * from test_user where name = '张三' and age > 28",
                "select * from test_user where id = 1 or name = '张三' and (age > 28 or sex = '男')",
                "select * from test_user where id = 1 and name = '张三' and (age > 28 or sex = '男') and (address like '%陕西%' and status != '0')",
                "select * from test_user where name in ('张三', '李四')",
                "select * from test_user where id = 1 and ( name in ('张三', '李四') and createTime between '2023-01-01' and '2023-04-30' )",
                "select * from test_user where ( name in ('张三', '李四') and age between 25 and 30 )",
                "select id, name from test_user where ( name in ('张三', '李四') and age between 26 and 30 )",

                "select id from test_user where 1 = 1",
                "select id from test_user where 1 != 1",
                "select id from test_user where name = 'XXX'",
                "select id from test_user where name is null",
                "select id from test_user where name is not null",

                "select hobbies from test_user where json_contains(hobbies, '[\"看书\"]')",
                "select * from test_user where json_contains(hobbies, '[\"看书\"]')",
                "select * from test_user where json_contains(hobbies, '[\"看书\"]') = 0",
                "select hobbies from test_user where json_length(hobbies) = 0 or hobbies is null",
                "select * from test_user where json_length(hobbies) > 0"
        ).map(Arguments::of);
    }

    /**
     * 构造聚合SQL
     *
     * @return {@link Stream<Arguments>}
     */
    private static Stream<Arguments> aggregation() {
        return Stream.of(
                "select count(*) from test_user",
                "select count(*) as count from test_user",
                "select count(id) from test_user where id = 1",
                "select count(id) as count from test_user where id > 1",
                "select count(id) from test_user group by name",
                "select sex, count(id) as count from test_user group by sex",
                "select sex, count(*) as count from test_user group by sex order by count",
                "select sex, count(*) as count from test_user group by sex order by count desc",
                "select sex, count(*) as count from test_user group by sex order by count desc limit 1",
                "select sex, count(*) as count from test_user group by sex order by count desc limit 1, 1",

                "select sum(age), max(id), min(age), avg(id), count(age) from test_user",
                "select sex, sum(age), max(id), min(age), avg(age), count(*) from test_user where name != 'xxx' group by sex"
        ).map(Arguments::of);
    }

    /**
     * 预置表及数据
     */
    @BeforeEach
    public void before() {
        List<User> userList = new ArrayList<User>() {{
            add(new User(1L, "张三", 25, "男", "陕西省西安市", true, LocalDate.of(2023, 1, 1),
                    Stream.of("看书", "听歌").map(Objects::toString).collect(Collectors.toList()), Stream.of(7, 21).collect(Collectors.toList())));
            add(new User(2L, "李四", 28, "女", "陕西省渭南市", true, LocalDate.of(2023, 4, 1),
                    Stream.of("逛街", "购物").map(Objects::toString).collect(Collectors.toList()), Collections.emptyList()));
            add(new User(3L, "王五", 30, "男", "北京市", false, LocalDate.of(2023, 5, 1),
                    Stream.of("看书").map(Objects::toString).collect(Collectors.toList()), Stream.of(8, 24).collect(Collectors.toList())));
        }};
        dataList = userList.stream().map(user -> new DataModel(JSONObject.parseObject(JSONUtil.toJsonStr(user)))).collect(Collectors.toList());

        userMapper.createTable();
        userMapper.delete(Wrappers.emptyWrapper());
        userList.forEach(user -> userMapper.insert(user));
    }

    /**
     * 清除表
     */
    @AfterEach
    public void after() {
        userMapper.dropTable();
    }

    /**
     * 测试查询
     *
     * @param sql SQL
     */
    @ParameterizedTest
    @MethodSource("query")
    public void testQuery(String sql) {
        List<Map<String, Object>> dbResult = dbMapper.aggregation(sql);

        JQLConvert jqlConvert = new JQLConvert(JSONSelectFactory.any());
        JSONMapperProvider provider = jqlConvert.convert(sql, DbType.mysql);
        provider.setDataList(dataList);
        JSONMapper jsonMapper = new JSONMapper();
        List<Map<String, Object>> jqlResult = jsonMapper.query(provider);

        Assertions.assertEquals(dbResult.size(), jqlResult.size());

        for (int i = 0; i < dbResult.size(); i++) {
            Map<String, Object> dbMap = dbResult.get(i);
            Map<String, Object> jqlMap = jqlResult.get(i);

            for (Map.Entry<String, Object> entry : dbMap.entrySet()) {
                Object expected = entry.getValue();
                Object actual = jqlMap.get(entry.getKey());
                if (expected instanceof LocalDateTime || actual instanceof Long) {
                    expected = Date.from(LocalDateTimeUtil.parse(expected.toString())
                            .atZone(ZoneId.systemDefault()).toInstant());
                    actual = DateUtil.date(NumberUtil.parseLong(actual.toString()));
                } else if (expected instanceof String && actual instanceof Collection) {
                    expected = ((String) expected).replaceAll(StrUtil.SPACE, StrUtil.EMPTY);
                    actual = JSONObject.toJSONString(actual);
                } else if (expected instanceof Integer[] && actual instanceof Collection) {
                    expected = JSONObject.toJSONString(expected);
                    actual = JSONObject.toJSONString(actual);
                } else if (expected instanceof Number && actual instanceof Number) {
                    expected = NumberUtil.parseDouble(expected.toString());
                    actual = NumberUtil.parseDouble(actual.toString());
                } else if (expected instanceof Number && actual instanceof Boolean) {
                    expected = BooleanUtil.toBoolean(expected.toString());
                    actual = BooleanUtil.toBoolean(actual.toString());
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
    @MethodSource(value = {"aggregation"})
    public void testAggregation(String sql) {
        List<Map<String, Object>> dbResult = dbMapper.aggregation(sql);

        JQLConvert jqlConvert = new JQLConvert(JSONSelectFactory.any());
        JSONMapperProvider provider = jqlConvert.convert(sql, DbType.mysql);
        provider.setDataList(dataList);
        JSONMapper jsonMapper = new JSONMapper();
        List<Map<String, Object>> jqlResult = jsonMapper.aggregation(provider);

        Assertions.assertEquals(dbResult.size(), jqlResult.size());

        for (Map<String, Object> dbMap : dbResult) {
            boolean match = jqlResult.stream().anyMatch(result -> dbMap.entrySet().stream().allMatch(
                    entry -> {
                        String key = entry.getKey();
                        if (StrUtil.contains(key, "(")) {
                            key = StrUtil.subBefore(key, "(", false);
                        }
                        String v1 = StrUtil.toStringOrNull(entry.getValue());
                        String v2 = StrUtil.toStringOrNull(result.get(key));
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
