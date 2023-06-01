package com.c3stones.json.mapper.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码
 *
 * @author CL
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    PROVIDER_MISS_ERROR(
            -1,
            "参数为空",
            "请调用#{com.c3stones.json.parser.convert.JQLConvert.convert(String sql)}方法得到#{com.c3stones.json.mapper.JSONMapperProvider}"
    ),
    DATA_MISS_ERROR(
            -2,
            "数据为空",
            "请调用#{com.*.*.biz.mapper.json.JSONMapperProvider.setDataModel(List<DataModel> dataList)}设置数据。"
    ),
    JQL_MISS_ERROR(
            -3,
            "JQL为空",
            "请检查#{com.*.*.biz.mapper.json.JSONMapperProvider}中的#{jql}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.convert(String sql)}方法设置。"
    ),
    JQL_WHERE_RELATION_MISS_ERROR(
            -4,
            "JQL Where中Relation为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{where}属性中的#{relation}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleWhere(SQLExpr sqlExpr)}方法设置，可能是使用了除[and、or]之外的连接符。"
    ),
    JQL_WHERE_BINARYOPEXPR_FIELDNAME_MISS_ERROR(
            -5,
            "JQL Where条件中二位操作表达式的属性名称为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{where}属性中的#{binaryOpExprList}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleWhere(SQLExpr sqlExpr)}方法设置，可能是使用了除[json_contain、json_not_contain、jsonb_array_length]之外的函数包装属性名称。"
    ),
    JQL_WHERE_BINARYOPEXPR_OPERATOR_MISS_ERROR(
            -6,
            "JQL Where条件中二位操作表达式的操作符为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{where}属性中的#{binaryOpExprList}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleWhere(SQLExpr sqlExpr)}方法设置，可能是使用了除[=、!=、>、>=、<、<=、<>、like、not like、is、is not]之外的操作符，或者使用了代码中未适配的函数调用。"
    ),
    JQL_AGGREGATION_TYPE_MISS_ERROR(
            -7,
            "JQL 聚合操作中聚合类型为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{binaryOpExprList}属性中的#{aggregationType}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleAggregate(List<SelectField> selectFieldList)}方法设置，可能是使用了除[count、max、min、avg、sum]之外的聚合函数。"
    ),
    JQL_AGGREGATION_FIELDNAME_MISS_ERROR(
            -8,
            "JQL 聚合操作中属性名称为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{binaryOpExprList}属性中的#{fieldName}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleAggregate(List<SelectField> selectFieldList)}方法设置，可能是没有设置属性。"
    ),
    JQL_GROUP_FIELNAME_MISS_ERROR(
            -9,
            "JQL 分组属性为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{group}属性中的#{groupNameList}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleGroup(SQLSelectGroupByClause sqlSelectGroupByClause)}方法设置，如果设置了属性别名请使用别名，否则请检查是否和属性名称一致，很小可能是没有设置分组属性或使用了函数进行了包装。"
    ),
    JQL_SORT_FIELDNAME_MISS_ERROR(
            -10,
            "JQL 排序中属性名称为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{sorts}属性中的#{fieldName}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleOrderBy(SQLOrderBy sqlOrderBy)}方法设置，可能是没有设置参数，如果设置了属性别名请使用别名，否则请检查是否和属性名称一致，很小可能是没有设置排序属性。"
    ),
    JQL_FROM_MISS_ERROR(
            -11,
            "JQL 偏移数为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{from}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleLimit(SQLLimit sqlLimit)}方法设置，它可能小于0，或者没有设置值。"
    ),
    JQL_SIZE_MISS_ERROR(
            -12,
            "JQL 限制数为空",
            "请检查#{com.c3stones.json.mapper.model.JQLModel}中的#{size}属性，它由#{com.c3stones.json.parser.convert.JQLConvert.handleLimit(SQLLimit sqlLimit)}方法设置，它可能小于0，或者没有设置值。"
    ),
    FILTER_EXECUTE_ERROR(
            -13,
            "过滤二元表达式异常",
            "请检查SQL中的Where条件，可能在#{com.c3stones.json.parser.convert.JQLConvert.convert(String sql)}方法中处理#{com.c3stones.json.mapper.model.JQLWhereModel}时存在不支持的语法，也可能在将不同的数据类型转换成通用的Number或String类型处理时发生异常。如果您熟悉#{com.c3stones.json.mapper.model.JQLWhereModel}的数据定义，直接检查它将会提高解决的效率。"
    ),
    ;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 原因
     */
    private final String reason;

    /**
     * 建议
     */
    private final String suggest;

}
