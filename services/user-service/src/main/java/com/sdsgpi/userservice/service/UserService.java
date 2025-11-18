package com.sdsgpi.userservice.service;

import com.sdsgpi.userservice.dto.*;
import com.sdsgpi.userservice.entity.*;
import com.sdsgpi.userservice.exception.UserNotFoundException;
import com.sdsgpi.userservice.exception.UserAlreadyExistsException;
import com.sdsgpi.userservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Value("${user-service.default-role:USER}")
    private String defaultRoleName;

    /**
     * Récupère un utilisateur par son ID Keycloak
     */
    @Transactional(readOnly = true)
    public UserDto getUserByKeycloakId(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + keycloakId));
        return convertToDto(user);
    }

    /**
     * Récupère les informations simplifiées d'un utilisateur pour les autres microservices
     */
    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + keycloakId));
        
        // Mettre à jour la dernière connexion
        user.updateLastLogin();
        userRepository.save(user);
        
        return convertToUserInfoDto(user);
    }

    /**
     * Crée un nouvel utilisateur
     */
    public UserDto createUser(CreateUserRequest request) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByKeycloakId(request.getKeycloakId())) {
            throw new UserAlreadyExistsException("Un utilisateur avec cet ID Keycloak existe déjà: " + request.getKeycloakId());
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Un utilisateur avec ce nom d'utilisateur existe déjà: " + request.getUsername());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Un utilisateur avec cet email existe déjà: " + request.getEmail());
        }

        // Créer l'utilisateur
        User user = User.builder()
                .keycloakId(request.getKeycloakId())
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .department(request.getDepartment())
                .position(request.getPosition())
                .preferredLanguage(request.getPreferredLanguage() != null ? request.getPreferredLanguage() : "fr")
                .profilePictureUrl(request.getProfilePictureUrl())
                .isActive(true)
                .build();

        user = userRepository.save(user);

        // Assigner les rôles
        assignRolesToUser(user, request.getRoleNames());

        log.info("Utilisateur créé avec succès: {}", user.getUsername());
        return convertToDto(user);
    }

    /**
     * Met à jour un utilisateur existant
     */
    public UserDto updateUser(String keycloakId, UpdateUserRequest request) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + keycloakId));

        // Mettre à jour les champs modifiables
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }
        if (request.getPosition() != null) {
            user.setPosition(request.getPosition());
        }
        if (request.getPreferredLanguage() != null) {
            user.setPreferredLanguage(request.getPreferredLanguage());
        }
        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        user = userRepository.save(user);
        log.info("Utilisateur mis à jour avec succès: {}", user.getUsername());
        return convertToDto(user);
    }

    /**
     * Synchronise un utilisateur avec les données de Keycloak
     */
    public UserDto syncUserFromKeycloak(String keycloakId, String username, String email, 
                                       String firstName, String lastName, List<String> keycloakRoles) {
        User user = userRepository.findByKeycloakId(keycloakId).orElse(null);
        
        if (user == null) {
            // Créer un nouvel utilisateur
            CreateUserRequest request = CreateUserRequest.builder()
                    .keycloakId(keycloakId)
                    .username(username)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .roleNames(keycloakRoles)
                    .build();
            return createUser(request);
        } else {
            // Mettre à jour l'utilisateur existant
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.updateLastLogin();
            
            user = userRepository.save(user);
            
            // Synchroniser les rôles
            syncUserRoles(user, keycloakRoles);
            
            log.info("Utilisateur synchronisé avec Keycloak: {}", user.getUsername());
            return convertToDto(user);
        }
    }

    /**
     * Récupère tous les utilisateurs actifs
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche des utilisateurs
     */
    @Transactional(readOnly = true)
    public List<UserDto> searchUsers(String searchTerm) {
        return userRepository.searchActiveUsers(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Désactive un utilisateur
     */
    public void deactivateUser(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + keycloakId));
        
        user.setIsActive(false);
        userRepository.save(user);
        log.info("Utilisateur désactivé: {}", user.getUsername());
    }

    /**
     * Active un utilisateur
     */
    public void activateUser(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + keycloakId));
        
        user.setIsActive(true);
        userRepository.save(user);
        log.info("Utilisateur activé: {}", user.getUsername());
    }

    // Méthodes privées utilitaires

    private void assignRolesToUser(User user, List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            // Assigner le rôle par défaut
            assignDefaultRole(user);
            return;
        }

        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName).orElse(null);
            if (role != null && role.getIsActive()) {
                UserRole userRole = UserRole.builder()
                        .user(user)
                        .role(role)
                        .isActive(true)
                        .assignedBy("SYSTEM")
                        .build();
                user.getUserRoles().add(userRole);
            }
        }

        // Si aucun rôle valide n'a été assigné, assigner le rôle par défaut
        if (user.getUserRoles().isEmpty()) {
            assignDefaultRole(user);
        }
    }

    private void assignDefaultRole(User user) {
        Role defaultRole = roleRepository.findByName(defaultRoleName).orElse(null);
        if (defaultRole != null && defaultRole.getIsActive()) {
            UserRole userRole = UserRole.builder()
                    .user(user)
                    .role(defaultRole)
                    .isActive(true)
                    .assignedBy("SYSTEM")
                    .build();
            user.getUserRoles().add(userRole);
        }
    }

    private void syncUserRoles(User user, List<String> keycloakRoles) {
        // Désactiver tous les rôles actuels
        user.getUserRoles().forEach(ur -> ur.setActive(false));
        
        // Assigner les nouveaux rôles
        assignRolesToUser(user, keycloakRoles);
    }

    private UserDto convertToDto(User user) {
        List<String> roles = user.getUserRoles().stream()
                .filter(ur -> ur.getActive())
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toList());

        List<String> permissions = permissionRepository.findActivePermissionsByUserKeycloakId(user.getKeycloakId())
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toList());

        return UserDto.builder()
                .keycloakId(user.getKeycloakId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .department(user.getDepartment())
                .position(user.getPosition())
                .isActive(user.getIsActive())
                .preferredLanguage(user.getPreferredLanguage())
                .profilePictureUrl(user.getProfilePictureUrl())
                .roles(roles)
                .permissions(permissions)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }

    private UserInfoDto convertToUserInfoDto(User user) {
        List<String> roles = user.getUserRoles().stream()
                .filter(ur -> ur.getActive())
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toList());

        List<String> permissions = permissionRepository.findActivePermissionsByUserKeycloakId(user.getKeycloakId())
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toList());

        return UserInfoDto.builder()
                .userId(user.getKeycloakId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .department(user.getDepartment())
                .position(user.getPosition())
                .isActive(user.getIsActive())
                .preferredLanguage(user.getPreferredLanguage())
                .roles(roles)
                .permissions(permissions)
                .build();
    }
}

