package com.c3stones.json.parser.select.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.c3stones.json.enums.Operator;
import com.c3stones.json.mapper.model.JQLBinaryOpExpr;
import com.c3stones.json.parser.select.JSONDefaultSelect;

import java.util.List;
import java.util.Objects;

/**
 * JSONSQL 字符串查询语法
 *
 * @author CL
 */
@SuppressWarnings("unchecked")
public class StringSelect implements JSONDefaultSelect {

    /**
     * 格式化值
     *
     * @param value 值
     * @return {@link String}
     */
    private String format(Object value) {
        return StrUtil.toStringOrNull(value);
    }

    /**
     * 格式化值
     *
     * @param value 值
     * @return {@link String[]}
     */
    private String[] format(List<Object> value) {
        return Opt.ofNullable(value).orElse(ListUtil.empty()).stream().map(StrUtil::toStringOrNull)
                .filter(Objects::nonNull).toArray(String[]::new);
    }

    /**
     * 格式化
     *
     * @param value 值
     * @return {@link String}
     */
    private String format(String value) {
        return StrUtil.removeSuffix(StrUtil.removePrefix(value, "%"), "%");
    }

    /**
     * 等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> eq(String fieldName, Object value) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.EQ).setValues(format(value));
    }

    /**
     * 不等
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> neq(String fieldName, Object value) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.NEQ).setValues(format(value));
    }

    /**
     * 大于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> gt(String fieldName, Object value) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.GT).setValues(format(value));
    }

    /**
     * 大于等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> gte(String fieldName, Object value) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.GTE).setValues(format(value));
    }

    /**
     * 小于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> lt(String fieldName, Object value) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.LT).setValues(format(value));
    }

    /**
     * 小于等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> lte(String fieldName, Object value) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.LTE).setValues(format(value));
    }

    /**
     * 属于
     *
     * @param fieldName 属性名称
     * @param values    值（不能包含英文逗号）
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> in(String fieldName, List<Object> values) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.IN).setValues(format(values));
    }

    /**
     * 不属于
     *
     * @param fieldName 属性名称
     * @param values    值（不能包含英文逗号）
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> notIn(String fieldName, List<Object> values) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.NOT_IN).setValues(format(values));
    }

    /**
     * 包含
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> contain(String fieldName, Object value) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.CONTAIN).setValues(format(format(value)));
    }

    /**
     * 不包含
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> notContain(String fieldName, Object value) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.NOT_CONSTAIN).setValues(format(format(value)));
    }

    /**
     * 为空
     *
     * @param fieldName 属性名称
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> empty(String fieldName) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.EMPTY);
    }

    /**
     * 不为空
     *
     * @param fieldName 属性名称
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> notEmpty(String fieldName) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.NOT_EMPTY);
    }

    /**
     * 在两者之间
     *
     * @param fieldName  属性名称
     * @param startValue 起始值
     * @param endValue   终止值
     * @return {@link JQLBinaryOpExpr}
     */
    @Override
    public JQLBinaryOpExpr<String> range(String fieldName, Object startValue, Object endValue) {
        return new JQLBinaryOpExpr<String>().setFieldName(fieldName).setOperator(Operator.RANGE).setValues(format(startValue), format(endValue));
    }

}
