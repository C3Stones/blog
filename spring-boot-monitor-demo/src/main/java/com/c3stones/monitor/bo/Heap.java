package com.c3stones.monitor.bo;

import com.c3stones.monitor.print.Layout;
import com.c3stones.monitor.utils.ByteUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 堆/非堆信息
 *
 * @author CL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Heap {

    /**
     * 堆初始化大小
     */
    private long heapInit;

    /**
     * 堆最大大小
     */
    private long heapMaxMemory;

    /**
     * 堆已用大小
     */
    private long heapUsedMemory;

    /**
     * 堆空闲大小
     */
    private long heapFreeMemory;

    /**
     * 非堆初始化大小
     */
    private long nonHeapInit;

    /**
     * 非堆最大大小
     */
    private long nonHeapMaxMemory;

    /**
     * 非堆已用大小
     */
    private long nonHeapUsedMemory;

    /**
     * 非堆空闲大小
     */
    private long nonHeapFreeMemory;

    @Override
    public String toString() {
        return Layout.Table.of(
                Layout.Row.of("Heap Init Size", ByteUtil.formatByteSize(heapInit)),
                Layout.Row.of("Heap Max Size", ByteUtil.formatByteSize(heapMaxMemory)),
                Layout.Row.of("Heap Used Size", ByteUtil.formatByteSize(heapUsedMemory)),
                Layout.Row.of("Heap Free Size", ByteUtil.formatByteSize(heapFreeMemory)),
                Layout.Row.of("NonHeap Init Size", ByteUtil.formatByteSize(nonHeapInit)),
                Layout.Row.of("NonHeap Max Size", ByteUtil.formatByteSize(nonHeapMaxMemory)),
                Layout.Row.of("NonHeap Used Size", ByteUtil.formatByteSize(nonHeapUsedMemory)),
                Layout.Row.of("NonHeap Free Size", ByteUtil.formatByteSize(nonHeapFreeMemory))
        ).toString();
    }

}
