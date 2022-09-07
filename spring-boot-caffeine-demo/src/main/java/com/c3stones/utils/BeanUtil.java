package com.c3stones.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Objects;

/**
 * Bean 工具类
 *
 * @author CL
 */
public class BeanUtil extends BeanUtils {

    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullProperties(source));
    }

    /**
     * 获取为Null的属性
     *
     * @param obj
     * @return
     */
    public static String[] getNullProperties(Object obj) {
        final BeanWrapper src = new BeanWrapperImpl(obj);
        return Arrays.stream(src.getPropertyDescriptors())
                .filter(pd -> Objects.isNull(src.getPropertyValue(pd.getName())))
                .map(PropertyDescriptor::getName)
                .distinct()
                .toArray(String[]::new);
    }
}
