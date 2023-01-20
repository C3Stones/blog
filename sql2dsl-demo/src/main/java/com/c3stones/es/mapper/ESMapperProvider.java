package com.c3stones.es.mapper;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

import static com.c3stones.es.constants.Constant.*;

/**
 * Elasticsearch 通用 Mapper Provider
 *
 * @author CL
 */
@Data
public class ESMapperProvider {

    /**
     * 索引名称
     */
    private String index;

    /**
     * DSL
     */
    private DslModel dsl;

    /**
     * 包含列
     */
    private String[] includes;

    /**
     * 排除列
     */
    private String[] excludes;

    public ESMapperProvider(String index, String dsl, String[] includes) {
        this.index = index;
        this.dsl = new DslModel(dsl);
        this.includes = ArrayUtil.isNotEmpty(includes) ? includes : new String[0];
        this.excludes = new String[0];
    }

    @Override
    public String toString() {
        StringJoiner str = new StringJoiner(StrUtil.LF);
        str.add("index : " + index);
        str.add("dsl : " + dsl);
        if (ArrayUtil.isNotEmpty(includes)) {
            str.add("includes : " + JSONUtil.toJsonStr(includes));
        }
        if (ArrayUtil.isNotEmpty(excludes)) {
            str.add("excludes : " + JSONUtil.toJsonStr(excludes));
        }
        return str.toString();
    }

    /**
     * DSL 结构模型
     */
    @Data
    @NoArgsConstructor
    public static class DslModel {

        /**
         * 查询
         */
        private JSONObject query;

        /**
         * 聚合
         */
        private JSONObject aggregations;

        /**
         * 排序
         */
        private JSONArray sort;

        /**
         * 起始位置
         */
        private Integer from;

        /**
         * 大小
         */
        private Integer size;

        public DslModel(String dsl) {
            JSONObject parseObj = JSONUtil.parseObj(dsl);
            this.query = Opt.ofNullable(parseObj).map(obj -> obj.getJSONObject(DSL_QUERY)).get();
            this.aggregations = Opt.ofNullable(parseObj).map(obj -> obj.getJSONObject(DSL_AGGREGATIONS)).get();
            this.sort = Opt.ofNullable(parseObj).map(obj -> obj.getJSONArray(DSL_SORT)).get();
            this.from = Opt.ofNullable(parseObj).map(obj -> obj.getInt(DSL_FROM)).get();
            this.size = Opt.ofNullable(parseObj).map(obj -> obj.getInt(DSL_SIZE)).get();
        }

        @Override
        public String toString() {
            return JSONUtil.toJsonStr(this);
        }

    }

}
