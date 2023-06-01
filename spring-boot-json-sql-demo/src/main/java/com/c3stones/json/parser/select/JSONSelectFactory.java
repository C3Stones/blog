package com.c3stones.json.parser.select;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import com.c3stones.json.parser.select.impl.StringSelect;
import com.c3stones.json.parser.select.impl.NumberSelect;

import java.util.List;
import java.util.Objects;

/**
 * JSONSQL 查询语法处理工厂
 *
 * @author CL
 */
public class JSONSelectFactory {

    /**
     * 根据数据自动匹配查询语法实现类
     *
     * @param obj 数据
     * @return {@link JSONDefaultSelect}
     */
    public static JSONDefaultSelect match(Object obj) {
        if (Objects.nonNull(obj) && Number.class.isAssignableFrom(obj.getClass())) {
            return match(Number.class);
        }
        return any();
    }

    /**
     * 根据数据自动匹配查询语法实现类
     *
     * @param list 数据
     * @return {@link JSONDefaultSelect}
     */
    public static JSONDefaultSelect match(List<Object> list) {
        if (Opt.ofNullable(list).orElse(ListUtil.empty()).stream().allMatch(value ->
                Number.class.isAssignableFrom(value.getClass()))) {
            return numberSelect;
        }
        return stringSelect;
    }

    /**
     * 根据数据类型自动匹配查询语法实现类
     *
     * @param clazz 数据类型
     * @return {@link JSONDefaultSelect}
     */
    public static JSONDefaultSelect match(Class<?> clazz) {
        if (Objects.nonNull(clazz) && Number.class.isAssignableFrom(clazz)) {
            return numberSelect;
        }
        return stringSelect;
    }

    /**
     * 随机获取查询语法实现类
     *
     * @return {@link JSONDefaultSelect}
     */
    public static JSONDefaultSelect any() {
        return stringSelect;
    }

    /**
     * JSONSQL 数字查询语法
     */
    private static final JSONDefaultSelect numberSelect = new NumberSelect();

    /**
     * JSONSQL 字符串查询语法
     */
    private static final JSONDefaultSelect stringSelect = new StringSelect();

}
