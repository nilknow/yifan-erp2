package com.nilknow.yifanerp2.config.security;

public class UserIdHolder {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void set(Long companyId) {
        userIdHolder.remove();
        userIdHolder.set(companyId);
    }

    public static Long get() {
        return userIdHolder.get();
    }

}
