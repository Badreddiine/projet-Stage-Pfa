package com.sdsgpi.userservice.repository;

import com.sdsgpi.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Trouve un utilisateur par son ID Keycloak
     */
    Optional<User> findByKeycloakId(String keycloakId);

    /**
     * Trouve un utilisateur par son nom d'utilisateur
     */
    Optional<User> findByUsername(String username);

    /**
     * Trouve un utilisateur par son email
     */
    Optional<User> findByEmail(String email);

    /**
     * Trouve tous les utilisateurs actifs
     */
    List<User> findByIsActiveTrue();

    /**
     * Trouve tous les utilisateurs inactifs
     */
    List<User> findByIsActiveFalse();

    /**
     * Trouve les utilisateurs par département
     */
    List<User> findByDepartmentAndIsActiveTrue(String department);

    /**
     * Trouve les utilisateurs par position
     */
    List<User> findByPositionAndIsActiveTrue(String position);

    /**
     * Recherche d'utilisateurs par nom ou prénom (insensible à la casse)
     */
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.isActive = true")
    List<User> searchActiveUsers(@Param("searchTerm") String searchTerm);

    /**
     * Vérifie si un utilisateur existe par son ID Keycloak
     */
    boolean existsByKeycloakId(String keycloakId);

    /**
     * Vérifie si un utilisateur existe par son nom d'utilisateur
     */
    boolean existsByUsername(String username);

    /**
     * Vérifie si un utilisateur existe par son email
     */
    boolean existsByEmail(String email);

    /**
     * Trouve les utilisateurs avec un rôle spécifique
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE r.name = :roleName AND u.isActive = true AND ur.isActive = true")
    List<User> findActiveUsersByRoleName(@Param("roleName") String roleName);

    /**
     * Trouve les utilisateurs avec une permission spécifique
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN u.userPermissions up " +
            "LEFT JOIN up.permission p1 " +
            "LEFT JOIN u.userRoles ur " +
            "LEFT JOIN ur.role r " +
            "LEFT JOIN r.rolePermissions rp " +
            "LEFT JOIN rp.permission p2 " +
            "WHERE (p1.name = :permissionName AND up.isActive = true) OR " +
            "(p2.name = :permissionName AND ur.isActive = true AND rp.isActive = true) " +
            "AND u.isActive = true")
    List<User> findActiveUsersByPermissionName(@Param("permissionName") String permissionName);


}
