package com.c3stones.monitor.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 预警配置
 *
 * @author  CL
 */
@Getter
@Component
public class WarnConfig {

    /**
     * 预警开关
     */
    @Value("${monitor.warn.enabled:true}")
    private Boolean enabled;

    /**
     * CPU - 阶段1
     */
    @Value("${monitor.warn.cpu.stage1:85}")
    private Double cpuStage1;

    /**
     * CPU - 阶段2
     */
    @Value("${monitor.warn.cpu.stage2:95}")
    private Double cpuStage2;

    /**
     * 内存 - 阶段1
     */
    @Value("${monitor.warn.memory.stage1:85}")
    private Double memoryStage1;

    /**
     * 内存 - 阶段2
     */
    @Value("${monitor.warn.memory.stage2:95}")
    private Double memoryStage2;

    /**
     * 磁盘 - 阶段1
     */
    @Value("${monitor.warn.disk.stage1:85}")
    private Double diskStage1;

    /**
     * 磁盘 - 阶段2
     */
    @Value("${monitor.warn.disk.stage2:95}")
    private Double diskStage2;

}
