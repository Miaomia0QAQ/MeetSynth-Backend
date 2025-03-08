package com.gl.meetsynthbackend.utils;

public class CurrentHolder {
    private static final ThreadLocal<String> CURRENT_LOCAL = new ThreadLocal<>();

    public static void setCurrentId(String uid) {
        CURRENT_LOCAL.set(uid);
    }

    public static String getCurrentId() {
        return CURRENT_LOCAL.get();
    }

    public static void removeCurrentId() {
        CURRENT_LOCAL.remove();
    }
}
