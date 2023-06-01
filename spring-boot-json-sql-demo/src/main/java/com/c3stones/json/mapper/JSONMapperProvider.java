package com.c3stones.json.mapper;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.c3stones.json.mapper.model.DataModel;
import com.c3stones.json.mapper.model.JQLModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * JSON Mapper Provider
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JSONMapperProvider {

    /**
     * 数据来源
     */
    @Deprecated
    private String json;

    /**
     * 数据模型
     */
    private List<DataModel> dataList;

    /**
     * JSONSQL
     */
    private JQLModel jql;

    /**
     * 包含列
     */
    private String[] includes;

    /**
     * 排除列
     */
    private String[] excludes;

    /**
     * SQL
     */
    private String sql;

    public JSONMapperProvider(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        StringJoiner str = new StringJoiner(StrUtil.LF);
        str.add("data : " + Opt.ofNullable(dataList)
                .orElse(ListUtil.empty()).stream().map(DataModel::toString)
                .collect(Collectors.joining(StrUtil.LF)));
        str.add("jql : " + jql);
        if (ArrayUtil.isNotEmpty(includes)) {
            str.add("includes : " + JSONObject.toJSONString(includes));
        }
        if (ArrayUtil.isNotEmpty(excludes)) {
            str.add("excludes : " + JSONObject.toJSONString(excludes));
        }
        str.add("sql : " + sql);

        return str.toString();
    }

}
