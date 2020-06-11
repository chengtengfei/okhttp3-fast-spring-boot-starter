package com.github.chengtengfei.configure;

import com.github.chengtengfei.constant.AuthConfig;
import com.github.chengtengfei.util.WWWAuthParse;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class AuthAuthenticator implements Authenticator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthAuthenticator.class);
    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (AuthConfig.isStatus()) {
            String responseAuth = response.header("WWW-Authenticate");
            if (responseAuth == null || responseAuth.length() <= 0) {
                return null;
            }
            String authType = WWWAuthParse.authType(responseAuth);
            if ("Basic".equalsIgnoreCase(authType)) {
                if (!StringUtils.isEmpty(AuthConfig.getBasicUsername()) && !StringUtils.isEmpty(AuthConfig.getBasicPassword())) {
                    String basicAuth = Credentials.basic(AuthConfig.getBasicUsername(), AuthConfig.getBasicPassword());
                    return response.request().newBuilder().addHeader("Authorization", basicAuth).build();
                }
                return null;
            } else if ("Digest".equalsIgnoreCase(authType)) {
                if (!StringUtils.isEmpty(AuthConfig.getDigestUsername()) && !StringUtils.isEmpty(AuthConfig.getDigestPassword())) {
                    Map<String, String> digestAuthMap = new HashMap<>();
                    try {
                        digestAuthMap = WWWAuthParse.parseDigestAuthenticateHeader(responseAuth);
                    } catch (Exception e) {
                        LOGGER.error("处理Digest认证,解析Response Header WWW-Authenticate [{}] 出错", responseAuth);
                        return null;
                    }
                    digestAuthMap.put("username", AuthConfig.getDigestUsername());
                    digestAuthMap.put("password", AuthConfig.getDigestPassword());
                    digestAuthMap.put("uri", new URL(response.request().url().toString()).getPath());
                    digestAuthMap.put("method", response.request().method());
                    if (response.request().body() != null) {
                        Buffer buffer = new Buffer();
                        response.request().body().writeTo(buffer);
                        Charset charset = UTF8;
                        MediaType contentType = response.request().body().contentType();
                        if (contentType != null) {
                            charset = contentType.charset(UTF8);
                        }
                        String requestBody = buffer.readString(charset == null? UTF8 : charset);
                        digestAuthMap.put("body", requestBody);
                    } else {
                        digestAuthMap.put("body", "");
                    }

                    String authorization = WWWAuthParse.assembleDigestAuthorization(digestAuthMap);
                    if (!StringUtils.isEmpty(authorization)) {
                        return response.request().newBuilder().addHeader("Authorization", authorization).build();
                    }
                }
                return null;
            } else {
                return null;
            }
        }
        return null;
    }
}
