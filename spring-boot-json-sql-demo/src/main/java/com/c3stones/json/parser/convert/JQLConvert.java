package com.c3stones.json.parser.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.c3stones.json.enums.AggregationType;
import com.c3stones.json.enums.Operator;
import com.c3stones.json.enums.OrderType;
import com.c3stones.json.enums.Relation;
import com.c3stones.json.mapper.JSONMapperProvider;
import com.c3stones.json.mapper.model.*;
import com.c3stones.json.parser.select.JSONDefaultSelect;
import com.c3stones.json.parser.select.JSONSelectFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharPool.DOUBLE_QUOTES;
import static cn.hutool.core.text.StrPool.*;
import static com.alibaba.druid.sql.ast.expr.SQLBinaryOperator.*;

/**
 * SQL 转 JSONSQL
 *
 * @author CL
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("all")
public class JQLConvert {
    /**
     * SQL 常量 - *
     */
    public static final String SQL_ASTERISK = "*";

    private final JSONDefaultSelect jsonSelectSyntax;

    /**
     * 转换
     *
     * @param sql SQL
     * @return {@link JSONMapperProvider}
     */
    /**
     * 转换
     *
     * @param sql    SQL
     * @param dbType 数据库类型
     * @return {@link JSONMapperProvider}
     */
    public JSONMapperProvider convert(String sql, DbType dbType) {
        SQLStatementParser sqlStatementParser = new SQLStatementParser(sql, dbType);
        Opt<SQLSelectQueryBlock> optional = Opt.ofNullable(sqlStatementParser)
                .map(parser -> (SQLSelectStatement) sqlStatementParser.parseStatement())
                .map(SQLSelectStatement::getSelect)
                .map(sqlSelect -> (SQLSelectQueryBlock) sqlSelect.getQuery());
        return optional.isPresent() ? handle(optional.get()).setSql(sql) : new JSONMapperProvider(sql);
    }

    /**
     * 处理SQL
     *
     * @param sqlSelectQuery SQL Select查询
     * @return {@link JSONMapperProvider}
     */
    private JSONMapperProvider handle(SQLSelectQueryBlock sqlSelectQuery) {
        // 处理 Select
        List<SelectField> selectFieldList = handleSelect(sqlSelectQuery.getSelectList());
        // 处理 From
        String json = handleFrom(sqlSelectQuery.getFrom());
        // 处理 Where
        JQLWhereModel where = handleWhere(sqlSelectQuery.getWhere());
        // 处理 Aggregation
        List<JQLAggregationModel> aggregationList = handleAggregate(selectFieldList);
        // 处理 Group
        JQLGroupModel group = handleGroup(sqlSelectQuery.getGroupBy());
        // 处理 OrderBy
        List<JQLSortModel> sortList = handleOrderBy(sqlSelectQuery.getOrderBy());
        // 处理 Limit
        Page page = handleLimit(sqlSelectQuery.getLimit());
        // 生成JQL
        Integer from = Opt.ofNullable(page).map(Page::getFrom).get();
        Integer size = Opt.ofNullable(page).map(Page::getSize).get();
        JQLModel jql = jsonSelectSyntax.jql(where, aggregationList, group, sortList, from, size);
        String[] includes = selectFieldList.stream().map(field -> Opt.ofNullable(field.getAlias()).orElse(field.getName()))
                .filter(fieldName -> !StrUtil.equals(SQL_ASTERISK, fieldName) && Objects.nonNull(fieldName)).toArray(String[]::new);
        return new JSONMapperProvider().setJson(json).setJql(jql).setIncludes(includes);
    }

    /**
     * 处理查询字段
     *
     * @param sqlSelectItemList 查询元素
     * @return {@link List<SelectField>}
     */
    private List<SelectField> handleSelect(List<SQLSelectItem> sqlSelectItemList) {
        return Opt.ofNullable(sqlSelectItemList).orElse(Collections.emptyList())
                .stream().map(sqlSelectItem -> {
                    String name = null, alias, methodName = null;
                    alias = sqlSelectItem.getAlias();
                    // SQL 表达式
                    SQLExpr sqlExpr = sqlSelectItem.getExpr();
                    if (sqlExpr instanceof SQLAggregateExpr) { // 聚合查询
                        SQLAggregateExpr sqlAggregateExpr = (SQLAggregateExpr) sqlExpr;
                        SQLExpr firstSqlExpr = CollUtil.getFirst(sqlAggregateExpr.getArguments());
                        methodName = sqlAggregateExpr.getMethodName();
                        if (firstSqlExpr instanceof SQLAllColumnExpr) {
                            name = SQL_ASTERISK;
                        } else if (firstSqlExpr instanceof SQLIdentifierExpr) {
                            name = ((SQLIdentifierExpr) firstSqlExpr).getName();
                        }
                        alias = Opt.ofNullable(alias).orElse(methodName);
                    } else if (sqlExpr instanceof SQLAllColumnExpr) {  // 查询全部
                        name = SQL_ASTERISK;
                    } else if (sqlExpr instanceof SQLIdentifierExpr) { // 查询指定列
                        name = ((SQLIdentifierExpr) sqlExpr).getName();
                    } else if (sqlExpr instanceof SQLPropertyExpr) { // 查询对象列
                        SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) sqlExpr;
                        name = sqlPropertyExpr.getOwnerName() + StrUtil.DOT + sqlPropertyExpr.getName();
                    }
                    return new SelectField(name, alias, methodName);
                }).collect(Collectors.toList());
    }

    /**
     * 处理 From
     *
     * @param sqlTableSource SQL表资源
     * @return {@link String}
     */
    private String handleFrom(SQLTableSource sqlTableSource) {
        String index = null;
        if (sqlTableSource instanceof SQLExprTableSource) {
            SQLExpr tableSqlExpr = ((SQLExprTableSource) sqlTableSource).getExpr();
            if (tableSqlExpr instanceof SQLIdentifierExpr) {
                index = ((SQLIdentifierExpr) tableSqlExpr).getName();
            }
        }
        return index;
    }

    /**
     * 处理 Where条件
     *
     * @param sqlExpr SQL表达式
     * @return {@link JQLWhereModel}
     */
    private JQLWhereModel handleWhere(SQLExpr sqlExpr) {
        if (sqlExpr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlExpr;
            SQLBinaryOperator operator = sqlBinaryOpExpr.getOperator();
            if (BooleanAnd == operator || BooleanOr == operator) {
                return handleWhereAndOrOr(sqlBinaryOpExpr, operator);
            }
            return handleWhereBinaryOp(sqlBinaryOpExpr);
        } else if (sqlExpr instanceof SQLInListExpr) {
            return handleWhereInOrNotIn((SQLInListExpr) sqlExpr);
        } else if (sqlExpr instanceof SQLBetweenExpr) {
            return handleWhereBetween((SQLBetweenExpr) sqlExpr);
        } else if (sqlExpr instanceof SQLMethodInvokeExpr) {
            return handleMethodInvoke((SQLMethodInvokeExpr) sqlExpr);
        }
        return jsonSelectSyntax.all();
    }

    /**
     * 处理 AND 或 OR
     *
     * @param sqlBinaryOpExpr SQL两位元素操作
     * @return {@link JQLWhereModel}
     */
    private JQLWhereModel handleWhereAndOrOr(SQLBinaryOpExpr sqlBinaryOpExpr,
                                             SQLBinaryOperator sqlBinaryOperator) {
        SQLExpr leftExpr = sqlBinaryOpExpr.getLeft();
        SQLExpr rightExpr = sqlBinaryOpExpr.getRight();
        JQLWhereModel left = handleWhere(leftExpr);
        JQLWhereModel right = handleWhere(rightExpr);
        Integer leftSize = Opt.ofNullable(left).map(JQLWhereModel::getBinaryOpExprList).map(List::size).orElse(0);
        Integer rightSize = Opt.ofNullable(right).map(JQLWhereModel::getBinaryOpExprList).map(List::size).orElse(0);
        Relation relation = (sqlBinaryOperator == BooleanAnd) ? Relation.AND : Relation.OR;
        if (leftSize > 1 || rightSize > 1 || CollUtil.isNotEmpty(left.getChildWhereList())
                || CollUtil.isNotEmpty(right.getChildWhereList())) {
            return jsonSelectSyntax.child(relation, left, right);
        }
        List<JQLBinaryOpExpr> leftList = Opt.ofNullable(left.getBinaryOpExprList()).orElse(CollUtil.newArrayList());
        List<JQLBinaryOpExpr> rightList = Opt.ofNullable(right.getBinaryOpExprList()).orElse(CollUtil.newArrayList());
        return Objects.equals(Relation.AND, relation) ?
                jsonSelectSyntax.and(CollUtil.addAll(leftList, rightList).toArray(new JQLBinaryOpExpr[0]))
                : jsonSelectSyntax.or(CollUtil.addAll(leftList, rightList).toArray(new JQLBinaryOpExpr[0]));
    }

    /**
     * 处理二位元素操作
     *
     * @param sqlExpr SQL表达式
     * @return {@link JQLWhereModel}
     */
    private JQLWhereModel handleWhereBinaryOp(SQLBinaryOpExpr sqlExpr) {
        List<JQLBinaryOpExpr> list = CollUtil.newArrayList();
        SQLExpr leftExpr = sqlExpr.getLeft();
        SQLExpr rightExpr = sqlExpr.getRight();
        // 特殊处理 1 = 1 / 1 != 1
        if (leftExpr instanceof SQLIntegerExpr && rightExpr instanceof SQLIntegerExpr) {
            if (Objects.equals(getValue(leftExpr), getValue(rightExpr))) {
                if (sqlExpr.getOperator() == SQLBinaryOperator.Equality) {
                    list.add(jsonSelectSyntax.empty(IdUtil.fastUUID()));
                } else {
                    list.add(jsonSelectSyntax.notEmpty(IdUtil.fastUUID()));
                }
            }
        } else {
            String fieldName = null;
            if (leftExpr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) leftExpr;
                fieldName = sqlIdentifierExpr.getName();
            } else if (leftExpr instanceof SQLPropertyExpr) {
                SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) leftExpr;
                fieldName = sqlPropertyExpr.getOwnerName() + StrUtil.DOT + sqlPropertyExpr.getName();
            } else if (leftExpr instanceof SQLMethodInvokeExpr) {
                return handleMethodInvoke((SQLMethodInvokeExpr) leftExpr);
            }
            Object value = getValue(rightExpr);
            JSONDefaultSelect matchSelect = JSONSelectFactory.match(value);
            switch (sqlExpr.getOperator()) {
                case Equality:
                    list.add(matchSelect.eq(fieldName, value));
                    break;
                case NotEqual:
                case LessThanOrGreater:
                    list.add(matchSelect.neq(fieldName, value));
                    break;
                case GreaterThan:
                    list.add(matchSelect.gt(fieldName, value));
                    break;
                case GreaterThanOrEqual:
                    list.add(matchSelect.gte(fieldName, value));
                    break;
                case LessThan:
                    list.add(matchSelect.lt(fieldName, value));
                    break;
                case LessThanOrEqual:
                    list.add(matchSelect.lte(fieldName, value));
                    break;
                case Like:
                    list.add(matchSelect.contain(fieldName, value));
                    break;
                case NotLike:
                    list.add(matchSelect.notContain(fieldName, value));
                    break;
                case Is:
                    list.add(matchSelect.empty(fieldName));
                    break;
                case IsNot:
                    list.add(matchSelect.notEmpty(fieldName));
                    break;
                default:
                    // no operate
            }
        }
        return jsonSelectSyntax.and(list.toArray(new JQLBinaryOpExpr[0]));
    }

    /**
     * 处理 in 或 notIn
     *
     * @param sqlInListExpr SQL In 表达式
     * @return {@link JQLWhereModel}
     */
    private JQLWhereModel handleWhereInOrNotIn(SQLInListExpr sqlInListExpr) {
        SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) sqlInListExpr.getExpr();
        String fieldName = sqlIdentifierExpr.getName();
        List<Object> values = sqlInListExpr.getTargetList().stream().map(this::getValue).collect(Collectors.toList());
        JSONDefaultSelect matchSelect = JSONSelectFactory.match(values);
        return jsonSelectSyntax.and(sqlInListExpr.isNot() ? matchSelect.notIn(fieldName, values) : matchSelect.in(fieldName, values));
    }

    /**
     * 处理 between
     *
     * @param sqlBetweenExpr SQL Between 表达式
     * @return {@link JQLWhereModel}
     */
    private JQLWhereModel handleWhereBetween(SQLBetweenExpr sqlBetweenExpr) {
        SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) sqlBetweenExpr.getTestExpr();
        String field = sqlIdentifierExpr.getName();
        Object startValue = getValue(sqlBetweenExpr.getBeginExpr());
        Object endValue = getValue(sqlBetweenExpr.getEndExpr());
        JSONDefaultSelect matchSelect = JSONSelectFactory.match(Arrays.asList(startValue, endValue));
        return jsonSelectSyntax.and(matchSelect.range(field, startValue, endValue));
    }

    /**
     * 处理函数调用
     *
     * @param sqlMethodInvokeExpr SQL函数调用表达式
     * @return {@link JQLWhereModel}
     */
    private JQLWhereModel handleMethodInvoke(SQLMethodInvokeExpr sqlMethodInvokeExpr) {
        String methodName = sqlMethodInvokeExpr.getMethodName();
        List<SQLExpr> arguments = sqlMethodInvokeExpr.getArguments();
        SQLExpr leftExpr = CollUtil.get(arguments, 0);
        SQLExpr rightExpr = CollUtil.get(arguments, 1);

        String fieldName = null;
        if (leftExpr instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) leftExpr;
            fieldName = sqlIdentifierExpr.getName();
        } else if (leftExpr instanceof SQLPropertyExpr) {
            SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) leftExpr;
            fieldName = sqlPropertyExpr.getOwnerName() + StrUtil.DOT + sqlPropertyExpr.getName();
        }

        if (StrUtil.equalsAnyIgnoreCase(methodName, "json_contain", "json_not_contain", "json_contains")) {
            List<Object> values = null;
            if (rightExpr instanceof SQLArrayExpr) {
                values = ((SQLArrayExpr) rightExpr).getValues().stream().map(this::getValue).collect(Collectors.toList());
            } else if (rightExpr instanceof SQLCharExpr) {
                values = Opt.ofNullable(((SQLCharExpr) rightExpr)).map(SQLCharExpr::getValue).map(str -> StrUtil.strip(
                                StrUtil.toStringOrNull(str), StrUtil.toString(C_BRACKET_START), StrUtil.toString(C_BRACKET_END)))
                        .map(str -> StrUtil.strip(str, COMMA)).map(CollUtil::newArrayList).orElse(CollUtil.newArrayList())
                        .stream().map(str -> StrUtil.strip(StrUtil.toStringOrNull(str), StrUtil.toString(DOUBLE_QUOTES),
                                StrUtil.toString(DOUBLE_QUOTES))).map(StrUtil::trim).collect(Collectors.toList());
            }
            JSONDefaultSelect matchSelect = JSONSelectFactory.match(values);
            boolean isContain = Objects.equals(Operator.IN, Operator.findByValue(methodName));
            SQLObject parent = sqlMethodInvokeExpr.getParent();
            if (parent instanceof SQLBinaryOpExpr) {
                SQLBinaryOperator operator = ((SQLBinaryOpExpr) parent).getOperator();
                Boolean value = BooleanUtil.toBoolean(StrUtil.toStringOrNull(getValue(((SQLBinaryOpExpr) parent).getRight())));
                if (BooleanUtil.isTrue(value)) {
                    isContain = Objects.equals(Equality, operator) ? true : false;
                } else {
                    isContain = Objects.equals(Equality, operator) ? false : true;
                }
            }
            return jsonSelectSyntax.and(isContain ? matchSelect.in(fieldName, values) : matchSelect.notIn(fieldName, values));
        } else if (StrUtil.equalsAnyIgnoreCase(methodName, "jsonb_array_length", "json_length")) {
            SQLObject parent = sqlMethodInvokeExpr.getParent();
            if (parent instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) parent;
                JSONDefaultSelect matchSelect = JSONSelectFactory.any();
                return jsonSelectSyntax.and(Objects.equals(Equality, sqlBinaryOpExpr.getOperator()) ?
                        matchSelect.empty(fieldName) : matchSelect.notEmpty(fieldName));
            }
        }
        return jsonSelectSyntax.all();
    }

    /**
     * 处理聚合函数
     *
     * @param selectFieldList 查询字段
     * @return {@link List<JQLAggregationModel>}
     */
    private List<JQLAggregationModel> handleAggregate(List<SelectField> selectFieldList) {
        List<JQLAggregationModel> list = CollUtil.newArrayList();
        for (SelectField selectField : Opt.ofNullable(selectFieldList).orElse(ListUtil.empty())) {
            AggregationType aggregationType = AggregationType.findByValue(selectField.getMethodName());
            if (Objects.isNull(aggregationType)) continue;
            list.add(new JQLAggregationModel(aggregationType, selectField.getName(),
                    Opt.ofNullable(selectField.getAlias()).orElse(selectField.getMethodName())));
        }
        return list;
    }

    /**
     * 处理分组表
     *
     * @param sqlSelectGroupByClause SQL GroupBy 从句
     * @return {@link JQLGroupModel}
     */
    private JQLGroupModel handleGroup(SQLSelectGroupByClause sqlSelectGroupByClause) {
        if (Objects.nonNull(sqlSelectGroupByClause)) {
            List<String> groupNameList = CollUtil.newArrayList();
            for (SQLExpr sqlExpr : sqlSelectGroupByClause.getItems()) {
                if (sqlExpr instanceof SQLIdentifierExpr) {
                    groupNameList.add(((SQLIdentifierExpr) sqlExpr).getName());
                }
            }
            return new JQLGroupModel(groupNameList);
        }
        return null;
    }

    /**
     * 处理 OrderBy
     *
     * @param sqlOrderBy SQL OrderBy
     * @return {@link String}
     */
    private List<JQLSortModel> handleOrderBy(SQLOrderBy sqlOrderBy) {
        List<JQLSortModel> list = CollUtil.newArrayList();
        for (SQLSelectOrderByItem sqlSelectOrderByItem : Opt.ofNullable(sqlOrderBy).map(SQLOrderBy::getItems).orElse(ListUtil.empty())) {
            SQLIdentifierExpr orderBySqlIdentifierExpr = (SQLIdentifierExpr) sqlSelectOrderByItem.getExpr();
            SQLOrderingSpecification sqlOrderingSpecification = sqlSelectOrderByItem.getType();
            list.add(new JQLSortModel(orderBySqlIdentifierExpr.getName(), OrderType.findByValue(StrUtil.toStringOrNull(sqlOrderingSpecification))));
        }
        return list;
    }

    /**
     * 处理 Limit
     *
     * @param sqlLimit SQL Limit
     * @return {@link Page}
     */
    private Page handleLimit(SQLLimit sqlLimit) {
        if (Objects.isNull(sqlLimit)) return null;
        SQLIntegerExpr sqlLimitOffset = (SQLIntegerExpr) sqlLimit.getOffset();
        SQLIntegerExpr sqlLimitRowCount = (SQLIntegerExpr) sqlLimit.getRowCount();
        Integer from = Objects.isNull(sqlLimitOffset) ? 0 : sqlLimitOffset.getNumber().intValue();
        Integer size = sqlLimitRowCount.getNumber().intValue();
        return new Page().setFrom(from).setSize(size);
    }

    /**
     * 获取值
     *
     * @param sqlExpr 表达式
     * @return {@link Object}
     */
    private Object getValue(SQLExpr sqlExpr) {
        Object value = null;
        if (sqlExpr instanceof SQLIntegerExpr) {
            value = ((SQLIntegerExpr) sqlExpr).getNumber();
        } else if (sqlExpr instanceof SQLCharExpr) {
            value = ((SQLCharExpr) sqlExpr).getText();
        } else if (sqlExpr instanceof SQLNumberExpr) {
            value = ((SQLNumberExpr) sqlExpr).getNumber();
        } else if (sqlExpr instanceof SQLMethodInvokeExpr) {
            SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr) sqlExpr;
            String methodName = methodInvokeExpr.getMethodName();
            List<SQLExpr> arguments = methodInvokeExpr.getArguments();
            if (StrUtil.containsIgnoreCase("concat", methodName)) {
                value = arguments.stream().map(this::getValue).map(StrUtil::toStringOrNull)
                        .filter(Objects::nonNull).collect(Collectors.joining());
            } else if (StrUtil.equalsAnyIgnoreCase(methodName, "lower", "upper")) {
                return getValue(CollUtil.getFirst(arguments));
            } else if (StrUtil.equalsAnyIgnoreCase(methodName, "to_timestamp", "from_unixtime")) {
                Object tmp = getValue(CollUtil.getFirst(arguments));
                return CollUtil.getFirst(StrUtil.split(tmp.toString(), StrUtil.DOT));
            }
        } else if (sqlExpr instanceof SQLCastExpr) {
            SQLCastExpr sqlCastExpr = (SQLCastExpr) sqlExpr;
            return getValue(sqlCastExpr.getExpr());
        } else if (sqlExpr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlExpr;
            return getValue(sqlBinaryOpExpr.getLeft()) + StrUtil.DOT
                    + getValue(sqlBinaryOpExpr.getRight());
        }
        return value;
    }

    /**
     * 字符串转义
     *
     * @param str 字符串
     * @return {@link String}
     */
    private String escape(String str) {
        if (StringUtils.isBlank(str)) return str;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c) ||
                    c == '\\' ||
                    c == '\"' ||
                    c == '+' ||
                    c == '-' ||
                    c == '!' ||
                    c == '(' ||
                    c == ')' ||
                    c == '[' ||
                    c == ']' ||
                    c == '{' ||
                    c == '}' ||
                    c == ':' ||
                    c == '^' ||
                    c == '~' ||
                    c == '*' ||
                    c == '?' ||
                    c == '|' ||
                    c == '&' ||
                    c == ';' ||
                    c == '/' ||
                    c == '.' ||
                    c == '$') {
                sb.append('\\').append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 查询字段
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SelectField {

        /**
         * 字段名
         */
        private String name;

        /**
         * 别名
         */
        private String alias;

        /**
         * 方法名
         */
        private String methodName;

    }

    /**
     * 翻页
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    private static class Page {

        /**
         * 开始位置
         */
        private Integer from;

        /**
         * 页大小
         */
        private Integer size;

    }

}
