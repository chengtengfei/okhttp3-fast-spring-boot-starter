package space.code.fei.bean;

import okhttp3.*;

public class OkHttp3Fast {

    private OkHttpClient okHttpClient;

    private static final MediaType APPLICATION_JSON = MediaType.parse("application/json");
    private static final MediaType X_WWW_FROM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded");

    public OkHttp3Fast(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String post(String url, String body) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(APPLICATION_JSON, body))
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body() == null ? "" : response.body().string();
        } else {
            throw new Exception("response is failure, response code : " + response.code() + ", response body : " + (response.body() == null ? "null" : response.body().string()));
        }
    }

}
