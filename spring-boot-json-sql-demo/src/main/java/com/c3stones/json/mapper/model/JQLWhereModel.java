package com.c3stones.json.mapper.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.c3stones.json.enums.Relation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * JSONSQL 查询模型
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JQLWhereModel {

    /**
     * 关系
     */
    private Relation relation;

    /**
     * 二位运算表达式
     */
    private List<JQLBinaryOpExpr> binaryOpExprList;

    /**
     * 子查询模型
     */
    private List<JQLWhereModel> childWhereList;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.ReferenceDetection);
    }

}