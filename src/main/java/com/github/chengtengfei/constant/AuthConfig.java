package com.github.chengtengfei.constant;

final public class AuthConfig {

    private static boolean status = false;
    private static String basicUsername;
    private static String basicPassword;
    private static String digestUsername;
    private static String digestPassword;

    public static boolean isStatus() {
        return status;
    }


    public static String getBasicUsername() {
        return basicUsername;
    }


    public static String getBasicPassword() {
        return basicPassword;
    }


    public static String getDigestUsername() {
        return digestUsername;
    }


    public static String getDigestPassword() {
        return digestPassword;
    }


    public static void on() {
        AuthConfig.status = true;
    }

    public static void off() {
        AuthConfig.status = false;
    }

    public static void basicAuth(String basicUsername, String basicPassword) {
        AuthConfig.basicUsername = basicUsername;
        AuthConfig.basicPassword = basicPassword;
    }

    public static void digestAuth(String digestUsername, String digestPassword) {
        AuthConfig.digestUsername = digestUsername;
        AuthConfig.digestPassword = digestPassword;
    }
}
