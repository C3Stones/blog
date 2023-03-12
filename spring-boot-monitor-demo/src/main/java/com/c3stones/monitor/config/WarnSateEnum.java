package com.c3stones.monitor.config;

import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 预警状态枚举类
 *
 * @author  CL
 */
@Getter
@AllArgsConstructor
public enum WarnSateEnum {

    /**
     * 正常
     */
    GREEN(1),

    /**
     * 警告
     */
    YELLOW(2),

    /**
     * 紧急
     */
    RED(3),
    ;

    private final int value;

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return {@link WarnSateEnum}
     */
    public static WarnSateEnum findByValue(Integer value) {
        if (Objects.nonNull(value)) {
            for (WarnSateEnum warnState : values()) {
                if (NumberUtil.equals(warnState.getValue(), value)) {
                    return warnState;
                }
            }
        }
        return GREEN;
    }

    /**
     * 获取高优先级
     *
     * @param warnSateEnum 预警状态
     * @return
     */
    public WarnSateEnum max(WarnSateEnum warnSateEnum) {
        if (Objects.nonNull(warnSateEnum)) {
            return findByValue(Math.max(this.value, warnSateEnum.value));
        }
        return this;
    }

}
