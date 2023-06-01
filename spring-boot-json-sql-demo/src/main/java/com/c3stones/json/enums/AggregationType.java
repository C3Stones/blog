package com.c3stones.json.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聚合类型 枚举类
 *
 * @author CL
 */
@Getter
@AllArgsConstructor
public enum AggregationType {

    COUNT("count", "统计"),
    MAX("max", "最大值"),
    MIN("min", "最小值"),
    AVG("avg", "平均值"),
    SUM("sum", "求和"),
    ;

    private final String value;
    private final String name;

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return {@link AggregationType}
     */
    public static AggregationType findByValue(String value) {
        if (StrUtil.isNotEmpty(value)) {
            for (AggregationType aggregationType : values()) {
                if (StrUtil.equalsIgnoreCase(aggregationType.getValue(), value)) {
                    return aggregationType;
                }
            }
        }
        return null;
    }

}
