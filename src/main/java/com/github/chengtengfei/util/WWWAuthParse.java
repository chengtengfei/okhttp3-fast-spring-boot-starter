package com.github.chengtengfei.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class WWWAuthParse {

    public static String authType(String wwwAuthenticate) {
        String authType = "";
        if (wwwAuthenticate == null || wwwAuthenticate.length() <= 0) {
            return authType;
        }
        StringTokenizer tokenizer = new StringTokenizer(wwwAuthenticate.trim());
        while (tokenizer.hasMoreTokens()) {
            authType = tokenizer.nextToken();
            break;
        }
        return authType;
    }

    /**
     * 解析WWW-Authenticate的值,如
     * WWW-Authenticate: Digest realm="testrealm@host.com",
     * qop="auth",
     * nonce="dcd98b7102dd2f0e8b11d0f600bfb0c093",
     * opaque="5ccc069c403ebaf9f0171e9517f40e41"
     * 解析为Map <realm, testrealm@host.com>
     *          <qop, auth>
     *          <nonce, dcd98b7102dd2f0e8b11d0f600bfb0c093>
     *          <opaque, 5ccc069c403ebaf9f0171e9517f40e41>
     * @param headerValue
     * @return
     */
    public static Map<String, String> parseDigestAuthenticateHeader(String headerValue) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        if (headerValue == null || headerValue.length() <= 0) {
            return resultMap;
        }

        try {
            headerValue = headerValue.trim();
            headerValue = headerValue.replaceFirst("Digest", "").trim();
            while (headerValue.length() > 0) {
                String key = headerValue.substring(0, headerValue.indexOf("="));
                headerValue = headerValue.substring(headerValue.indexOf("=") + 1);
                String value = "";
                if (headerValue.indexOf("\",") > 0) {
                    value = headerValue.substring(1, headerValue.indexOf("\","));
                    resultMap.put(key, value);
                    headerValue = headerValue.substring(headerValue.indexOf("\",") + 2).trim();
                } else {
                    value = headerValue.substring(1, headerValue.lastIndexOf("\""));
                    resultMap.put(key, value);
                    break;
                }
            }
        } catch (Exception e) {
            throw new Exception("解析[" + headerValue + "]出错");
        }

        if (resultMap.get("realm") == null) {
            resultMap.put("realm", "");
        }
        if (resultMap.get("qop") == null) {
            resultMap.put("qop", "");
        }
        if (resultMap.get("nonce") == null) {
            resultMap.put("nonce", "");
        }
        return resultMap;
    }

    public static String assembleDigestAuthorization(Map<String, String> digestMap) {
        String nc = "0000001";
        String cnonce = UUIDUtils.getUUID().toLowerCase().substring(0, 8);
        // qop为auth时生成response过程
        // String a1 = MD5("user:realm:password").toLowerCase();
        // String a2 = MD5("method:uri").toLowerCase();
        // String response = MD5(a1:nonce:nc:cnonce:qop:a2).toLowerCase();
        String h1 = MD5Utils.MD5(digestMap.get("username") + ":" + digestMap.get("realm") + ":" + digestMap.get("password")).toLowerCase();
        String a2 = "";
        String qop = digestMap.get("qop");
        if (!StringUtils.isEmpty(qop)) {
            if ("auth".equalsIgnoreCase(qop)) {
                a2 = MD5Utils.MD5(digestMap.get("method").toUpperCase() + ":" + digestMap.get("uri")).toLowerCase();
            } else if ("auth-int".equalsIgnoreCase(qop)) {
                a2 = MD5Utils.MD5(digestMap.get("method").toUpperCase() + ":" + digestMap.get("uri") + ":" + MD5Utils.MD5(digestMap.get("body"))).toLowerCase();
            }
        } else {

        }

        String response = MD5Utils.MD5(a1 + ":" + authResponseMap.get("nonce") + ":" + nc + ":" + cnonce + ":"
                + authResponseMap.get("qop") + ":" + a2).toLowerCase();
        String authorization = "Digest username=\"" + digestMap.get("username") + "\", realm=\"" + digestMap.get("realm")
                + "\", nonce=\"" + digestMap.get("nonce") + "\", uri=" + digestMap.get("uri") + ", qop=" + digestMap.get("qop")
                + ", nc=" + nc + ", cnonce=\"" + cnonce + "\", response=\"" + response + "\"";
    }
}
