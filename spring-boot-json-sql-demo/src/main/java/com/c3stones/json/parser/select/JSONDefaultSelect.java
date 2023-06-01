package com.c3stones.json.parser.select;

import cn.hutool.core.collection.CollUtil;
import com.c3stones.json.enums.Relation;
import com.c3stones.json.mapper.model.*;

import java.util.List;
import java.util.Objects;

/**
 * JSONSQL 默认查询语法接口
 *
 * @author CL
 */
@SuppressWarnings("all")
public interface JSONDefaultSelect {

    /**
     * 等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr eq(String fieldName, Object value);

    /**
     * 不等
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr neq(String fieldName, Object value);

    /**
     * 大于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr gt(String fieldName, Object value);

    /**
     * 大于等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr gte(String fieldName, Object value);

    /**
     * 小于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr lt(String fieldName, Object value);

    /**
     * 小于等于
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr lte(String fieldName, Object value);

    /**
     * 属于
     *
     * @param fieldName 属性名称
     * @param values    值（不能包含英文逗号）
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr in(String fieldName, List<Object> values);

    /**
     * 不属于
     *
     * @param fieldName 属性名称
     * @param values    值（不能包含英文逗号）
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr notIn(String fieldName, List<Object> values);

    /**
     * 包含
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr contain(String fieldName, Object value);

    /**
     * 不包含
     *
     * @param fieldName 属性名称
     * @param value     值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr notContain(String fieldName, Object value);

    /**
     * 为空
     *
     * @param fieldName 属性名称
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr empty(String fieldName);

    /**
     * 不为空
     *
     * @param fieldName 属性名称
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr notEmpty(String fieldName);

    /**
     * 在两者之间
     *
     * @param fieldName  属性名称
     * @param startValue 起始值
     * @param endValue   终止值
     * @return {@link JQLBinaryOpExpr}
     */
    JQLBinaryOpExpr range(String fieldName, Object startValue, Object endValue);

    /**
     * 关系与
     *
     * @param binaryOpExprs 二元运算表达式
     * @return {@link JQLWhereModel}
     */
    default JQLWhereModel and(JQLBinaryOpExpr... binaryOpExprs) {
        return new JQLWhereModel().setRelation(Relation.AND).setBinaryOpExprList(CollUtil.newArrayList(binaryOpExprs));
    }

    /**
     * 关系
     *
     * @param binaryOpExprs 二元运算表达式
     * @return {@link JQLWhereModel}
     */
    default JQLWhereModel or(JQLBinaryOpExpr... binaryOpExprs) {
        return new JQLWhereModel().setRelation(Relation.OR).setBinaryOpExprList(CollUtil.newArrayList(binaryOpExprs));
    }

    /**
     * 子查询模型
     *
     * @param relation    子模型之间的关系
     * @param childWheres 子查询模型
     * @return {@link JQLWhereModel}
     */
    default JQLWhereModel child(Relation relation, JQLWhereModel... childWheres) {
        return new JQLWhereModel().setRelation(relation).setChildWhereList(CollUtil.newArrayList(childWheres));
    }

    /**
     * 查询全部
     *
     * @return {@link JQLWhereModel}
     */
    default JQLWhereModel all() {
        return new JQLWhereModel();
    }

    /**
     * JQL
     *
     * @param where        条件
     * @param aggregations 聚合
     * @param group        分组
     * @param sorts        排序
     * @param from         偏移数
     * @param size         限制数
     * @return {@link JQLModel}
     */
    default JQLModel jql(JQLWhereModel where, List<JQLAggregationModel> aggregations, JQLGroupModel group, List<JQLSortModel> sorts, Integer from, Integer size) {
        JQLModel jql = new JQLModel();
        jql.setWhere(where);
        if (CollUtil.isNotEmpty(aggregations)) {
            jql.setAggregations(aggregations);
        }
        if (Objects.nonNull(group)) {
            jql.setGroup(group);
        }
        if (CollUtil.isNotEmpty(sorts)) {
            jql.setSorts(sorts);
        }
        if (Objects.nonNull(from)) {
            jql.setFrom(from);
        }
        if (Objects.nonNull(size)) {
            jql.setSize(size);
        }
        return jql;
    }

}
