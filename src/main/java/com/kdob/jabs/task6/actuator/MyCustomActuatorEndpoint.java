package com.kdob.jabs.task6.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.lang.management.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Endpoint(id = "custom")
@Component
public class MyCustomActuatorEndpoint {

    @ReadOperation
    public Map<String, String> getCustomInfo() {
        final Map<String, String> map = new HashMap<>();
        // JVM Information
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        map.put("jvm.name", runtimeMXBean.getVmName());
        map.put("jvm.version", runtimeMXBean.getVmVersion());
        map.put("jvm.vendor", runtimeMXBean.getVmVendor());
        map.put("jvm.startTime", ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(runtimeMXBean.getStartTime()), java.time.ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        map.put("jvm.uptime", runtimeMXBean.getUptime() + " ms");

        // JVM Memory Information
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        // Memory information
        map.put("memory.heap.init", String.valueOf(heapMemoryUsage.getInit()));
        map.put("memory.heap.used", String.valueOf(heapMemoryUsage.getUsed()));
        map.put("memory.heap.committed", String.valueOf(heapMemoryUsage.getCommitted()));
        map.put("memory.heap.max", String.valueOf(heapMemoryUsage.getMax() < 0 ? 0 : heapMemoryUsage.getMax()));
        map.put("memory.nonHeap.init", String.valueOf(nonHeapMemoryUsage.getInit()));
        map.put("memory.nonHeap.used", String.valueOf(nonHeapMemoryUsage.getUsed()));
        map.put("memory.nonHeap.committed", String.valueOf(nonHeapMemoryUsage.getCommitted()));
        map.put("memory.nonHeap.max", String.valueOf(nonHeapMemoryUsage.getMax() < 0 ? 0 : nonHeapMemoryUsage.getMax()));

        // Also store human-readable values for reference
        map.put("memory.heap.init.human", formatBytes(heapMemoryUsage.getInit()));
        map.put("memory.heap.used.human", formatBytes(heapMemoryUsage.getUsed()));
        map.put("memory.heap.committed.human", formatBytes(heapMemoryUsage.getCommitted()));
        map.put("memory.heap.max.human", formatBytes(heapMemoryUsage.getMax()));

        map.put("memory.nonHeap.init.human", formatBytes(nonHeapMemoryUsage.getInit()));
        map.put("memory.nonHeap.used.human", formatBytes(nonHeapMemoryUsage.getUsed()));
        map.put("memory.nonHeap.committed.human", formatBytes(nonHeapMemoryUsage.getCommitted()));
        map.put("memory.nonHeap.max.human", formatBytes(nonHeapMemoryUsage.getMax()));

        // Thread Information
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        map.put("threads.count", String.valueOf(threadMXBean.getThreadCount()));
        map.put("threads.daemonCount", String.valueOf(threadMXBean.getDaemonThreadCount()));
        map.put("threads.peakCount", String.valueOf(threadMXBean.getPeakThreadCount()));
        map.put("threads.totalStarted", String.valueOf(threadMXBean.getTotalStartedThreadCount()));

        // Class Loading Information
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        map.put("classLoading.loadedClassCount", String.valueOf(classLoadingMXBean.getLoadedClassCount()));
        map.put("classLoading.totalLoadedClassCount", String.valueOf(classLoadingMXBean.getTotalLoadedClassCount()));
        map.put("classLoading.unloadedClassCount", String.valueOf(classLoadingMXBean.getUnloadedClassCount()));

        // Operating System Information
        OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
        map.put("os.name", osMXBean.getName());
        map.put("os.arch", osMXBean.getArch());
        map.put("os.version", osMXBean.getVersion());
        map.put("os.availableProcessors", String.valueOf(osMXBean.getAvailableProcessors()));

        // Try to get more detailed OS information using com.sun.management
        try {
            com.sun.management.OperatingSystemMXBean sunOsMXBean =
                    (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            map.put("os.systemLoadAverage", String.valueOf(sunOsMXBean.getSystemLoadAverage()));
            map.put("os.processCpuLoad", String.valueOf(sunOsMXBean.getProcessCpuLoad()));
            map.put("os.systemCpuLoad", String.valueOf(sunOsMXBean.getCpuLoad()));

            // OS memory information
            map.put("os.totalPhysicalMemory", String.valueOf(sunOsMXBean.getTotalMemorySize()));
            map.put("os.freePhysicalMemory", String.valueOf(sunOsMXBean.getFreeMemorySize()));
            map.put("os.totalSwapSpace", String.valueOf(sunOsMXBean.getTotalSwapSpaceSize()));
            map.put("os.freeSwapSpace", String.valueOf(sunOsMXBean.getFreeSwapSpaceSize()));
            map.put("os.committedVirtualMemory", String.valueOf(sunOsMXBean.getCommittedVirtualMemorySize()));

            // Also store human-readable values for reference
            map.put("os.totalPhysicalMemory.human", formatBytes(sunOsMXBean.getTotalMemorySize()));
            map.put("os.freePhysicalMemory.human", formatBytes(sunOsMXBean.getFreeMemorySize()));
            map.put("os.totalSwapSpace.human", formatBytes(sunOsMXBean.getTotalSwapSpaceSize()));
            map.put("os.freeSwapSpace.human", formatBytes(sunOsMXBean.getFreeSwapSpaceSize()));
            map.put("os.committedVirtualMemory.human", formatBytes(sunOsMXBean.getCommittedVirtualMemorySize()));
        } catch (Exception e) {
            map.put("os.extendedInfo.error", e.getMessage());
        }

        // System Properties
        Properties systemProperties = System.getProperties();
        systemProperties.forEach((key, value) -> {
            String keyStr = String.valueOf(key);
            String valueStr = String.valueOf(value);
            String fullKey = "system.property." + keyStr;
            map.put(fullKey, valueStr);
        });

        // Environment Variables
        Map<String, String> envVars = System.getenv();
        envVars.forEach((key, value) -> {
            String fullKey = "env." + key;
            map.put(fullKey, value);
        });

        return map;
    }

    private String formatBytes(long bytes) {
        if (bytes < 0) {
            return "undefined";
        }

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double value = bytes;

        while (value > 1024 && unitIndex < units.length - 1) {
            value /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", value, units[unitIndex]);
    }

}
