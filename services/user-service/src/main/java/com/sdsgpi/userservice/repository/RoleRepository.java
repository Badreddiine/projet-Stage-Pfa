package com.sdsgpi.userservice.repository;

import com.sdsgpi.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Trouve un rôle par son nom
     */
    Optional<Role> findByName(String name);

    /**
     * Trouve tous les rôles actifs
     */
    List<Role> findByIsActiveTrue();

    /**
     * Trouve tous les rôles inactifs
     */
    List<Role> findByIsActiveFalse();

    /**
     * Vérifie si un rôle existe par son nom
     */
    boolean existsByName(String name);

    /**
     * Recherche de rôles par nom ou nom d'affichage (insensible à la casse)
     */
    @Query("SELECT r FROM Role r WHERE " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.displayName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "r.isActive = true")
    List<Role> searchActiveRoles(@Param("searchTerm") String searchTerm);

    /**
     * Trouve les rôles d'un utilisateur spécifique
     */
    @Query("SELECT r FROM Role r " +
           "JOIN r.userRoles ur " +
           "WHERE ur.user.keycloakId = :keycloakId AND ur.isActive = true AND r.isActive = true")
    List<Role> findActiveRolesByUserKeycloakId(@Param("keycloakId") String keycloakId);

    /**
     * Trouve les rôles ayant une permission spécifique
     */
    @Query("SELECT DISTINCT r FROM Role r " +
           "JOIN r.rolePermissions rp " +
           "JOIN rp.permission p " +
           "WHERE p.name = :permissionName AND r.isActive = true AND rp.isActive = true")
    List<Role> findActiveRolesByPermissionName(@Param("permissionName") String permissionName);
}

