package com.c3stones.json.mapper.valid;

import cn.hutool.core.lang.Opt;
import com.c3stones.json.enums.Operator;
import com.c3stones.json.mapper.JSONMapperProvider;
import com.c3stones.json.mapper.exception.ErrorCode;
import com.c3stones.json.mapper.exception.JQLException;
import com.c3stones.json.mapper.model.*;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Objects;

import static cn.hutool.core.collection.CollUtil.newArrayList;
import static cn.hutool.core.collection.ListUtil.empty;

/**
 * JSONSQL 断言
 *
 * @author CL
 */
public class JQLAssert {

    /**
     * 校验JSONSQL语法和
     *
     * @param provider JSON Mapper Provider
     */
    public static void validated(JSONMapperProvider provider) {
        validated((Object) provider);
        validated(provider.getDataList());
        validated(provider.getJql());
        JQLModel jql = provider.getJql();
        validated(jql.getWhere());
        validated(jql.getAggregations());
        validated(jql.getGroup());
        validated(jql.getSorts());
        validated(jql.getFrom());
        validated(jql.getSize());
    }

    /**
     * 对象断言
     *
     * @param obj 对象
     */
    private static void validated(Object obj) {
        if (obj instanceof JSONMapperProvider) {
            notNull(obj, ErrorCode.PROVIDER_MISS_ERROR);
        } else if (obj instanceof JQLModel) {
            notNull(obj, ErrorCode.JQL_MISS_ERROR);
        } else if (obj instanceof JQLWhereModel) {
            JQLWhereModel where = (JQLWhereModel) obj;
            if (Objects.nonNull(where.getBinaryOpExprList()) || Objects.nonNull(where.getChildWhereList()))
                notNull(obj, ErrorCode.JQL_WHERE_RELATION_MISS_ERROR);
            Opt.ofNullable(where.getBinaryOpExprList()).orElse(empty()).forEach(expr -> validated(expr));
            Opt.ofNullable(where.getChildWhereList()).orElse(empty()).forEach(childWhere -> validated(childWhere));
        } else if (obj instanceof JQLBinaryOpExpr) {
            JQLBinaryOpExpr binaryOpExpr = (JQLBinaryOpExpr) obj;
            notNull(binaryOpExpr.getFieldName(), ErrorCode.JQL_WHERE_BINARYOPEXPR_FIELDNAME_MISS_ERROR);
            notNull(binaryOpExpr.getOperator(), ErrorCode.JQL_WHERE_BINARYOPEXPR_OPERATOR_MISS_ERROR);
        } else if (obj instanceof JQLAggregationModel) {
            JQLAggregationModel aggregation = (JQLAggregationModel) obj;
            notNull(aggregation.getAggregationType(), ErrorCode.JQL_AGGREGATION_TYPE_MISS_ERROR);
            notNull(aggregation.getFieldName(), ErrorCode.JQL_AGGREGATION_FIELDNAME_MISS_ERROR);
        } else if (obj instanceof JQLGroupModel) {
            JQLGroupModel group = (JQLGroupModel) obj;
            noNullElements(group.getGroupNameList(), ErrorCode.JQL_GROUP_FIELNAME_MISS_ERROR);
        } else if (obj instanceof JQLSortModel) {
            JQLSortModel sort = (JQLSortModel) obj;
            notNull(sort.getFieldName(), ErrorCode.JQL_SORT_FIELDNAME_MISS_ERROR);
        } else if (obj instanceof Integer) {
            Integer integer = (Integer) obj;
            gte(integer, BigInteger.ZERO.intValue(), ErrorCode.JQL_FROM_MISS_ERROR);
        }
    }

    /**
     * 校验集合参数
     *
     * @param collection 集合
     */
    private static void validated(Collection collection) {
        Opt.ofNullable(collection).orElse(newArrayList()).forEach(obj -> validated(obj));
    }

    /**
     * 对象非空断言
     *
     * @param obj       对象
     * @param errorCode 错误码
     */
    private static void notNull(Object obj, ErrorCode errorCode) {
        Assert.notNull(obj, () -> {
            throw new JQLException(errorCode);
        });
    }

    /**
     * 集合非空断言
     *
     * @param collection 对象
     * @param errorCode  错误码
     */
    private static void noNullElements(Collection collection, ErrorCode errorCode) {
        Assert.notEmpty(collection, () -> {
            throw new JQLException(errorCode);
        });
    }

    /**
     * a大于等于b断言
     *
     * @param a         数字a
     * @param b         数字b
     * @param errorCode 财错误码
     */
    private static void gte(Number a, Number b, ErrorCode errorCode) {
        Assert.isTrue(Operator.judgeNumber(a, Operator.GTE, b), () -> {
            throw new JQLException(errorCode);
        });
    }

}
