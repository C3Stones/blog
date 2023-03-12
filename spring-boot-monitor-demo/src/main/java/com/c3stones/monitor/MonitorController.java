package com.c3stones.monitor;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.c3stones.monitor.bo.Thread;
import com.c3stones.monitor.bo.*;
import com.c3stones.monitor.config.WarnConfig;
import com.c3stones.monitor.config.WarnSateEnum;
import com.c3stones.monitor.print.Layout;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.lang.management.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 监控 Controller
 *
 * @author CL
 */
@Slf4j
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private WarnConfig warnConfig;

    /**
     * 巡检
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void patrol() {
        // 发现预警
        warn();
    }

    /**
     * 预警
     *
     * @return {@link WarnSateEnum}
     */
    @GetMapping("/warn")
    public WarnSateEnum warn() {
        // 不开启，则返回空
        if (BooleanUtil.isFalse(warnConfig.getEnabled())) return null;

        WarnSateEnum result = WarnSateEnum.GREEN;

        // 判断CPU
        Cpu cpu = cpu();
        if (NumberUtil.sub(cpu.getSystemCpuUsage().doubleValue() * 100, warnConfig.getCpuStage2().doubleValue()) >= 0 ||
                NumberUtil.sub(cpu.getProcessCpuUsage().doubleValue() * 100, warnConfig.getCpuStage2().doubleValue()) >= 0) {
            result = result.max(WarnSateEnum.RED);
            log.error("Monitoring capability detection cpu warning!!!" + StrUtil.LF + cpu);
        } else if (NumberUtil.sub(cpu.getSystemCpuUsage().doubleValue() * 100, warnConfig.getCpuStage1().doubleValue()) >= 0 ||
                NumberUtil.sub(cpu.getProcessCpuUsage().doubleValue() * 100, warnConfig.getCpuStage1().doubleValue()) >= 0) {
            result = result.max(WarnSateEnum.YELLOW);
            log.warn("Monitoring capability detection cpu warning!!!" + StrUtil.LF + cpu);
        }

        // 判断内存
        Memory memory = memory();
        if (NumberUtil.sub(memory.getPhysicalMemoryUsage().doubleValue() * 100, warnConfig.getMemoryStage2().doubleValue()) >= 0 ||
                NumberUtil.sub(memory.getMemoryUsage().doubleValue() * 100, warnConfig.getMemoryStage2().doubleValue()) >= 0) {
            result = result.max(WarnSateEnum.RED);
            log.warn("Monitoring capability detection memory warning!!!" + StrUtil.LF + memory);
        } else if (NumberUtil.sub(memory.getPhysicalMemoryUsage().doubleValue() * 100, warnConfig.getMemoryStage1().doubleValue()) >= 0 ||
                NumberUtil.sub(memory.getMemoryUsage().doubleValue() * 100, warnConfig.getMemoryStage1().doubleValue()) >= 0) {
            result = result.max(WarnSateEnum.YELLOW);
            log.warn("Monitoring capability detection memory warning!!!" + StrUtil.LF + memory);
        }

        // 判断磁盘
        Disk disk = disk();
        double diskTotalUsage = disk.getDevices().stream().map(Disk.Drive::getDriveUsage).mapToDouble(BigDecimal::doubleValue).sum();
        if (NumberUtil.sub(diskTotalUsage * 100, warnConfig.getDiskStage2().doubleValue()) >= 0) {
            result = result.max(WarnSateEnum.RED);
            log.warn("Monitoring capability detection disk warning!!!" + StrUtil.LF + disk);
        } else if (NumberUtil.sub(diskTotalUsage * 100, warnConfig.getDiskStage1().doubleValue()) >= 0) {
            result = result.max(WarnSateEnum.YELLOW);
            log.warn("Monitoring capability detection disk warning!!!" + StrUtil.LF + disk);
        }

        return result;
    }

    /**
     * 打印
     *
     * @return {@link String}
     */
    @GetMapping({"", "/print"})
    public String print() {
        return Stream.of(
                Layout.of("Server Info", server()),
                Layout.of("JVM", jvm()),
                Layout.of("CPU", cpu()),
                Layout.of("Memory", memory()),
                Layout.of("Disk", disk()),
                Layout.of("Heap/NonHeap", heap()),
                Layout.of("Thread", thread()),
                Layout.of("GC", gc())
        ).map(Layout::toString).collect(Collectors.joining(StrUtil.LF));
    }

    /**
     * 服务器信息
     *
     * @return {@link Server}
     */
    @SneakyThrows
    private Server server() {
        InetAddress inetAddress = InetAddress.getLocalHost();
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        return Server.builder()
                .hostName(inetAddress.getHostName())
                .osName(operatingSystemMXBean.getName())
                .osVersion(operatingSystemMXBean.getVersion())
                .arch(operatingSystemMXBean.getArch())
                .localIp(inetAddress.getHostAddress())
                .build();
    }

    /**
     * Java虚拟机信息
     *
     * @return {@link Jvm}
     */
    private Jvm jvm() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Map<String, String> systemProperties = runtimeMXBean.getSystemProperties();
        return Jvm.builder()
                .vmName(runtimeMXBean.getVmName())
                .jdkVersion(systemProperties.get("java.runtime.version"))
                .javaHome(systemProperties.get("java.home"))
                .pid(systemProperties.get("PID"))
                .startTime(LocalDateTimeUtil.of(runtimeMXBean.getStartTime()))
                .runtime(runtimeMXBean.getUptime())
                .startParameters(runtimeMXBean.getInputArguments())
                .build();
    }

    /**
     * CPU
     *
     * @return {@link Cpu}
     */
    private Cpu cpu() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        JSONObject operatingSystemJson = JSON.parseObject(JSON.toJSONString(operatingSystemMXBean));
        return Cpu.builder()
                .availableProcssors(operatingSystemMXBean.getAvailableProcessors())
                .systemCpuUsage(operatingSystemJson.getBigDecimal("systemCpuLoad"))
                .processCpuUsage(operatingSystemJson.getBigDecimal("processCpuLoad"))
                .build();
    }

    /**
     * 内存
     *
     * @return {@link Memory}
     */
    private Memory memory() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        JSONObject operatingSystemJson = JSON.parseObject(JSON.toJSONString(operatingSystemMXBean));
        long totalPhysicalMemory = operatingSystemJson.getLongValue("totalPhysicalMemorySize");
        long freePhysicalMemory = operatingSystemJson.getLongValue("freePhysicalMemorySize");
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
        Runtime runtime = Runtime.getRuntime();
        return Memory.builder()
                .totalPhysicalMemory(totalPhysicalMemory)
                .freePhysicalMemory(freePhysicalMemory)
                .usedPhysicalMemory(usedPhysicalMemory)
                .physicalMemoryUsage(NumberUtil.toBigDecimal(NumberUtil.div(usedPhysicalMemory, totalPhysicalMemory)))
                .totalMemory(runtime.totalMemory())
                .freeMemory(runtime.freeMemory())
                .usedMemory(runtime.totalMemory() - runtime.freeMemory()).maxMemory(runtime.maxMemory())
                .maxUseMemory(runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory())
                .memoryUsage(NumberUtil.toBigDecimal(NumberUtil.div(runtime.totalMemory() - runtime.freeMemory(), runtime.totalMemory())))
                .build();
    }

    /**
     * 磁盘信息
     *
     * @return {@link Disk}
     */
    private Disk disk() {
        List<Disk.Drive> drives = Stream.of(File.listRoots()).map(file -> Disk.Drive.builder()
                .name(file.toString())
                .totalSpace(file.getTotalSpace())
                .freeSpace(file.getFreeSpace())
                .usedSpace(file.getTotalSpace() - file.getFreeSpace())
                .driveUsage(NumberUtil.toBigDecimal(NumberUtil.div(file.getTotalSpace() - file.getFreeSpace(), file.getTotalSpace())))
                .build()
        ).collect(Collectors.toList());
        return new Disk(drives);
    }

    /**
     * 堆/非堆
     *
     * @return {@link StringBuilder}
     */
    private Heap heap() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        return Heap.builder()
                .heapInit(heapMemoryUsage.getInit())
                .heapMaxMemory(heapMemoryUsage.getMax())
                .heapUsedMemory(heapMemoryUsage.getUsed())
                .heapFreeMemory(heapMemoryUsage.getMax() - heapMemoryUsage.getUsed())
                .nonHeapInit(nonHeapMemoryUsage.getInit())
                .nonHeapMaxMemory(nonHeapMemoryUsage.getMax())
                .nonHeapUsedMemory(nonHeapMemoryUsage.getUsed())
                .nonHeapFreeMemory(nonHeapMemoryUsage.getMax() - nonHeapMemoryUsage.getUsed())
                .build();
    }

    /**
     * 线程信息
     *
     * @return {@link Thread}
     */
    private Thread thread() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        return Thread.builder().threadCount(threadMXBean.getThreadCount()).peakThreadCount(threadMXBean.getPeakThreadCount()).daemonThreadCount(threadMXBean.getDaemonThreadCount()).nonDaemonThreadCount(threadMXBean.getThreadCount() - threadMXBean.getDaemonThreadCount()).totalStartedThreadCount(threadMXBean.getTotalStartedThreadCount()).build();
    }

    /**
     * 垃圾回收信息
     *
     * @return {@link Gc}
     */
    private Gc gc() {
        long collectionCount = 0, collectionTime = 0;
        for (GarbageCollectorMXBean garbageCollectorMXBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            collectionCount += garbageCollectorMXBean.getCollectionCount();
            collectionTime += garbageCollectorMXBean.getCollectionTime();
        }
        return Gc.builder().collectionCount(collectionCount).collectionTime(collectionTime).build();
    }

}
