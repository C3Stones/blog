package com.c3stones.monitor.print;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 打印布局
 *
 * @author CL
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Layout {

    // 横线和竖线
    private final static String H_LINE = "-";
    private final static String V_LINE = "┊";

    /**
     * 大标题
     */
    private String headline;

    /**
     * 数据
     */
    private ArrayList<Object> objs;

    /**
     * 自定义构造方法
     *
     * @param headline 大标题
     * @param objs     数据
     */
    private Layout(String headline, Object... objs) {
        this.headline = headline;
        this.objs = CollUtil.toList(objs);
    }

    /**
     * 自定义静态构造方法
     *
     * @param headline 大标题
     * @param objs     数据
     * @return {@link Layout}
     */
    public static Layout of(String headline, Object... objs) {
        return new Layout(headline, objs);
    }

    /**
     * 重写toString方法
     *
     * @return {@link String}
     */
    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(StrUtil.LF);
        if (Objects.nonNull(headline)) {
            sj.add(headline + StrUtil.COLON);
            sj.add(StrUtil.EMPTY);
        }
        objs.forEach(obj -> sj.add(obj.toString()));
        sj.add(StrUtil.EMPTY);
        return sj.toString();
    }


    /**
     * 表格
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Table {

        /**
         * 标题
         */
        private String title;

        /**
         * 行
         */
        private ArrayList<Row> rows;

        /**
         * 自定义构造方法
         *
         * @param title 标题
         * @param rows  行
         */
        private Table(String title, Row... rows) {
            this.title = title;
            this.rows = CollUtil.toList(rows);
        }

        /**
         * 自定义静态构造方法
         *
         * @param title 标题
         * @param rows  行
         * @return {@link Table}
         */
        public static Table of(String title, Row... rows) {
            return new Table(title, rows);
        }

        /**
         * 自定义静态构造方法
         *
         * @param rows 行
         * @return {@link Table}
         */
        public static Table of(Row... rows) {
            return new Table(null, rows);
        }

        /**
         * 重写ToString方法
         *
         * @return {@link String}
         */
        @Override
        public String toString() {
            int maxKeyLength = rows.stream().map(Row::getKey).map(StrUtil::length).max(Comparator.comparing(Integer::valueOf)).orElse(1) + 5;
            int maxValueLength = rows.stream().map(Row::getValues).flatMap(Collection::stream).map(StrUtil::toStringOrNull).map(StrUtil::length).max(Comparator.comparing(Integer::valueOf)).orElse(1) + 5;
            int totalLength = maxKeyLength + maxValueLength + 3;
            // 分割行
            String spit = StrUtil.SPACE + IntStream.range(0, totalLength).mapToObj(i -> H_LINE).collect(Collectors.joining()) + StrUtil.SPACE;
            StringJoiner sj = new StringJoiner(StrUtil.LF);
            if (Objects.nonNull(title)) {
                sj.add(spit);
                sj.add(V_LINE + StrUtil.padAfter(title, totalLength, StrUtil.SPACE) + V_LINE);
            }
            sj.add(spit);
            rows.forEach(row -> sj.add(row.toString(maxKeyLength, maxValueLength)));
            sj.add(spit);
            return sj.toString();
        }

    }

    /**
     * 行
     */
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Row {

        /**
         * 键
         */
        private final String key;

        /**
         * 跨行
         */
        private final int rowspan;

        /**
         * 值
         */
        private final ArrayList<Object> values;

        /**
         * 自定义静态构造方法
         *
         * @param key   键
         * @param value 值
         * @return {@link Row}
         */
        public static Row of(String key, Object value) {
            return of(key, 1, value);
        }

        /**
         * 自定义静态构造方法
         *
         * @param key     键
         * @param rowspan 跨行
         * @param value   值
         * @return {@link Row}
         */
        public static Row of(String key, int rowspan, Object value) {
            return new Row(key, Math.max(rowspan, 1), value instanceof List ? new ArrayList<>(((List<?>) value)) : CollUtil.toList(value));
        }

        /**
         * 重写ToString方法
         *
         * @param maxKeyLength   最大的键长度
         * @param maxValueLength 最大的值长度
         * @return {@link String}
         */
        public String toString(int maxKeyLength, int maxValueLength) {
            StringJoiner sj = new StringJoiner(StrUtil.LF);
            IntStream.range(0, rowspan).forEach(i -> sj.add(
                    V_LINE + StrUtil.SPACE +
                            String.format("%-" + Math.max(maxKeyLength, 1) + "s", i == 0 ? key : StrUtil.SPACE) +
                            V_LINE + StrUtil.SPACE +
                            String.format("%-" + Math.max(maxValueLength, 1) + "s", values.get(i)) +
                            V_LINE));
            return sj.toString();
        }

    }

}
