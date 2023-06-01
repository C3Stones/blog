package com.c3stones.json.mapper.model;

import com.alibaba.fastjson2.JSONObject;
import com.c3stones.json.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON 排序模型
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JQLSortModel {

    /**
     * 属性名称
     */
    private String fieldName;

    /**
     * 排序类型
     */
    private OrderType orderType;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}