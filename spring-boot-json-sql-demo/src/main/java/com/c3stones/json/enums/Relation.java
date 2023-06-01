package com.c3stones.json.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 条件关系 枚举类
 *
 * @author CL
 */
@Getter
@AllArgsConstructor
public enum Relation {

    AND(" and "),
    OR(" or "),
    NOT(" not "),
    ;

    private final String value;

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return {@link Relation}
     */
    public static Relation findByValue(String value) {
        if (StrUtil.isNotEmpty(value)) {
            for (Relation relation : values()) {
                if (StrUtil.equalsIgnoreCase(relation.getValue().trim(), value)) {
                    return relation;
                }
            }
        }
        return AND;
    }

    /**
     * 反转关系
     *
     * @param relation 关系
     * @return {@link Relation}
     */
    public static Relation reverse(Relation relation) {
        if (Objects.equals(AND, relation)) {
            return OR;
        } else if (Objects.equals(OR, relation)) {
            return AND;
        }
        return relation;
    }

}
