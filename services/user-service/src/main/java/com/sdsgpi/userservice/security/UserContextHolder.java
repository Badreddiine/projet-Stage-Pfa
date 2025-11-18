package com.sdsgpi.userservice.security;

import com.sdsgpi.userservice.dto.UserInfoDto;

/**
 * Holder pour stocker les informations de l'utilisateur connecté dans un ThreadLocal
 * Utilisé pour partager les informations utilisateur entre les différentes couches de l'application
 */
public class UserContextHolder {

    private static final ThreadLocal<UserInfoDto> userContext = new ThreadLocal<>();

    /**
     * Définit les informations de l'utilisateur connecté
     */
    public static void setUserInfo(UserInfoDto userInfo) {
        userContext.set(userInfo);
    }

    /**
     * Récupère les informations de l'utilisateur connecté
     */
    public static UserInfoDto getUserInfo() {
        return userContext.get();
    }

    /**
     * Récupère l'ID de l'utilisateur connecté
     */
    public static String getCurrentUserId() {
        UserInfoDto userInfo = userContext.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    /**
     * Récupère le nom d'utilisateur de l'utilisateur connecté
     */
    public static String getCurrentUsername() {
        UserInfoDto userInfo = userContext.get();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    /**
     * Vérifie si l'utilisateur connecté a un rôle spécifique
     */
    public static boolean hasRole(String roleName) {
        UserInfoDto userInfo = userContext.get();
        return userInfo != null && userInfo.hasRole(roleName);
    }

    /**
     * Vérifie si l'utilisateur connecté a une permission spécifique
     */
    public static boolean hasPermission(String permissionName) {
        UserInfoDto userInfo = userContext.get();
        return userInfo != null && userInfo.hasPermission(permissionName);
    }

    /**
     * Nettoie le contexte utilisateur
     * IMPORTANT: Doit être appelé à la fin de chaque requête pour éviter les fuites de mémoire
     */
    public static void clear() {
        userContext.remove();
    }
}

