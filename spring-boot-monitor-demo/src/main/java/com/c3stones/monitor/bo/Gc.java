package com.c3stones.monitor.bo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.c3stones.monitor.print.Layout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 垃圾回收信息
 *
 * @author CL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gc {

    /**
     * GC收集器
     */
    private List<GcCollector> collectors;

    @Override
    public String toString() {
        return Opt.ofNullable(collectors).orElse(ListUtil.empty()).stream().map(Gc.GcCollector::toString).collect(Collectors.joining(StrUtil.LF));
    }

    /**
     * GC收集器
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GcCollector {

        /**
         * 收集器名称
         */
        private String name;

        /**
         * 收集总数
         */
        private long count;

        /**
         * 收集耗时
         */
        private long time;

        @Override
        public String toString() {
            return Layout.Table.of(name,
                    Layout.Row.of("Count", count),
                    Layout.Row.of("Time", DateUtil.formatBetween(time, BetweenFormatter.Level.MILLISECOND)
                            .replaceAll(BetweenFormatter.Level.MILLISECOND.getName(), "ms")
                            .replaceAll(BetweenFormatter.Level.SECOND.getName(), "s")
                            .replaceAll(BetweenFormatter.Level.MINUTE.getName(), "m")
                            .replaceAll(BetweenFormatter.Level.HOUR.getName(), "h")
                            .replaceAll(BetweenFormatter.Level.DAY.getName(), "d"))
            ).toString();
        }

    }

}
