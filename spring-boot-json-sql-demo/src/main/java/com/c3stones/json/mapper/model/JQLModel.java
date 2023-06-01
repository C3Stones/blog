package com.c3stones.json.mapper.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JSONSQL 结构模型
 *
 * @author CL
 */
@Data
@NoArgsConstructor
public class JQLModel {

    /**
     * 条件
     */
    private JQLWhereModel where;

    /**
     * 聚合
     */
    private List<JQLAggregationModel> aggregations;

    /**
     * 分组
     */
    private JQLGroupModel group;

    /**
     * 排序
     */
    private List<JQLSortModel> sorts;

    /**
     * 起始位置
     */
    private Integer from;

    /**
     * 大小
     */
    private Integer size;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.ReferenceDetection);
    }

}