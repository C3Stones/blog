package com.c3stones.es.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.c3stones.es.mapper.ESMapperProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.druid.sql.ast.expr.SQLBinaryOperator.BooleanAnd;
import static com.alibaba.druid.sql.ast.expr.SQLBinaryOperator.BooleanOr;
import static com.c3stones.es.constants.Constant.*;
import static java.util.Collections.singletonMap;

/**
 * SQL 转 DSL
 *
 * @author CL
 */
@Slf4j
@RequiredArgsConstructor
public class DSLConvert {

    private final DSLSelectSyntax dslSelectSyntax;

    /**
     * 转换
     *
     * @param sql    SQL
     * @param dbType 数据库类型
     * @return {@link ESMapperProvider}
     */
    public ESMapperProvider convert(String sql, DbType dbType) {
        SQLStatementParser sqlStatementParser = new SQLStatementParser(sql, dbType);
        Opt<SQLSelectQueryBlock> optional = Opt.ofNullable(sqlStatementParser)
                .map(parser -> (SQLSelectStatement) sqlStatementParser.parseStatement())
                .map(SQLSelectStatement::getSelect)
                .map(sqlSelect -> (SQLSelectQueryBlock) sqlSelect.getQuery());
        return optional.isPresent() ? handle(optional.get()) : null;
    }

    /**
     * 处理SQL
     *
     * @param sqlSelectQuery SQL Select查询
     * @return {@link ESMapperProvider}
     */
    private ESMapperProvider handle(SQLSelectQueryBlock sqlSelectQuery) {
        // 处理 Select
        List<SelectField> selectFieldList = handleSelect(sqlSelectQuery.getSelectList());
        // 处理 From
        String index = handleFrom(sqlSelectQuery.getFrom());
        // 处理 Where
        String where = handleWhere(sqlSelectQuery.getWhere(), true);
        // 处理 GroupBy
        String groupBy = handleGroupBy(selectFieldList, sqlSelectQuery.getGroupBy());
        // 处理 OrderBy
        String orderBy = handleOrderBy(sqlSelectQuery.getOrderBy());
        // 处理 Limit
        Page page = handleLimit(sqlSelectQuery.getLimit());
        // 生成DSL
        Integer from = Opt.ofNullable(page).map(Page::getFrom).get();
        Integer size = Opt.ofNullable(page).map(Page::getSize).get();
        String dsl = dslSelectSyntax.dsl(where, groupBy, orderBy, from, size);
        String[] includes = selectFieldList.stream()
                .map(field -> Opt.ofNullable(field.getAlias()).orElse(field.getName()))
                .filter(field -> !StrUtil.equals("*", field)).toArray(String[]::new);
        return new ESMapperProvider(index, dsl, includes);
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
                            name = "*";
                        } else if (firstSqlExpr instanceof SQLIdentifierExpr) {
                            name = ((SQLIdentifierExpr) firstSqlExpr).getName();
                        }
                    } else if (sqlExpr instanceof SQLAllColumnExpr) {  // 查询全部
                        name = "*";
                    } else if (sqlExpr instanceof SQLMethodInvokeExpr) { // 函数调用
                        SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr) sqlExpr;
                        SQLExpr firstSqlExpr = CollUtil.getFirst(methodInvokeExpr.getArguments());
                        methodName = methodInvokeExpr.getMethodName();
                        if (firstSqlExpr instanceof SQLIdentifierExpr) {
                            name = ((SQLIdentifierExpr) firstSqlExpr).getName();
                        } else if (firstSqlExpr instanceof SQLBinaryOpExpr) {
                            name = handleWhere(firstSqlExpr, true);
                        } else {
                            name = firstSqlExpr.toString();
                        }
                    } else if (sqlExpr instanceof SQLIdentifierExpr) { // 查询指定列
                        name = ((SQLIdentifierExpr) sqlExpr).getName();
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
     * @param sqlExpr    SQL表达式
     * @param isComplete 是否完整条件
     * @return {@link String}
     */
    private String handleWhere(SQLExpr sqlExpr, boolean isComplete) {
        if (sqlExpr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlExpr;
            SQLBinaryOperator operator = sqlBinaryOpExpr.getOperator();
            if (BooleanAnd == operator || BooleanOr == operator)
                return handleWhereAndOrOr(sqlBinaryOpExpr, operator);
            return handleWhereBinaryOp(sqlBinaryOpExpr, isComplete);
        } else if (sqlExpr instanceof SQLInListExpr) {
            return handleWhereInOrNotIn((SQLInListExpr) sqlExpr, isComplete);
        } else if (sqlExpr instanceof SQLBetweenExpr) {
            return handleWhereBetween((SQLBetweenExpr) sqlExpr, isComplete);
        }
        return dslSelectSyntax.all();
    }

    /**
     * 处理 AND 或 OR
     *
     * @param sqlBinaryOpExpr SQL两位元素操作
     * @return {@link String}
     */
    private String handleWhereAndOrOr(SQLBinaryOpExpr sqlBinaryOpExpr, SQLBinaryOperator sqlBinaryOperator) {
        SQLExpr leftExpr = sqlBinaryOpExpr.getLeft();
        SQLExpr rightExpr = sqlBinaryOpExpr.getRight();
        String left = handleWhere(leftExpr, false);
        String right = handleWhere(rightExpr, false);
        StringJoiner dsl = new StringJoiner(StrUtil.equalsAny(StrUtil.EMPTY, left, right) ? StrUtil.EMPTY : StrUtil.COMMA);
        dsl.add(left).add(right);
        SQLObject parent = sqlBinaryOpExpr.getParent();
        if (parent instanceof SQLBinaryOpExpr) {
            if (((SQLBinaryOpExpr) parent).getOperator() == sqlBinaryOperator) return dsl.toString();
        }
        return sqlBinaryOperator == BooleanAnd ? dslSelectSyntax.must(dsl.toString()) : dslSelectSyntax.should(dsl.toString());
    }

    /**
     * 处理二位元素操作
     *
     * @param sqlExpr    SQL表达式
     * @param isComplete 是否完整条件
     * @return {@link String}
     */
    private String handleWhereBinaryOp(SQLBinaryOpExpr sqlExpr, boolean isComplete) {
        StringBuilder dsl = new StringBuilder();
        SQLExpr leftExpr = sqlExpr.getLeft();
        SQLExpr rightExpr = sqlExpr.getRight();
        // 特殊处理 1 = 1 / 1 != 1
        if (leftExpr instanceof SQLIntegerExpr && rightExpr instanceof SQLIntegerExpr) {
            if (Objects.equals(getValue(leftExpr), getValue(rightExpr))) {
                if (sqlExpr.getOperator() == SQLBinaryOperator.Equality) {
                    dsl.append(dslSelectSyntax.empty(IdUtil.fastUUID()));
                } else {
                    dsl.append(dslSelectSyntax.notEmpty(IdUtil.fastUUID()));
                }
            }
        } else {
            SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) sqlExpr.getLeft();
            String fieldName = sqlIdentifierExpr.getName();
            String value = getValue(rightExpr);
            switch (sqlExpr.getOperator()) {
                case Equality:
                    dsl.append(dslSelectSyntax.eq(fieldName, value));
                    break;
                case NotEqual:
                    dsl.append(dslSelectSyntax.neq(fieldName, value));
                    break;
                case GreaterThan:
                    dsl.append(dslSelectSyntax.gt(fieldName, value));
                    break;
                case GreaterThanOrEqual:
                    dsl.append(dslSelectSyntax.gte(fieldName, value));
                    break;
                case LessThan:
                    dsl.append(dslSelectSyntax.lt(fieldName, value));
                    break;
                case LessThanOrEqual:
                    dsl.append(dslSelectSyntax.lte(fieldName, value));
                    break;
                case Like:
                    dsl.append(dslSelectSyntax.contain(fieldName, escape(value)));
                    break;
                case NotLike:
                    dsl.append(dslSelectSyntax.notContain(fieldName, value));
                    break;
                case Is:
                    dsl.append(dslSelectSyntax.empty(fieldName));
                    break;
                case IsNot:
                    dsl.append(dslSelectSyntax.notEmpty(fieldName));
                    break;
                default:
                    // no operate
            }
        }
        return isComplete ? dslSelectSyntax.must(dsl.toString()) : dsl.toString();
    }

    /**
     * 处理 in 或 notIn
     *
     * @param sqlInListExpr SQL In 表达式
     * @param isComplete    是否完整条件
     * @return {@link String}
     */
    private String handleWhereInOrNotIn(SQLInListExpr sqlInListExpr, boolean isComplete) {
        SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) sqlInListExpr.getExpr();
        String fieldName = sqlIdentifierExpr.getName();
        List<String> values = sqlInListExpr.getTargetList().stream().map(this::getValue).collect(Collectors.toList());
        String dsl = sqlInListExpr.isNot() ? dslSelectSyntax.notIn(fieldName, values) : dslSelectSyntax.in(fieldName, values);
        return isComplete ? dslSelectSyntax.must(dsl) : dsl;
    }

    /**
     * 处理 between
     *
     * @param sqlBetweenExpr SQL Between 表达式
     * @param isComplete     是否完整条件
     * @return {@link String}
     */
    private String handleWhereBetween(SQLBetweenExpr sqlBetweenExpr, boolean isComplete) {
        SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) sqlBetweenExpr.getTestExpr();
        String field = sqlIdentifierExpr.getName();
        String startValue = getValue(sqlBetweenExpr.getBeginExpr());
        String endValue = getValue(sqlBetweenExpr.getEndExpr());
        String dsl = dslSelectSyntax.range(field, startValue, endValue);
        return isComplete ? dslSelectSyntax.must(dsl) : dsl;
    }

    /**
     * 处理 GroupBy
     *
     * @param selectFieldList        查询字段
     * @param sqlSelectGroupByClause SQL GroupBy 从句
     * @return {@link String}
     */
    private String handleGroupBy(List<SelectField> selectFieldList, SQLSelectGroupByClause sqlSelectGroupByClause) {
        if (selectFieldList.stream().allMatch(field -> Objects.isNull(field.getMethodName()))) return null;
        Queue<String> groupByList = CollUtil.newLinkedList();
        if (Objects.nonNull(sqlSelectGroupByClause)) {
            for (SQLExpr sqlExpr : sqlSelectGroupByClause.getItems()) {
                if (sqlExpr instanceof SQLIdentifierExpr) {
                    groupByList.add(((SQLIdentifierExpr) sqlExpr).getName());
                }
            }
        }
        return JSONUtil.toJsonStr(handleAggregate(selectFieldList, groupByList));
    }

    /**
     * 处理 OrderBy
     *
     * @param sqlOrderBy SQL OrderBy
     * @return {@link String}
     */
    private String handleOrderBy(SQLOrderBy sqlOrderBy) {
        if (Objects.isNull(sqlOrderBy)) return null;
        List<Map<String, String>> orderByList = CollUtil.newArrayList();
        for (SQLSelectOrderByItem sqlSelectOrderByItem : sqlOrderBy.getItems()) {
            SQLIdentifierExpr orderBySqlIdentifierExpr = (SQLIdentifierExpr) sqlSelectOrderByItem.getExpr();
            SQLOrderingSpecification sqlOrderingSpecification = sqlSelectOrderByItem.getType();
            orderByList.add(singletonMap(orderBySqlIdentifierExpr.getName(), sqlOrderingSpecification.name()));
        }
        return CollUtil.isNotEmpty(orderByList) ? JSONUtil.toJsonStr(orderByList) : null;
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
     * 处理聚合函数
     * {分组字段 : 配置}
     *
     * @param selectFields 查询字段
     * @param groupByList  分组字段
     * @return {@link Map}
     */
    private Map<String, Object> handleAggregate(List<SelectField> selectFields, Queue<String> groupByList) {
        if (groupByList.isEmpty()) return handleAggregate(selectFields);
        String groupBy = groupByList.poll();
        HashMap<String, Object> fieldMap = MapUtil.of(DSL_TERMS, MapUtil.of(DSL_AGGREGATIONS_FIELD, groupBy));
        fieldMap.put(DSL_AGGREGATIONS, handleAggregate(selectFields, groupByList));
        return MapUtil.of(groupBy, fieldMap);
    }

    /**
     * 处理聚合函数
     * {分组字段 : 配置}
     *
     * @param selectFieldList 查询字段
     * @return {@link Map}
     */
    private Map<String, Object> handleAggregate(List<SelectField> selectFieldList) {
        if (CollUtil.isEmpty(selectFieldList)) return null;
        Map<String, Object> result = MapUtil.newHashMap(2);
        for (SelectField field : selectFieldList) {
            String method = field.getMethodName();
            if (StrUtil.isEmpty(method)) continue;
            String fieldName = field.getName();
            String alias = field.getAlias();
            if (StrUtil.equals(method, DSL_COUNT)) {
                method = DSL_VALUE_COUNT;
                if (StrUtil.equals(fieldName, "*")) fieldName = "_index";
            }
            result.put(alias, MapUtil.of(method, MapUtil.of(DSL_AGGREGATIONS_FIELD, fieldName)));
        }
        return result;
    }

    /**
     * 获取值
     *
     * @param sqlExpr 表达式
     * @return {@link String}
     */
    private String getValue(SQLExpr sqlExpr) {
        String value = StrUtil.EMPTY;
        if (sqlExpr instanceof SQLIntegerExpr) {
            value = ((SQLIntegerExpr) sqlExpr).getNumber().toString();
        } else if (sqlExpr instanceof SQLCharExpr) {
            value = ((SQLCharExpr) sqlExpr).getText();
        } else if (sqlExpr instanceof SQLNumberExpr) {
            value = ((SQLNumberExpr) sqlExpr).getNumber().toString();
        } else if (sqlExpr instanceof SQLMethodInvokeExpr) {
            SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr) sqlExpr;
            String methodName = methodInvokeExpr.getMethodName();
            List<SQLExpr> arguments = methodInvokeExpr.getArguments();
            if (StrUtil.containsIgnoreCase("concat", methodName)) {
                value = arguments.stream().map(this::getValue).collect(Collectors.joining());
            } else if (StrUtil.equalsAnyIgnoreCase(methodName, "lower", "upper")) {
                return getValue(CollUtil.getFirst(arguments));
            } else if (StrUtil.equalsAnyIgnoreCase(methodName, "to_timestamp", "from_unixtime")) {
                String tmp = getValue(CollUtil.getFirst(arguments));
                return CollUtil.getFirst(StrUtil.split(tmp, StrUtil.DOT));
            }
        } else if (sqlExpr instanceof SQLCastExpr) {
            SQLCastExpr sqlCastExpr = (SQLCastExpr) sqlExpr;
            return getValue(sqlCastExpr.getExpr());
        } else if (sqlExpr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlExpr;
            return getValue(sqlBinaryOpExpr.getLeft()) + StrUtil.DOT
                    + getValue(sqlBinaryOpExpr.getRight());
        } else if (sqlExpr instanceof SQLNullExpr) {
            value = null;
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
