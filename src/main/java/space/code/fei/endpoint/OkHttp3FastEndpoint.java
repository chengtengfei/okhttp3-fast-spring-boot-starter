package space.code.fei.endpoint;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import space.code.fei.constant.MetricNameConstant;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Endpoint(id = "okhttp3fast")
public class OkHttp3FastEndpoint {

    private MetricRegistry registry;

    public OkHttp3FastEndpoint(MetricRegistry registry) {
        this.registry = registry;
    }

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
        map.put(name + ".count", timer.getCount());
        map.put(name + ".oneMinuteRate", timer.getOneMinuteRate());
        map.put(name + ".fiveMinuteRate", timer.getFiveMinuteRate());
        map.put(name + ".fifteenMinuteRate", timer.getFifteenMinuteRate());
        map.put(name + ".meanRate", timer.getMeanRate());
        Snapshot snapshot = timer.getSnapshot();
        map.put(name + ".snapshot.mean", snapshot.getMean());
        map.put(name + ".snapshot.max", snapshot.getMax());
        map.put(name + ".snapshot.min", snapshot.getMin());
        map.put(name + ".snapshot.median", snapshot.getMedian());
        map.put(name + ".snapshot.stdDev", snapshot.getStdDev());
        map.put(name + ".snapshot.75thPercentile", snapshot.get75thPercentile());
        map.put(name + ".snapshot.95thPercentile", snapshot.get95thPercentile());
        map.put(name + ".snapshot.98thPercentile", snapshot.get98thPercentile());
        map.put(name + ".snapshot.99thPercentile", snapshot.get99thPercentile());
        map.put(name + ".snapshot.999thPercentile", snapshot.get999thPercentile());
        return map;
    }
}
