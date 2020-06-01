package com.github.chengtengfei.util;

import java.nio.charset.Charset;
import java.util.Base64;

public class HttpAuths {

    public static String basicAuth(String username, String password) {
        if (username == null || password == null) {
            return "";
        }
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode((auth.getBytes(Charset.forName("UTF-8"))));
        return "Basic " + new String(encodedAuth);
    }
}
