package com.github.chengtengfei.endpoint;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import com.github.chengtengfei.constant.MetricNameConstant;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Endpoint(id = "okhttp3fast")
public class OkHttp3FastEndpoint {

    private MetricRegistry registry;

    public OkHttp3FastEndpoint(MetricRegistry registry) {
        this.registry = registry;
    }

    private final DecimalFormat decimalFormat =new DecimalFormat("#.00");

    @ReadOperation
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("author", "tengfei.cheng@foxmail.com");
        info.put("okhttp3-fast", "https://github.com/chengtengfei/okhttp3-fast-spring-boot-starter");
        info.put("metrics", getMetrics());
        return info;
    }

    private Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        // timer
        SortedMap<String, com.codahale.metrics.Timer> timers = registry.getTimers((name, metric) -> name.startsWith(MetricNameConstant.METRIC_NAME));
        for (Map.Entry<String, Timer> entry : timers.entrySet()) {
            metrics.putAll(convertTimerToMap(entry.getKey(), entry.getValue()));
        }
        return metrics;
    }

    private Map<String, Object> convertTimerToMap(String name, Timer timer) {
        Map<String, Object> map = new HashMap<>();
        // 总请求数
        map.put(name + ".count", timer.getCount());
        // 最近1份钟平均请求数
        map.put(name + ".oneMinuteRate", formatDoubleWith2Point(timer.getOneMinuteRate()));
        // 最近5分钟平均请求数
        map.put(name + ".fiveMinuteRate", formatDoubleWith2Point(timer.getFiveMinuteRate()));
        // 最近15分钟平均请求数
        map.put(name + ".fifteenMinuteRate", formatDoubleWith2Point(timer.getFifteenMinuteRate()));
        // 总平均请求书
        map.put(name + ".meanRate", formatDoubleWith2Point(timer.getMeanRate()));
        // Snapshot snapshot = timer.getSnapshot();
        // map.put(name + ".snapshot.mean", formatDoubleWith2Point(snapshot.getMean()));
        // map.put(name + ".snapshot.max", snapshot.getMax());
        // map.put(name + ".snapshot.min", snapshot.getMin());
        // map.put(name + ".snapshot.median", formatDoubleWith2Point(snapshot.getMedian()));
        // map.put(name + ".snapshot.stdDev", formatDoubleWith2Point(snapshot.getStdDev()));
        // map.put(name + ".snapshot.75thPercentile", formatDoubleWith2Point(snapshot.get75thPercentile()));
        // map.put(name + ".snapshot.95thPercentile", formatDoubleWith2Point(snapshot.get95thPercentile()));
        // map.put(name + ".snapshot.98thPercentile", formatDoubleWith2Point(snapshot.get98thPercentile()));
        // map.put(name + ".snapshot.99thPercentile", formatDoubleWith2Point(snapshot.get99thPercentile()));
        // map.put(name + ".snapshot.999thPercentile", formatDoubleWith2Point(snapshot.get999thPercentile()));
        return map;
    }

    private String formatDoubleWith2Point(double value) {
        if (value <= 0) {
            return "0";
        }
        return decimalFormat.format(value);
    }
}
