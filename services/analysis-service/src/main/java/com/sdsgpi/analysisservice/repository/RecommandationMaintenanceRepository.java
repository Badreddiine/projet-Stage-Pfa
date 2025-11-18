package com.sdsgpi.analysisservice.repository;

import com.sdsgpi.analysisservice.entity.RecommandationMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommandationMaintenanceRepository extends JpaRepository<RecommandationMaintenance, Long> {

    /**
     * Trouve les recommandations par équipement
     */
    List<RecommandationMaintenance> findByEquipmentIdOrderByDateCreationDesc(Long equipmentId);

    /**
     * Trouve les recommandations par statut
     */
    List<RecommandationMaintenance> findByStatusOrderByDateCreationDesc(RecommandationMaintenance.StatusRecommandation status);

    /**
     * Trouve les recommandations par priorité
     */
    List<RecommandationMaintenance> findByPrioriteOrderByDateCreationDesc(RecommandationMaintenance.PrioriteRecommandation priorite);

    /**
     * Trouve les recommandations par type
     */
    List<RecommandationMaintenance> findByTypeRecommandationOrderByDateCreationDesc(String typeRecommandation);

    /**
     * Trouve les recommandations en attente avec priorité haute ou critique
     */
    @Query("SELECT r FROM RecommandationMaintenance r WHERE r.status = 'EN_ATTENTE' AND r.priorite IN ('HAUTE', 'CRITIQUE') ORDER BY r.priorite DESC, r.dateCreation ASC")
    List<RecommandationMaintenance> findUrgentRecommandations();

    /**
     * Trouve les recommandations créées dans une période donnée
     */
    List<RecommandationMaintenance> findByDateCreationBetweenOrderByDateCreationDesc(LocalDateTime debut, LocalDateTime fin);

    /**
     * Trouve les recommandations avec un score de confiance minimum
     */
    @Query("SELECT r FROM RecommandationMaintenance r WHERE r.scoreConfiance >= :scoreMin ORDER BY r.scoreConfiance DESC")
    List<RecommandationMaintenance> findByScoreConfianceGreaterThanEqual(@Param("scoreMin") Double scoreMin);

    /**
     * Trouve les recommandations approuvées par un utilisateur
     */
    List<RecommandationMaintenance> findByApprouveParOrderByDateCreationDesc(String approuvePar);

    /**
     * Compte les recommandations par statut
     */
    @Query("SELECT r.status, COUNT(r) FROM RecommandationMaintenance r GROUP BY r.status")
    List<Object[]> countByStatus();

    /**
     * Trouve les recommandations avec coût estimé dans une fourchette
     */
    @Query("SELECT r FROM RecommandationMaintenance r WHERE r.coutEstime BETWEEN :coutMin AND :coutMax ORDER BY r.coutEstime ASC")
    List<RecommandationMaintenance> findByCoutEstimeBetween(@Param("coutMin") Double coutMin, @Param("coutMax") Double coutMax);

    /**
     * Recherche textuelle dans titre et description
     */
    @Query("SELECT r FROM RecommandationMaintenance r WHERE " +
           "LOWER(r.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY r.dateCreation DESC")
    List<RecommandationMaintenance> searchByTitleAndDescription(@Param("searchTerm") String searchTerm);
}

