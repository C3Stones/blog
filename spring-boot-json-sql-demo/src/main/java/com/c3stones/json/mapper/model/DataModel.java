package com.c3stones.json.mapper.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON 数据模型
 *
 * @author CL
 */
@Data
@NoArgsConstructor
public class DataModel {

    /**
     * 数据
     */
    private JSONObject data;

    public DataModel(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}