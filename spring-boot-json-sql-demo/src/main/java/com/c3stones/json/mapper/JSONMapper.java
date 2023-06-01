package com.c3stones.json.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.TypeUtils;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.c3stones.json.enums.Operator;
import com.c3stones.json.enums.OrderType;
import com.c3stones.json.enums.Relation;
import com.c3stones.json.mapper.exception.ErrorCode;
import com.c3stones.json.mapper.exception.JQLException;
import com.c3stones.json.mapper.model.*;
import com.c3stones.json.mapper.valid.JQLAssert;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JSON Mapper
 *
 * @author CL
 */
@Slf4j
@Component
public class JSONMapper {

    @Resource
    private MybatisPlusProperties mybatisPlusProperties;

    /**
     * 聚合
     *
     * @param provider JSON Mapper Provider
     * @return {@link List}
     */
    public List<Map<String, Object>> aggregation(JSONMapperProvider provider) {
        return execute(provider);
    }

    /**
     * 查询
     *
     * @param provider JSON Mapper Provider
     * @return {@link List}
     */
    public List<Map<String, Object>> query(JSONMapperProvider provider) {
        return execute(provider);
    }

    /**
     * 执行
     *
     * @param provider JSON Mapper Provider
     * @return {@link List}
     */
    private List<Map<String, Object>> execute(JSONMapperProvider provider) {
        // 如果开启Mybatis-Plus SQL打印，则同时打印 JQL
        if (Opt.ofNullable(mybatisPlusProperties).map(MybatisPlusProperties::getConfiguration).map(Configuration::getLogImpl).isPresent()
                && log.isInfoEnabled()) {
            log.info(JSONObject.toJSONString(provider));
        }

        JQLAssert.validated(provider);
        if (CollUtil.isEmpty(provider.getDataList())) return ListUtil.empty();
        List<JSONObject> dataList = provider.getDataList().stream().map(DataModel::getData).collect(Collectors.toList());
        if (CollUtil.isEmpty(dataList)) return ListUtil.empty();
        String[] includes = provider.getIncludes();
        String[] excludes = provider.getExcludes();
        JQLModel jql = provider.getJql();
        JQLWhereModel where = jql.getWhere();
        JQLGroupModel group = jql.getGroup();
        List<JQLAggregationModel> aggregations = jql.getAggregations();
        List<JQLSortModel> sorts = jql.getSorts();
        Integer from = jql.getFrom();
        Integer size = jql.getSize();

        // 过滤
        List<JSONObject> filterList = dataList.stream().filter(data -> filter(data, where)).collect(Collectors.toList());
        // 过滤完的临时表存储
        List<Map<String, Object>> tempTable = createTempTable(filterList);
        // 分组聚合
        tempTable = groupAndAggregation(tempTable, group, aggregations);
        // 排序
        sort(tempTable, sorts);
        // 限制
        tempTable = limit(tempTable, from, size);

        // 包含列/排除列
        return result(tempTable, includes, excludes);
    }

    /**
     * 过滤
     *
     * @param data  数据
     * @param where 条件
     * @return {@link Boolean}
     */
    private boolean filter(JSONObject data, JQLWhereModel where) {
        List<Boolean> resultList = CollUtil.newArrayList();
        Opt.ofNullable(where.getBinaryOpExprList()).orElse(ListUtil.empty()).forEach(binaryOpExpr -> {
            String fieldName = binaryOpExpr.getFieldName();
            Object source = getValue(data, fieldName);
            Operator operator = binaryOpExpr.getOperator();
            if (Objects.equals(Operator.EMPTY, operator)) {
                resultList.add(source instanceof Iterable ? CollUtil.isEmpty((Iterable<?>) source) : Objects.isNull(source));
            } else if (Objects.equals(Operator.NOT_EMPTY, operator)) {
                resultList.add(source instanceof Iterable ? CollUtil.isNotEmpty((Iterable<?>) source) : Objects.nonNull(source));
            } else {
                resultList.add(filter(source, operator, binaryOpExpr.getValues()));
            }
        });
        Opt.ofNullable(where.getChildWhereList()).orElse(ListUtil.empty()).forEach(childWhere -> resultList.add(filter(data, childWhere)));
        return Objects.equals(Relation.OR, where.getRelation()) ? resultList.stream().anyMatch(result -> result)
                : resultList.stream().allMatch(result -> result);
    }

    /**
     * 过滤
     *
     * @param source   源数据
     * @param operator 操作符
     * @param values   目标数据
     * @return {@link Boolean}
     */
    private boolean filter(Object source, Operator operator, Object[] values) {
        boolean temp = false;
        try {
            if (Objects.nonNull(source) && Number.class.isAssignableFrom(source.getClass())) {
                if (values instanceof String[]) { // 可能是时间类型，尝试将时间转成Long
                    values = Stream.of(values).map(value -> DateUtil.parse(TypeUtils.cast(value, String.class)))
                            .map(DateTime::getTime).toArray(Number[]::new);
                }
                temp = Operator.judgeNumber(TypeUtils.cast(source, Number.class), operator, TypeUtils.cast(values, Number[].class));
            } else if (Objects.nonNull(source) && Date.class.isAssignableFrom(source.getClass())) {
                temp = Operator.judgeNumber(TypeUtils.toDate(source).getTime(), operator, Stream.of(values).map(value ->
                        DateUtil.parse(TypeUtils.cast(value, String.class))).map(DateTime::getTime).toArray(Number[]::new));
            } else if (Objects.nonNull(source) && source instanceof Collection) {
                temp = Operator.judgeArray(TypeUtils.cast(source, Object[].class), operator, values);
            } else {
                temp = Operator.judgeStr(StrUtil.toStringOrNull(source), operator, Stream.of(values)
                        .map(StrUtil::toStringOrNull).filter(Objects::nonNull).collect(Collectors.joining(StrUtil.COMMA)));
            }
        } catch (Exception e) {
            log.warn(StrUtil.EMPTY, new JQLException(ErrorCode.FILTER_EXECUTE_ERROR, e.getMessage(), e));
        }
        return temp;
    }

    /**
     * 创建临时表
     *
     * @param dataList 过滤后的数据
     * @return {@link List}
     */
    private List<Map<String, Object>> createTempTable(List<JSONObject> dataList) {
        List<Map<String, Object>> tempTable = new ArrayList<>(dataList.size());
        dataList.forEach(data -> {
            Set<Map.Entry<String, Object>> entries = data.entrySet();
            Map<String, Object> map = new LinkedHashMap<>(entries.size());
            for (Map.Entry<String, Object> entry : entries) {
                map.put(entry.getKey(), entry.getValue());
            }
            tempTable.add(map);
        });
        return tempTable;
    }

    /**
     * 分组并聚合
     *
     * @param tempTable    临时表
     * @param group        分组
     * @param aggregations 聚合
     * @return {@link List}
     */
    private List<Map<String, Object>> groupAndAggregation(List<Map<String, Object>> tempTable,
                                                          JQLGroupModel group,
                                                          List<JQLAggregationModel> aggregations) {
        List<String> groupNameList = Opt.ofNullable(group).map(JQLGroupModel::getGroupNameList).get();
        if (CollUtil.isEmpty(groupNameList)) return aggregation(tempTable, aggregations);

        List<Map<String, Object>> resultList = new ArrayList<>(tempTable.size());

        // 分组
        Map<Map<String, Object>, List<Map<String, Object>>> collect = tempTable.stream()
                .collect(Collectors.groupingBy(data -> groupNameList.stream().map(data::get)
                        .map(StrUtil::toString).collect(Collectors.joining()))).values()
                .stream().map(maps -> {
                    Map<String, Object> key = new LinkedHashMap<>(groupNameList.size());
                    Map<String, Object> first = CollUtil.getFirst(maps);
                    groupNameList.forEach(groupName -> key.put(groupName, first.get(groupName)));
                    return MapUtil.entry(key, maps);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 聚合
        collect.forEach((key, value) -> {
            Map<String, Object> map = MapBuilder.<String, Object>create(true).putAll(key).build();
            aggregation(value, aggregations).forEach(map::putAll);
            resultList.add(map);
        });

        return resultList;
    }

    /**
     * 聚合
     *
     * @param tempTable    临时表
     * @param aggregations 聚合
     * @return {@link List}
     */
    private List<Map<String, Object>> aggregation(List<Map<String, Object>> tempTable, List<JQLAggregationModel> aggregations) {
        if (CollUtil.isEmpty(aggregations)) return tempTable;

        Map<String, Object> map = MapUtil.newHashMap(aggregations.size(), true);
        int newScale = 4;
        aggregations.stream().collect(Collectors.groupingBy(JQLAggregationModel::getFieldName)).forEach((fileName, aggregationList) -> {
            DoubleSummaryStatistics statistics = tempTable.stream().map(data -> StrUtil.toStringOrNull(data.get(fileName))).mapToDouble(data ->
                    Opt.ofTry(() -> NumberUtil.parseDouble(data)).orElse(BigInteger.ZERO.doubleValue())).summaryStatistics();
            aggregationList.forEach(aggregation -> {
                Object value = null;
                switch (aggregation.getAggregationType()) {
                    case SUM:
                        value = BigDecimal.valueOf(statistics.getSum()).longValue();
                        break;
                    case MAX:
                        value = BigDecimal.valueOf(statistics.getMax()).setScale(newScale, RoundingMode.HALF_UP).doubleValue();
                        break;
                    case MIN:
                        value = BigDecimal.valueOf(statistics.getMin()).setScale(newScale, RoundingMode.HALF_UP).doubleValue();
                        break;
                    case AVG:
                        value = BigDecimal.valueOf(statistics.getAverage()).setScale(newScale, RoundingMode.HALF_UP).doubleValue();
                        break;
                    case COUNT:
                        value = BigDecimal.valueOf(statistics.getCount()).longValue();
                        break;
                }
                map.put(aggregation.getAlias(), value);
            });
        });
        return CollUtil.newArrayList(map);
    }

    /**
     * 排序
     *
     * @param tempTable 临时表
     * @param sorts     排序
     */
    private void sort(List<Map<String, Object>> tempTable, List<JQLSortModel> sorts) {
        Opt.ofNullable(sorts).orElse(ListUtil.empty()).forEach(sort -> {
            String fieldName = sort.getFieldName();
            OrderType orderType = sort.getOrderType();
            tempTable.sort((d1, d2) -> {
                Integer a = Integer.valueOf(StrUtil.toString((d1.get(fieldName))));
                Integer b = Integer.valueOf(StrUtil.toString((d2.get(fieldName))));
                return Objects.equals(OrderType.ASC, orderType) ? a - b : b - a;
            });
        });
    }

    /**
     * 限制
     *
     * @param tempTable 临时表
     * @param from      偏移数
     * @param size      限制数
     * @return {@link List}
     */
    private List<Map<String, Object>> limit(List<Map<String, Object>> tempTable, Integer from, Integer size) {
        return tempTable.stream().skip(Opt.ofNullable(from).orElse(0)).limit(Opt.ofNullable(size).orElse(tempTable.size())).collect(Collectors.toList());
    }

    /**
     * 结果
     *
     * @param tempTable 临时表
     * @param includes  包含列
     * @param excludes  排除列
     * @return {@link List}
     */
    private List<Map<String, Object>> result(List<Map<String, Object>> tempTable, String[] includes, String[] excludes) {
        return tempTable.stream().map(data -> includes(data, includes)).map(data -> excludes(data, includes, excludes))
                .collect(Collectors.toList());
    }

    /**
     * 包含
     *
     * @param data     数据
     * @param includes 包含列
     * @return {@link Map}
     */
    private Map<String, Object> includes(Map<String, Object> data, String[] includes) {
        if (ArrayUtil.isEmpty(includes) || MapUtil.isEmpty(data)) return data;
        Map<String, Object> map = new HashMap<>(data.size());
        for (String include : includes) {
            String json = JSONObject.toJSONString(data);
            JSONObject jsonObject = JSONObject.parseObject(json);
            map.put(include, getValue(jsonObject, include));
        }
        return MapUtil.isEmpty(map) ? data : map;
    }

    /**
     * 排除
     *
     * @param data     数据
     * @param includes 包含列
     * @param excludes 排除列
     * @return {@link Map}
     */
    private Map<String, Object> excludes(Map<String, Object> data, String[] includes, String[] excludes) {
        if (ArrayUtil.isEmpty(excludes) || MapUtil.isEmpty(data)) return data;
        for (String exclude : excludes) {
            if (!StrUtil.containsAny(exclude, includes)) {
                excludes(data, exclude);
            } else {
                data.remove(exclude);
            }
        }
        return data;
    }

    /**
     * 排除
     *
     * @param data    数据
     * @param exclude 排除列
     */
    private void excludes(Map<String, Object> data, String exclude) {
        List<String> split = StrUtil.split(exclude, StrUtil.DOT);
        int size = CollUtil.size(split);
        if (size <= 1) {
            data.remove(exclude);
            return;
        }
        String name = CollUtil.getFirst(split);
        String subName = StrUtil.join(StrUtil.DOT, CollUtil.sub(split, 1, size));
        String json = JSONObject.toJSONString(data.get(name));
        if (!JSON.isValidObject(json)) return;
        JSONObject jsonObject = JSONObject.parseObject(json);
        excludes(jsonObject, subName);
        data.put(name, jsonObject);
    }

    /**
     * 获取数据
     *
     * @param data      数据
     * @param fieldName 属性名称
     * @return {@link Object}
     */
    private Object getValue(JSONObject data, String fieldName) {
        List<String> split = StrUtil.split(fieldName, StrUtil.DOT);
        int size = CollUtil.size(split);
        if (size <= 1) {
            return data.get(fieldName);
        }
        String name = CollUtil.getFirst(split);
        List<String> subList = CollUtil.sub(split, 1, size);
        String subName = StrUtil.join(StrUtil.DOT, subList);
        String json = JSONObject.toJSONString(data.get(name));
        if (!JSON.isValidObject(json)) return null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        return getValue(jsonObject, subName);
    }

}
