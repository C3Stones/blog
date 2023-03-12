package com.c3stones.monitor.bo;

import com.c3stones.monitor.print.Layout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线程信息
 *
 * @author CL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Thread {

    /**
     * 活动线程数
     */
    private int threadCount;

    /**
     * 峰值活动线程数
     */
    private int peakThreadCount;

    /**
     * 守护线程数
     */
    private int daemonThreadCount;

    /**
     * 非守护线程数
     */
    private int nonDaemonThreadCount;

    /**
     * 总线程数
     */
    private long totalStartedThreadCount;

    @Override
    public String toString() {
        return Layout.Table.of(
                Layout.Row.of("Active Thread Count", threadCount),
                Layout.Row.of("Active Peak Thread Count", peakThreadCount),
                Layout.Row.of("Daemon Thread Count", daemonThreadCount),
                Layout.Row.of("NonDaemon Thread Count", nonDaemonThreadCount),
                Layout.Row.of("Total Thread Count", totalStartedThreadCount)
        ).toString();
    }

}
