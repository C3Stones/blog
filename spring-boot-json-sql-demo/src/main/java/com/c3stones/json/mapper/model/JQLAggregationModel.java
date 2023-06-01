package com.c3stones.json.mapper.model;

import com.alibaba.fastjson2.JSONObject;
import com.c3stones.json.enums.AggregationType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON 聚合模型
 *
 * @author CL
 */
@Data
@NoArgsConstructor
public class JQLAggregationModel {

    /**
     * 聚合类型
     */
    private AggregationType aggregationType;

    /**
     * 属性名称
     */
    private String fieldName;

    /**
     * 别名
     */
    private String alias;

    public JQLAggregationModel(AggregationType aggregationType, String fieldName, String alias) {
        this.aggregationType = aggregationType;
        this.fieldName = fieldName;
        this.alias = alias;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}