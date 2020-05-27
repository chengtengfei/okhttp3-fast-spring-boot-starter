package space.code.fei.bean;

import okhttp3.*;
import space.code.fei.constant.RawMediaType;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class OkHttp3Fast {

    private OkHttpClient okHttpClient;

    public OkHttp3Fast(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    private static final MediaType X_WWW_FROM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded");

    /**************************************************************************************/
    /*********************************GET METHOD******************************************/
    /*************************************************************************************/

    public Response get(String url) throws IOException {
        return get(url, null, null);
    }

    public Response get(String url, Map<String, Object> paramMap) throws IOException {
        return get(url, paramMap, null);
    }

    public Response get(String url, Headers headers) throws IOException {
        return get(url, null, headers);
    }

    public Response get(String url, Map<String, Object> paramMap, Headers headers) throws IOException {
        if (paramMap != null) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("?");
            for(Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    urlBuilder.append(entry.getKey())
                            .append("=")
                            .append(entry.getValue())
                            .append("&");
                }
            }
            url = url + urlBuilder.toString().substring(0, urlBuilder.length() -1);
        }

        url = URLEncoder.encode(url, StandardCharsets.UTF_8.name());

        Request request = null;
        if (headers != null) {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .get()
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
        }


        return okHttpClient.newCall(request).execute();

    }

    /**************************************************************************************/
    /*********************************POST METHOD******************************************/
    /*************************************************************************************/


    public Response postRaw(String url, RawMediaType mediaType) throws IOException {
        return postRaw(url, null, null, mediaType);
    }

    public Response postRaw(String url, Headers headers, RawMediaType mediaType) throws IOException {

        return postRaw(url, null, headers, mediaType);
    }

    public Response postRaw(String url, String body, RawMediaType mediaType) throws IOException {
        return postRaw(url, body, null, mediaType);
    }

    public Response postRaw(String url, String body, Headers headers, RawMediaType mediaType) throws IOException {

        if (mediaType == null) {
            mediaType = RawMediaType.TEXT_PLAIN;
        }

        Request request = null;
        if (headers == null && body == null) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else if (headers == null && body != null) {
            request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType.getName(), body))
                    .build();
        } else if (headers != null && body == null) {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(RequestBody.create(mediaType.getName(), body))
                    .build();
        }

        return okHttpClient.newCall(request).execute();

        // if (response.isSuccessful()) {
        //     return response.body() == null ? "" : response.body().string();
        // } else {
        //     throw new Exception("response is failure, response code : " + response.code() + ", response body : " + (response.body() == null ? "null" : response.body().string()));
        // }
    }


    public Response postFormEncode(String url) throws Exception {
        return postFormEncode(url, null, null);
    }

    public Response postFormEncode(String url, Map<String, Object> body) throws Exception {
        return postFormEncode(url, body, null);
    }

    public Response postFormEncode(String url, Map<String, Object> body, Headers headers) throws IOException {

        StringBuilder dataBuilder = new StringBuilder();
        String content = "";

        if (body != null) {
            for(Map.Entry<String, Object> entry : body.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    dataBuilder.append(entry.getKey())
                            .append("=")
                            .append(entry.getValue())
                            .append("&");
                }
            }
            content = dataBuilder.toString();
            content = content.substring(0, content.length() - 1);
            content = URLEncoder.encode(content, StandardCharsets.UTF_8.name());
        }

        Request request = null;

        if (headers != null) {
            request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(X_WWW_FROM_URLENCODED, content))
                    .headers(headers)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(X_WWW_FROM_URLENCODED, content))
                    .build();
        }

        return okHttpClient.newCall(request).execute();
    }

    public Response postMultipart(String url, MultipartBody body) throws IOException {
        return postMultipart(url, body, null);
    }

    public Response postMultipart(String url, MultipartBody body, Headers headers) throws IOException {

        if (body == null) {
            throw new IOException("MultipartBody must be not null");
        }

        Request request = null;
        if (headers != null) {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }

        return okHttpClient.newCall(request).execute();
    }

}
