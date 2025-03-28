package com.openMRS.audit.security;

public class AuditContext {
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    public static void setUsername(String username) {
        currentUser.set(username);
    }

    public static String getUsername() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}
