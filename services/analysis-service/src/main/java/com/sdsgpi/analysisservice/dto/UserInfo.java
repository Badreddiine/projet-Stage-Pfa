package com.sdsgpi.analysisservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour les informations utilisateur extraites du token JWT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String department;
    private String position;
    private Boolean isActive;
    private String preferredLanguage;
    private List<String> roles;
    private List<String> permissions;

    /**
     * Vérifie si l'utilisateur a un rôle spécifique
     */
    public boolean hasRole(String roleName) {
        return roles != null && roles.contains(roleName);
    }

    /**
     * Vérifie si l'utilisateur a une permission spécifique
     */
    public boolean hasPermission(String permissionName) {
        return permissions != null && permissions.contains(permissionName);
    }

    /**
     * Vérifie si l'utilisateur a au moins un des rôles spécifiés
     */
    public boolean hasAnyRole(List<String> roleNames) {
        if (roles == null || roleNames == null) return false;
        return roleNames.stream().anyMatch(roles::contains);
    }

    /**
     * Récupère le nom complet de l'utilisateur
     */
    public String getFullName() {
        if (fullName != null && !fullName.isEmpty()) {
            return fullName;
        }
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }
}

