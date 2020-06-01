package com.github.chengtengfei.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URIUtils {

    public static String urlEncode(String some) {
        String result = some;
        try {
            result = URLEncoder.encode(some, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String urlDecode(String some) {
        String result = some;
        try {
            result = URLDecoder.decode(some, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
