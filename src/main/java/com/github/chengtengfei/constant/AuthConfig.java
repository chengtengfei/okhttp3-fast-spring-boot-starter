package com.github.chengtengfei.constant;

final public class AuthConfig {

    private static boolean status = true;
    private static String basicUsername;
    private static String basicPassword;
    private static String digestUsername;
    private static String digestPassword;

    public static boolean isStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        AuthConfig.status = status;
    }

    public static String getBasicUsername() {
        return basicUsername;
    }

    public static void setBasicUsername(String basicUsername) {
        AuthConfig.basicUsername = basicUsername;
    }

    public static String getBasicPassword() {
        return basicPassword;
    }

    public static void setBasicPassword(String basicPassword) {
        AuthConfig.basicPassword = basicPassword;
    }

    public static String getDigestUsername() {
        return digestUsername;
    }

    public static void setDigestUsername(String digestUsername) {
        AuthConfig.digestUsername = digestUsername;
    }

    public static String getDigestPassword() {
        return digestPassword;
    }

    public static void setDigestPassword(String digestPassword) {
        AuthConfig.digestPassword = digestPassword;
    }
}
