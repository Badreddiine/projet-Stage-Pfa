package com.sdsgpi.analysisservice.security;

import com.sdsgpi.analysisservice.dto.UserInfo;

/**
 * Holder pour stocker les informations de l'utilisateur courant dans le contexte de la requête
 */
public class UserContextHolder {
    
    private static final ThreadLocal<UserInfo> userContext = new ThreadLocal<>();

    /**
     * Définit les informations de l'utilisateur courant
     */
    public static void setUserInfo(UserInfo userInfo) {
        userContext.set(userInfo);
    }

    /**
     * Récupère les informations de l'utilisateur courant
     */
    public static UserInfo getUserInfo() {
        return userContext.get();
    }

    /**
     * Récupère l'ID de l'utilisateur courant
     */
    public static String getCurrentUserId() {
        UserInfo userInfo = userContext.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    /**
     * Récupère le nom d'utilisateur courant
     */
    public static String getCurrentUsername() {
        UserInfo userInfo = userContext.get();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    /**
     * Vérifie si l'utilisateur courant a un rôle spécifique
     */
    public static boolean hasRole(String roleName) {
        UserInfo userInfo = userContext.get();
        return userInfo != null && userInfo.hasRole(roleName);
    }

    /**
     * Vérifie si l'utilisateur courant a une permission spécifique
     */
    public static boolean hasPermission(String permissionName) {
        UserInfo userInfo = userContext.get();
        return userInfo != null && userInfo.hasPermission(permissionName);
    }

    /**
     * Nettoie le contexte utilisateur (important pour éviter les fuites de mémoire)
     */
    public static void clear() {
        userContext.remove();
    }
}

