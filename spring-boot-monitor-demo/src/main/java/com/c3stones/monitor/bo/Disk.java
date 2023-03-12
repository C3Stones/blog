package com.c3stones.monitor.bo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.c3stones.monitor.print.Layout;
import com.c3stones.monitor.utils.ByteUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 磁盘信息
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disk {

    /**
     * 盘符
     */
    private List<Drive> devices;

    @Override
    public String toString() {
        return Opt.ofNullable(devices).orElse(ListUtil.empty()).stream().map(Drive::toString).collect(Collectors.joining(StrUtil.LF));
    }

    /**
     * 盘符
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Drive {

        /**
         * 盘符名称
         */
        private String name;

        /**
         * 总大小
         */
        private long totalSpace;

        /**
         * 空闲大小
         */
        private long freeSpace;

        /**
         * 已用大小
         */
        private long usedSpace;

        /**
         * 盘符使用率
         */
        private BigDecimal driveUsage;

        @Override
        public String toString() {
            return Layout.Table.of(name,
                    Layout.Row.of("Total Space", ByteUtil.formatByteSize(totalSpace)),
                    Layout.Row.of("Free Space", ByteUtil.formatByteSize(freeSpace)),
                    Layout.Row.of("Used Space", ByteUtil.formatByteSize(usedSpace)),
                    Layout.Row.of("Drive Space", NumberUtil.mul(driveUsage, 100).setScale(2, RoundingMode.HALF_UP) + "%")
            ).toString();
        }
    }

}
