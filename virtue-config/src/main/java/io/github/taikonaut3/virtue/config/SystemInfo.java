package io.github.taikonaut3.virtue.config;

import com.sun.management.OperatingSystemMXBean;
import io.github.taikonaut3.virtue.config.manager.RemoteServiceManager;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;

@Accessors(fluent = true)
@Data
public class SystemInfo {

    private double cpuUsage;

    private double memoryUsage;

    private long connections;

    private int services;

    private double loadAverage;

    public SystemInfo() {

    }

    public SystemInfo(Virtue virtue) {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        // 获取服务器 CPU 使用率
        cpuUsage = Double.parseDouble(decimalFormat.format(osBean.getCpuLoad()));
        loadAverage = Double.parseDouble(decimalFormat.format(osBean.getSystemLoadAverage()));
        // 获取服务器内存使用情况
        long totalMemory = osBean.getTotalMemorySize();
        long freeMemory = osBean.getFreeMemorySize();
        long usedMemory = totalMemory - freeMemory;
        memoryUsage = Double.parseDouble(decimalFormat.format((double) usedMemory / totalMemory));
        RemoteServiceManager remoteServiceManager = virtue.configManager().remoteServiceManager();
        Collection<RemoteService<?>> remoteServices = remoteServiceManager.remoteServices();
        services = remoteServices.stream()
                .mapToInt(remoteService -> remoteService.callers().length)
                .sum();
    }

    public static SystemInfo valueOf(Map<String, String> map) {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.cpuUsage(Double.parseDouble(map.get("cpuUsage")));
        systemInfo.memoryUsage(Double.parseDouble(map.get("memoryUsage")));
        systemInfo.connections(Long.parseLong(map.get("connections")));
        systemInfo.services(Integer.parseInt(map.get("services")));
        systemInfo.loadAverage(Double.parseDouble(map.get("loadAverage")));
        return systemInfo;
    }

    public Map<String, String> toMap() {
        return Map.of("cpuUsage", String.valueOf(cpuUsage)
                , "memoryUsage", String.valueOf(memoryUsage)
                , "connections", String.valueOf(connections)
                , "services", String.valueOf(services)
                , "loadAverage", String.valueOf(loadAverage));
    }
}
