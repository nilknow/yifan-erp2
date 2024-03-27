package com.nilknow.yifanerp2.config.security;

public class TenantContextHolder {
    private static final ThreadLocal<Long> companyIdHolder = new ThreadLocal<>();

    public static void set(Long companyId){
        companyIdHolder.remove();
        companyIdHolder.set(companyId);
    }

    public static Long get() {
        return companyIdHolder.get();
    }
}
