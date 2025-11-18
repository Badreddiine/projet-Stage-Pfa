package com.notification.security;


import com.notification.dto.UserInfo;

public class UserContextHolder {
    private static final ThreadLocal<UserInfo> userContext = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        userContext.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return userContext.get();
    }

    public static String getCurrentUserId() {
        UserInfo userInfo = userContext.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    public static String getCurrentUsername() {
        UserInfo userInfo = userContext.get();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    public static boolean hasRole(String roleName) {
        UserInfo userInfo = userContext.get();
        return userInfo != null && userInfo.hasRole(roleName);
    }

    public static boolean hasPermission(String permissionName) {
        UserInfo userInfo = userContext.get();
        return userInfo != null && userInfo.hasPermission(permissionName);
    }

    public static void clear() {
        userContext.remove();
    }
}
