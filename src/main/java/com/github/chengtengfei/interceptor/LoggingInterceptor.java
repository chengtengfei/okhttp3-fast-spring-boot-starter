package com.github.chengtengfei.interceptor;

import com.github.chengtengfei.util.URIUtils;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 日志拦截器
 */
public class LoggingInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String sendBody = "";
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            sendBody = buffer.readString(charset == null? UTF8 : charset);
        }
        LOGGER.debug("\n request method : {} \n request url: {} \n request headers: {} \n request body: {}",
                request.method(), URIUtils.urlDecode(request.url().toString()), request.headers(), sendBody);


        long requestStartNanoTime = System.nanoTime();
        Response response = chain.proceed(request);
        long requestEndNanoTime = System.nanoTime();
        double usedMillsTime = (requestEndNanoTime - requestStartNanoTime) / Math.pow(10, 6);


        ResponseBody responseBody = response.body();
        String receiveBody = "";
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            receiveBody = buffer.clone().readString(charset == null? UTF8 : charset);
        }

        LOGGER.debug("\n response url: {} \n response time: {} millseconds \n response code: {} \n request headers: {} \n " +
                        "request body: {} \n response headers: {} \n response body: {}",
                URIUtils.urlDecode(response.request().url().toString()), String.format("%.2f", usedMillsTime),
                response.code(), request.headers(), sendBody, response.headers(), receiveBody);

        return response;
    }
}
