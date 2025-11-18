package com.sdsgpi.userservice.config;

import com.sdsgpi.userservice.entity.Permission;
import com.sdsgpi.userservice.entity.Role;
// import com.sdsgpi.userservice.entity.RolePermission; // 1. SUPPRIMER CET IMPORT
import com.sdsgpi.userservice.entity.RolePermission;
import com.sdsgpi.userservice.repository.PermissionRepository;
import com.sdsgpi.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional; // Assurez-vous que cet import est présent

/**
 * Initialisateur de données pour créer les rôles et permissions par défaut
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Initialisation des données par défaut...");

        initializePermissions();
        initializeRoles();

        log.info("Initialisation des données terminée.");
    }

    private void initializePermissions() {
        List<PermissionData> permissionsData = Arrays.asList(
                new PermissionData("READ_USER", "USER", "READ", "Lire les informations utilisateur"),
                new PermissionData("WRITE_USER", "USER", "WRITE", "Créer et modifier les utilisateurs"),
                new PermissionData("DELETE_USER", "USER", "DELETE", "Supprimer les utilisateurs"),
                new PermissionData("READ_EQUIPMENT", "EQUIPMENT", "READ", "Lire les informations d'équipement"),
                new PermissionData("WRITE_EQUIPMENT", "EQUIPMENT", "WRITE", "Créer et modifier les équipements"),
                new PermissionData("DELETE_EQUIPMENT", "EQUIPMENT", "DELETE", "Supprimer les équipements"),
                new PermissionData("READ_NOTIFICATION", "NOTIFICATION", "READ", "Lire les notifications"),
                new PermissionData("WRITE_NOTIFICATION", "NOTIFICATION", "WRITE", "Créer et modifier les notifications"),
                new PermissionData("DELETE_NOTIFICATION", "NOTIFICATION", "DELETE", "Supprimer les notifications"),
                new PermissionData("ADMIN_SYSTEM", "SYSTEM", "ADMIN", "Administration système complète")
        );

        for (PermissionData permData : permissionsData) {
            if (!permissionRepository.existsByName(permData.name)) {
                Permission permission = Permission.builder()
                        .name(permData.name)
                        .resource(permData.resource)
                        .action(permData.action)
                        .description(permData.description)
                        .isActive(true)
                        .build();
                permissionRepository.save(permission);
                log.info("Permission créée: {}", permData.name);
            }
        }
    }

    private void initializeRoles() {
        List<RoleData> rolesData = Arrays.asList(
                new RoleData("ADMIN", "Administrateur", "Administrateur système avec tous les droits",
                        Arrays.asList("READ_USER", "WRITE_USER", "DELETE_USER", "READ_EQUIPMENT",
                                "WRITE_EQUIPMENT", "DELETE_EQUIPMENT", "READ_NOTIFICATION",
                                "WRITE_NOTIFICATION", "DELETE_NOTIFICATION", "ADMIN_SYSTEM")),
                new RoleData("MANAGER", "Gestionnaire", "Gestionnaire avec droits de lecture/écriture",
                        Arrays.asList("READ_USER", "WRITE_USER", "READ_EQUIPMENT", "WRITE_EQUIPMENT",
                                "READ_NOTIFICATION", "WRITE_NOTIFICATION")),
                new RoleData("TECHNICIAN", "Technicien", "Technicien avec droits sur les équipements",
                        Arrays.asList("READ_USER", "READ_EQUIPMENT", "WRITE_EQUIPMENT", "READ_NOTIFICATION")),
                new RoleData("USER", "Utilisateur", "Utilisateur standard avec droits de lecture",
                        Arrays.asList("READ_USER", "READ_EQUIPMENT", "READ_NOTIFICATION"))
        );

        for (RoleData roleData : rolesData) {
            if (!roleRepository.existsByName(roleData.name)) {
                Role role = Role.builder()
                        .name(roleData.name)
                        .displayName(roleData.displayName)
                        .description(roleData.description)
                        .isActive(true)
                        .build();
                // Il n'est pas nécessaire de sauvegarder le rôle ici,
                // la sauvegarde finale suffira grâce à la transaction.

                // --- DÉBUT DE LA SECTION CORRIGÉE ---
                // Assigner les permissions au rôle
                for (String permissionName : roleData.permissions) {
                    Optional<Permission> permissionOpt = permissionRepository.findByName(permissionName);
                    permissionOpt.ifPresent(permission -> {
                        RolePermission rolePermission = RolePermission.builder()
                                .role(role)
                                .permission(permission)
                                .isActive(true)
                                .createdAt(LocalDateTime.now())
                                .build();
                        role.getRolePermissions().add(rolePermission);
                    });
                }

                // --- FIN DE LA SECTION CORRIGÉE ---

                roleRepository.save(role); // On sauvegarde le rôle avec ses permissions associées
                log.info("Rôle créé: {} avec {} permissions", roleData.name, role.getRolePermissions().size());
            }
        }
    }

    // Les classes internes PermissionData et RoleData restent inchangées
    private static class PermissionData {
        final String name;
        final String resource;
        final String action;
        final String description;

        PermissionData(String name, String resource, String action, String description) {
            this.name = name;
            this.resource = resource;
            this.action = action;
            this.description = description;
        }
    }

    private static class RoleData {
        final String name;
        final String displayName;
        final String description;
        final List<String> permissions;

        RoleData(String name, String displayName, String description, List<String> permissions) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.permissions = permissions;
        }
    }
}
