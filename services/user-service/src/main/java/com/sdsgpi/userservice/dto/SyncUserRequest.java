package com.sdsgpi.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour la synchronisation d'un utilisateur depuis Keycloak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncUserRequest {

    private String keycloakId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
}

