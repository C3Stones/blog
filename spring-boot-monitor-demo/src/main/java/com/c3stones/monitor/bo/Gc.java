package com.c3stones.monitor.bo;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.c3stones.monitor.print.Layout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 收集总数
     */
    private long collectionCount;

    /**
     * 收集耗时
     */
    private long collectionTime;

    @Override
    public String toString() {
        return Layout.Table.of(
                Layout.Row.of("Total Collection Count", collectionCount),
                Layout.Row.of("Total Collection Time", DateUtil.formatBetween(collectionTime, BetweenFormatter.Level.MILLISECOND)
                        .replaceAll(BetweenFormatter.Level.MILLISECOND.getName(), "ms")
                        .replaceAll(BetweenFormatter.Level.SECOND.getName(), "s")
                        .replaceAll(BetweenFormatter.Level.MINUTE.getName(), "m")
                        .replaceAll(BetweenFormatter.Level.HOUR.getName(), "h")
                        .replaceAll(BetweenFormatter.Level.DAY.getName(), "d"))
        ).toString();
    }

}
