package com.sdsgpi.analysisservice.repository;

import com.sdsgpi.analysisservice.entity.RapportAnalyse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RapportAnalyseRepository extends JpaRepository<RapportAnalyse, Long> {

    /**
     * Trouve les rapports par type
     */
    List<RapportAnalyse> findByTypeRapportOrderByDateGenerationDesc(String typeRapport);

    /**
     * Trouve les rapports par statut
     */
    List<RapportAnalyse> findByStatusOrderByDateGenerationDesc(RapportAnalyse.StatusRapport status);

    /**
     * Trouve les rapports générés par un utilisateur
     */
    List<RapportAnalyse> findByGenereParOrderByDateGenerationDesc(String generePar);

    /**
     * Trouve les rapports générés dans une période donnée
     */
    List<RapportAnalyse> findByDateGenerationBetweenOrderByDateGenerationDesc(LocalDateTime debut, LocalDateTime fin);

    /**
     * Trouve les rapports par format
     */
    List<RapportAnalyse> findByFormatOrderByDateGenerationDesc(String format);

    /**
     * Trouve les rapports terminés récents
     */
    @Query("SELECT r FROM RapportAnalyse r WHERE r.status = 'TERMINE' ORDER BY r.dateGeneration DESC")
    List<RapportAnalyse> findRecentCompletedReports();

    /**
     * Trouve les rapports en cours
     */
    @Query("SELECT r FROM RapportAnalyse r WHERE r.status = 'EN_COURS' ORDER BY r.dateGeneration ASC")
    List<RapportAnalyse> findRunningReports();

    /**
     * Recherche textuelle dans nom et description
     */
    @Query("SELECT r FROM RapportAnalyse r WHERE " +
           "LOWER(r.nomRapport) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY r.dateGeneration DESC")
    List<RapportAnalyse> searchByNameAndDescription(@Param("searchTerm") String searchTerm);

    /**
     * Trouve les rapports par période d'analyse
     */
    @Query("SELECT r FROM RapportAnalyse r WHERE r.dateDebut >= :debut AND r.finPeriode <= :fin ORDER BY r.dateGeneration DESC")
    List<RapportAnalyse> findByAnalysisPeriod(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    /**
     * Compte les rapports par statut
     */
    @Query("SELECT r.status, COUNT(r) FROM RapportAnalyse r GROUP BY r.status")
    List<Object[]> countByStatus();

    /**
     * Trouve les rapports volumineux (taille > seuil)
     */
    @Query("SELECT r FROM RapportAnalyse r WHERE r.tailleOctets > :tailleSeuil ORDER BY r.tailleOctets DESC")
    List<RapportAnalyse> findLargeReports(@Param("tailleSeuil") Long tailleSeuil);
}

