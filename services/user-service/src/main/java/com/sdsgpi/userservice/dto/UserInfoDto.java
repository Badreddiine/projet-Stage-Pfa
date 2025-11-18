package com.sdsgpi.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO simplifié pour les informations utilisateur à transmettre aux autres microservices
 * Contient les informations essentielles pour la gestion des droits d'accès
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {

    private String userId; // keycloakId
    private String username;
    private String email;
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
        if (roles == null || roleNames == null) {
            return false;
        }
        return roleNames.stream().anyMatch(roles::contains);
    }

    /**
     * Vérifie si l'utilisateur a au moins une des permissions spécifiées
     */
    public boolean hasAnyPermission(List<String> permissionNames) {
        if (permissions == null || permissionNames == null) {
            return false;
        }
        return permissionNames.stream().anyMatch(permissions::contains);
    }
}

