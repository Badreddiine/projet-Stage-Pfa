package com.sdsgpi.userservice.repository;

import com.sdsgpi.userservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * Trouve une permission par son nom
     */
    Optional<Permission> findByName(String name);

    /**
     * Trouve toutes les permissions actives
     */
    List<Permission> findByIsActiveTrue();

    /**
     * Trouve les permissions par ressource
     */
    List<Permission> findByResourceAndIsActiveTrue(String resource);

    /**
     * Trouve les permissions par action
     */
    List<Permission> findByActionAndIsActiveTrue(String action);

    /**
     * Trouve les permissions par ressource et action
     */
    Optional<Permission> findByResourceAndActionAndIsActiveTrue(String resource, String action);

    /**
     * Vérifie si une permission existe par son nom
     */
    boolean existsByName(String name);

    /**
     * Recherche de permissions par nom ou description (insensible à la casse)
     */
    @Query("SELECT p FROM Permission p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.isActive = true")
    List<Permission> searchActivePermissions(@Param("searchTerm") String searchTerm);

    /**
     * Trouve les permissions d'un utilisateur spécifique (directes et via rôles)
     */
    @Query("SELECT DISTINCT p FROM Permission p " +
           "LEFT JOIN p.userPermissions up " +
           "LEFT JOIN up.user u1 " +
           "LEFT JOIN p.rolePermissions rp " +
           "LEFT JOIN rp.role r " +
           "LEFT JOIN r.userRoles ur " +
           "LEFT JOIN ur.user u2 " +
           "WHERE ((u1.keycloakId = :keycloakId AND up.isActive = true) OR " +
           "(u2.keycloakId = :keycloakId AND ur.isActive = true AND rp.isActive = true)) " +
           "AND p.isActive = true")
    List<Permission> findActivePermissionsByUserKeycloakId(@Param("keycloakId") String keycloakId);

    /**
     * Trouve les permissions d'un rôle spécifique
     */
    @Query("SELECT p FROM Permission p " +
           "JOIN p.rolePermissions rp " +
           "JOIN rp.role r " +
           "WHERE r.name = :roleName AND p.isActive = true AND rp.isActive = true")
    List<Permission> findActivePermissionsByRoleName(@Param("roleName") String roleName);
}

