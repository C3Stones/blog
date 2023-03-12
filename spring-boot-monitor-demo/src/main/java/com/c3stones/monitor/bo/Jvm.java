package com.c3stones.monitor.bo;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.c3stones.monitor.print.Layout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_FORMATTER;

/**
 * JVM信息
 *
 * @author CL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jvm {

    /**
     * 虚拟机名称
     */
    private String vmName;

    /**
     * JDK版本
     */
    private String jdkVersion;

    /**
     * JavaHome
     */
    private String javaHome;

    /**
     * 进程号
     */
    private String pid;

    /**
     * 启动时间
     */
    private LocalDateTime startTime;

    /**
     * 运行时长
     */
    private long runtime;

    /**
     * 启动餐参数
     */
    private List<String> startParameters;

    @Override
    public String toString() {
        return Layout.Table.of(
                Layout.Row.of("VM Name", vmName),
                Layout.Row.of("JDK Version", jdkVersion),
                Layout.Row.of("Java Home", javaHome),
                Layout.Row.of("PID", pid),
                Layout.Row.of("StartTime", NORM_DATETIME_FORMATTER.format(startTime)),
                Layout.Row.of("RunTime", DateUtil.formatBetween(runtime, BetweenFormatter.Level.SECOND)
                        .replaceAll(BetweenFormatter.Level.SECOND.getName(), "s")
                        .replaceAll(BetweenFormatter.Level.MINUTE.getName(), "m")
                        .replaceAll(BetweenFormatter.Level.HOUR.getName(), "h")
                        .replaceAll(BetweenFormatter.Level.DAY.getName(), "d")),
                Layout.Row.of("Start Parameters", startParameters.size(), startParameters)
        ).toString();
    }

}
