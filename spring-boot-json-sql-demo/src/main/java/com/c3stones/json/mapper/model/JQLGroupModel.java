package com.c3stones.json.mapper.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JSONSQL 分组模型
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JQLGroupModel {

    /**
     * 分组属性名称
     */
    private List<String> groupNameList;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
