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

        String a1 = digestMap.get("username") + ":" + digestMap.get("realm") + ":" + digestMap.get("password");
        String ha1 = MD5Utils.MD5(a1).toLowerCase();
        String a2 = digestMap.get("method").toUpperCase() + ":" + digestMap.get("uri");
        String ha2 = "";
        String qop = digestMap.get("qop");
        String response = "";
        if (!StringUtils.isEmpty(qop)) {
            if ("auth".equalsIgnoreCase(qop)) {
                ha2 = MD5Utils.MD5(a2).toLowerCase();
                response =  MD5Utils.MD5(ha1 + ":" + digestMap.get("nonce") + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2).toLowerCase();
            } else if ("auth-int".equalsIgnoreCase(qop)) {
                ha2 = MD5Utils.MD5(digestMap.get("method").toUpperCase() + ":" + digestMap.get("uri") + ":" + MD5Utils.MD5(digestMap.get("body"))).toLowerCase();
                response = MD5Utils.MD5(ha1 + ":" + digestMap.get("nonce") + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2).toLowerCase();
            } else if (isAuthAndAuthInt(qop)) {
                // qop为auth,auth-int是，客户端可以任选一个值，这里默认选择auth
                ha2 = MD5Utils.MD5(a2).toLowerCase();
                response =  MD5Utils.MD5(ha1 + ":" + digestMap.get("nonce") + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2).toLowerCase();
            } else {
                // qop的值不属于auth 或者 auth-int
                return "";
            }
        } else {
            // qop未指定
            ha2 = MD5Utils.MD5(a2).toLowerCase();
            response = MD5Utils.MD5(ha1 + ":" + digestMap.get("nonce") + ":" + ha2);
        }

        String authorization = "Digest username=\"" + digestMap.get("username") + "\", realm=\"" + digestMap.get("realm")
                + "\", nonce=\"" + digestMap.get("nonce") + "\", uri=" + digestMap.get("uri") + ", qop=" + digestMap.get("qop")
                + ", nc=" + nc + ", cnonce=\"" + cnonce + "\", response=\"" + response + "\"";
        return authorization;
    }

    private static boolean isAuthAndAuthInt(String qop) {
        if (StringUtils.isEmpty(qop)) {
            return false;
        }
        Map<String, Boolean> authMap = new HashMap<>();
        authMap.put("auth", false);
        authMap.put("auth-int", false);

        String[] auth = qop.split(",");
        for (String authType : auth) {
            if ("auth".equalsIgnoreCase(authType)) {
                authMap.put("auth", true);
            }
            if ("auth-int".equalsIgnoreCase(authType)) {
                authMap.put("auth-int", true);
            }
        }

        if (authMap.get("auth") && authMap.get("auth-int")) {
            return true;
        }
        return false;
    }
}
