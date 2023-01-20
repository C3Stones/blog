package com.c3stones.es.convert;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static com.c3stones.es.constants.Constant.*;

/**
 * Elasticsearch DSL 统用查询语法
 *
 * @author CL
 */
public class DSLSelectSyntax {

    /**
     * 格式化
     *
     * @param values 值
     * @return {@link String}
     */
    public String format(List<String> values) {
        StringJoiner format = new StringJoiner("\",\"", "\"", "\"");
        values.forEach(format::add);
        return format.toString();
    }

    /**
     * 等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link String}
     */
    public String eq(String fieldName, String value) {
        return String.format("{\"match_phrase\" : {\"%s\" : \"%s\"}}", fieldName, value);
    }

    /**
     * 不等
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link String}
     */
    public String neq(String fieldName, String value) {
        return String.format("{\"bool\" : {\"must_not\" : [{\"match_phrase\" : {\"%s\" : {\"query\" : \"%s\"}}}]}}", fieldName, value);
    }

    /**
     * 大于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link String}
     */
    public String gt(String fieldName, String value) {
        return String.format("{\"range\" : {\"%s\" : {\"gt\" : \"%s\"}}}", fieldName, value);
    }

    /**
     * 大于等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link String}
     */
    public String gte(String fieldName, String value) {
        return String.format("{\"range\" : {\"%s\" : {\"from\" : \"%s\"}}}", fieldName, value);
    }

    /**
     * 小于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link String}
     */
    public String lt(String fieldName, String value) {
        return String.format("{\"range\" : {\"%s\" : {\"lt\" : \"%s\"}}}", fieldName, value);
    }

    /**
     * 小于等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link String}
     */
    public String lte(String fieldName, String value) {
        return String.format("{\"range\" : {\"%s\" : {\"to\" : \"%s\"}}}", fieldName, value);
    }

    /**
     * 属于
     *
     * @param fieldName 属性名称
     * @param values    值
     * @return {@link String}
     */
    public String in(String fieldName, List<String> values) {
        return String.format("{\"terms\" : {\"%s\" : [%s]}}", fieldName, format(values));
    }

    /**
     * 不属于
     *
     * @param fieldName 属性名称
     * @param values    值
     * @return {@link String}
     */
    public String notIn(String fieldName, List<String> values) {
        return String.format("{\"bool\" : {\"must_not\" : {\"terms\" : {\"%s\" : [%s]}}}}", fieldName, format(values));
    }

    /**
     * 包含
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link String}
     */
    public String contain(String fieldName, String value) {
        return String.format("{\"query_string\":{\"default_field\": \"%s\",\"query\":\"%s\"}}", fieldName, value.replaceAll("%", "*"));
    }

    /**
     * 不包含
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link String}
     */
    public String notContain(String fieldName, String value) {
        return String.format("{\"bool\":{\"must_not\":{\"query_string\":{\"default_field\":\"%s\",\"query\":\"%s\"}}}}", fieldName, value.replaceAll("%", "*")
        );
    }

    /**
     * 为空
     *
     * @param fieldName 属性名称
     * @return {@link String}
     */
    public String empty(String fieldName) {
        return String.format("{\"bool\": { \"must_not\": { \"exists\": { \"field\": \"%s\" }}}}", fieldName);
    }

    /**
     * 不为空
     *
     * @param fieldName 属性名称
     * @return {@link String}
     */
    public String notEmpty(String fieldName) {
        return String.format("{\"bool\": { \"must\": { \"exists\": { \"field\": \"%s\" }}}}", fieldName);
    }

    /**
     * 在两者之间
     *
     * @param fieldName  属性名称
     * @param startValue 起始值
     * @param endValue   终止值
     * @return {@link String}
     */
    public String range(String fieldName, String startValue, String endValue) {
        return String.format("{\"range\" : {\"%s\" : {\"from\" : \"%s\", \"to\" : \"%s\"}}}", fieldName, startValue, endValue);
    }

    /**
     * 必须包含
     *
     * @param dsl DSL
     * @return {@link String}
     */
    public String must(String dsl) {
        return String.format("{\"bool\" : {\"must\" : [%s]}}", dsl);
    }

    /**
     * 可能包含
     *
     * @param dsl DSL
     * @return {@link String}
     */
    public String should(String dsl) {
        return String.format("{\"bool\" : {\"should\" : [%s]}}", dsl);
    }

    /**
     * 查询全部
     *
     * @return {@link String}
     */
    public String all() {
        return "{\"match_all\": {}}";
    }

    /**
     * DSL
     *
     * @param where   Where条件
     * @param groupBy 分组字段
     * @param orderBy 排序字段
     * @param from    偏移数
     * @param size    限制数
     * @return {@link String}
     */
    public String dsl(String where, String groupBy, String orderBy, Integer from, Integer size) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{\"" + DSL_QUERY + "\" : %s ", where));
        if (StrUtil.isNotBlank(groupBy)) {
            sb.append(String.format(" ,\"" + DSL_AGGREGATIONS + "\" : %s", groupBy));
        }
        if (StrUtil.isNotBlank(orderBy)) {
            sb.append(String.format(" ,\"" + DSL_SORT + "\" : %s", orderBy));
        }
        if (Objects.nonNull(from)) {
            sb.append(String.format(" ,\"" + DSL_FROM + "\" : %s ", from));
        }
        if (Objects.nonNull(size)) {
            sb.append(String.format(" ,\"" + DSL_SIZE + "\" : %s ", size));
        }
        sb.append("}");
        return sb.toString();
    }

}
