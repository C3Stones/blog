package com.c3stones.json.mapper.model;

import com.alibaba.fastjson2.JSONObject;
import com.c3stones.json.enums.Operator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * JSONSQL 二元运算表达式
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JQLBinaryOpExpr<T> {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 操作符
     */
    private Operator operator;

    /**
     * 值
     */
    private T[] values;

    @SafeVarargs
    public final JQLBinaryOpExpr setValues(T... values) {
        this.values = values;
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}