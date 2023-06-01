package com.c3stones.json.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排序类型 枚举类
 *
 * @author CL
 */
@Getter
@AllArgsConstructor
public enum OrderType {

    ASC,
    DESC,
    ;

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return {@link OrderType}
     */
    public static OrderType findByValue(String value) {
        if (StrUtil.isNotEmpty(value)) {
            for (OrderType orderType : values()) {
                if (StrUtil.equalsIgnoreCase(orderType.toString(), value)) {
                    return orderType;
                }
            }
        }
        return ASC;
    }

}
