package com.github.chengtengfei.interceptor;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import com.github.chengtengfei.constant.MetricNameConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * okhttp3 请求指标度量拦截器
 */
public class MetricsInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsInterceptor.class);

    private MetricRegistry registry;

    public MetricsInterceptor(MetricRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = request.url().host();
        Response response;
        Timer timer = registry.timer(MetricRegistry.name(MetricNameConstant.METRIC_NAME, host, request.method()));
        Timer.Context context = timer.time();
        try {
            response = chain.proceed(request);
        } finally {
            context.stop();
        }
        return response;
    }
}
