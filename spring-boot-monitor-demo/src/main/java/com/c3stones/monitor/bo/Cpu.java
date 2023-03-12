package com.c3stones.monitor.bo;

import cn.hutool.core.util.NumberUtil;
import com.c3stones.monitor.print.Layout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * CPU信息
 *
 * @author  CL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cpu {

    /**
     * 可用处理器数
     */
    private int availableProcssors;

    /**
     * 系统CPU使用率
     */
    private BigDecimal systemCpuUsage;

    /**
     * 当前进程CPU使用率
     */
    private BigDecimal processCpuUsage;

    @Override
    public String toString() {
        return Layout.Table.of(
                Layout.Row.of("Available Processors", availableProcssors),
                Layout.Row.of("System CPU Usage", NumberUtil.mul(systemCpuUsage, 100).setScale(2, RoundingMode.HALF_UP) + "%"),
                Layout.Row.of("Process CPU Usage", NumberUtil.mul(processCpuUsage, 100).setScale(2, RoundingMode.HALF_UP) + "%")
        ).toString();
    }

}
