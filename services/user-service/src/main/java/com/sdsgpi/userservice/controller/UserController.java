package com.sdsgpi.userservice.controller;

import com.sdsgpi.userservice.dto.*;
import com.sdsgpi.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "API pour la gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Récupérer les informations de l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informations utilisateur récupérées avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getCurrentUserInfo(
            @Parameter(description = "ID Keycloak de l'utilisateur (extrait du token JWT)")
            @RequestHeader("X-User-Id") String keycloakId) {
        
        log.info("Récupération des informations pour l'utilisateur: {}", keycloakId);
        UserInfoDto userInfo = userService.getUserInfo(keycloakId);
        return ResponseEntity.ok(userInfo);
    }

    @Operation(summary = "Récupérer un utilisateur par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/{keycloakId}")
    @PreAuthorize("hasRole('ADMIN') or hasPermission('USER', 'READ')")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID Keycloak de l'utilisateur")
            @PathVariable String keycloakId) {
        
        log.info("Récupération de l'utilisateur avec l'ID: {}", keycloakId);
        UserDto user = userService.getUserByKeycloakId(keycloakId);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Récupérer tous les utilisateurs actifs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasPermission('USER', 'READ')")
    public ResponseEntity<List<UserDto>> getAllActiveUsers() {
        log.info("Récupération de tous les utilisateurs actifs");
        List<UserDto> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Rechercher des utilisateurs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasPermission('USER', 'READ')")
    public ResponseEntity<List<UserDto>> searchUsers(
            @Parameter(description = "Terme de recherche")
            @RequestParam String searchTerm) {
        
        log.info("Recherche d'utilisateurs avec le terme: {}", searchTerm);
        List<UserDto> users = userService.searchUsers(searchTerm);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Créer un nouvel utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Utilisateur déjà existant"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasPermission('USER', 'WRITE')")
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "Données de création de l'utilisateur")
            @Valid @RequestBody CreateUserRequest request) {
        
        log.info("Création d'un nouvel utilisateur: {}", request.getUsername());
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Mettre à jour un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PutMapping("/{keycloakId}")
    @PreAuthorize("hasRole('ADMIN') or hasPermission('USER', 'WRITE') or #keycloakId == authentication.name")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID Keycloak de l'utilisateur")
            @PathVariable String keycloakId,
            @Parameter(description = "Données de mise à jour de l'utilisateur")
            @Valid @RequestBody UpdateUserRequest request) {
        
        log.info("Mise à jour de l'utilisateur: {}", keycloakId);
        UserDto updatedUser = userService.updateUser(keycloakId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Désactiver un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Utilisateur désactivé avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @DeleteMapping("/{keycloakId}")
    @PreAuthorize("hasRole('ADMIN') or hasPermission('USER', 'DELETE')")
    public ResponseEntity<Void> deactivateUser(
            @Parameter(description = "ID Keycloak de l'utilisateur")
            @PathVariable String keycloakId) {
        
        log.info("Désactivation de l'utilisateur: {}", keycloakId);
        userService.deactivateUser(keycloakId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activer un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Utilisateur activé avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PatchMapping("/{keycloakId}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasPermission('USER', 'WRITE')")
    public ResponseEntity<Void> activateUser(
            @Parameter(description = "ID Keycloak de l'utilisateur")
            @PathVariable String keycloakId) {
        
        log.info("Activation de l'utilisateur: {}", keycloakId);
        userService.activateUser(keycloakId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Synchroniser un utilisateur avec Keycloak")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur synchronisé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/sync")
    public ResponseEntity<UserDto> syncUserFromKeycloak(
            @Parameter(description = "Données de synchronisation depuis Keycloak")
            @RequestBody SyncUserRequest request) {
        
        log.info("Synchronisation de l'utilisateur depuis Keycloak: {}", request.getUsername());
        UserDto syncedUser = userService.syncUserFromKeycloak(
                request.getKeycloakId(),
                request.getUsername(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getRoles()
        );
        return ResponseEntity.ok(syncedUser);
    }
}

