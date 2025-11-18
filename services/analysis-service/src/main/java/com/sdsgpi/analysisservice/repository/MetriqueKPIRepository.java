package com.sdsgpi.analysisservice.repository;

import com.sdsgpi.analysisservice.entity.MetriqueKPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MetriqueKPIRepository extends JpaRepository<MetriqueKPI, Long> {

    /**
     * Trouve les métriques par catégorie
     */
    List<MetriqueKPI> findByCategorieOrderByDateCalculDesc(String categorie);

    /**
     * Trouve les métriques par statut
     */
    List<MetriqueKPI> findByStatusOrderByDateCalculDesc(MetriqueKPI.StatusMetrique status);

    /**
     * Trouve une métrique par nom
     */
    Optional<MetriqueKPI> findByNomMetrique(String nomMetrique);

    /**
     * Trouve les métriques actives
     */
    @Query("SELECT m FROM MetriqueKPI m WHERE m.status = 'ACTIF' ORDER BY m.dateCalcul DESC")
    List<MetriqueKPI> findActiveMetrics();

    /**
     * Trouve les métriques calculées dans une période donnée
     */
    List<MetriqueKPI> findByDateCalculBetweenOrderByDateCalculDesc(LocalDateTime debut, LocalDateTime fin);

    /**
     * Trouve les métriques avec alerte (valeur actuelle > seuil alerte)
     */
    @Query("SELECT m FROM MetriqueKPI m WHERE m.valeurActuelle > m.seuilAlerte AND m.seuilAlerte IS NOT NULL ORDER BY m.valeurActuelle DESC")
    List<MetriqueKPI> findMetricsWithAlert();

    /**
     * Trouve les métriques par période de calcul
     */
    List<MetriqueKPI> findByPeriodeCalculOrderByDateCalculDesc(String periodeCalcul);

    /**
     * Trouve les métriques qui n'atteignent pas leur cible
     */
    @Query("SELECT m FROM MetriqueKPI m WHERE m.valeurActuelle < m.valeurCible AND m.valeurCible IS NOT NULL ORDER BY (m.valeurCible - m.valeurActuelle) DESC")
    List<MetriqueKPI> findMetricsBelowTarget();

    /**
     * Trouve les métriques qui dépassent leur cible
     */
    @Query("SELECT m FROM MetriqueKPI m WHERE m.valeurActuelle > m.valeurCible AND m.valeurCible IS NOT NULL ORDER BY (m.valeurActuelle - m.valeurCible) DESC")
    List<MetriqueKPI> findMetricsAboveTarget();

    /**
     * Recherche textuelle dans nom de métrique
     */
    @Query("SELECT m FROM MetriqueKPI m WHERE LOWER(m.nomMetrique) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY m.dateCalcul DESC")
    List<MetriqueKPI> searchByName(@Param("searchTerm") String searchTerm);

    /**
     * Trouve les métriques par catégorie et statut
     */
    List<MetriqueKPI> findByCategorieAndStatusOrderByDateCalculDesc(String categorie, MetriqueKPI.StatusMetrique status);

    /**
     * Compte les métriques par catégorie
     */
    @Query("SELECT m.categorie, COUNT(m) FROM MetriqueKPI m WHERE m.status = 'ACTIF' GROUP BY m.categorie")
    List<Object[]> countActiveMetricsByCategory();

    /**
     * Trouve les métriques récemment mises à jour
     */
    @Query("SELECT m FROM MetriqueKPI m WHERE m.updatedAt >= :since ORDER BY m.updatedAt DESC")
    List<MetriqueKPI> findRecentlyUpdated(@Param("since") LocalDateTime since);
}

