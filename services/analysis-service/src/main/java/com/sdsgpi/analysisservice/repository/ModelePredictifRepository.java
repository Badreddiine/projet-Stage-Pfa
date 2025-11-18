package com.sdsgpi.analysisservice.repository;

import com.sdsgpi.analysisservice.entity.ModelePredictif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModelePredictifRepository extends JpaRepository<ModelePredictif, Long> {

    /**
     * Trouve un modèle par nom
     */
    Optional<ModelePredictif> findByNomModele(String nomModele);

    /**
     * Trouve les modèles par type
     */
    List<ModelePredictif> findByTypeModeleOrderByDateEntrainementDesc(String typeModele);

    /**
     * Trouve les modèles par algorithme
     */
    List<ModelePredictif> findByAlgorithmeOrderByDateEntrainementDesc(String algorithme);

    /**
     * Trouve les modèles par statut
     */
    List<ModelePredictif> findByStatusOrderByDateEntrainementDesc(ModelePredictif.StatusModele status);

    /**
     * Trouve les modèles prêts à être utilisés
     */
    @Query("SELECT m FROM ModelePredictif m WHERE m.status IN ('ENTRAINE', 'DEPLOYE') ORDER BY m.precision DESC")
    List<ModelePredictif> findReadyModels();

    /**
     * Trouve les modèles déployés
     */
    @Query("SELECT m FROM ModelePredictif m WHERE m.status = 'DEPLOYE' ORDER BY m.derniereUtilisation DESC")
    List<ModelePredictif> findDeployedModels();

    /**
     * Trouve les modèles avec une précision minimum
     */
    @Query("SELECT m FROM ModelePredictif m WHERE m.precision >= :precisionMin ORDER BY m.precision DESC")
    List<ModelePredictif> findByPrecisionGreaterThanEqual(@Param("precisionMin") Double precisionMin);

    /**
     * Trouve les modèles entraînés dans une période donnée
     */
    List<ModelePredictif> findByDateEntrainementBetweenOrderByDateEntrainementDesc(LocalDateTime debut, LocalDateTime fin);

    /**
     * Trouve les modèles récemment utilisés
     */
    @Query("SELECT m FROM ModelePredictif m WHERE m.derniereUtilisation >= :since ORDER BY m.derniereUtilisation DESC")
    List<ModelePredictif> findRecentlyUsed(@Param("since") LocalDateTime since);

    /**
     * Trouve les modèles non utilisés depuis une date
     */
    @Query("SELECT m FROM ModelePredictif m WHERE m.derniereUtilisation < :since OR m.derniereUtilisation IS NULL ORDER BY m.derniereUtilisation ASC")
    List<ModelePredictif> findUnusedSince(@Param("since") LocalDateTime since);

    /**
     * Recherche textuelle dans nom de modèle
     */
    @Query("SELECT m FROM ModelePredictif m WHERE LOWER(m.nomModele) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY m.dateEntrainement DESC")
    List<ModelePredictif> searchByName(@Param("searchTerm") String searchTerm);

    /**
     * Trouve le meilleur modèle par type (plus haute précision)
     */
    @Query("SELECT m FROM ModelePredictif m WHERE m.typeModele = :typeModele AND m.status IN ('ENTRAINE', 'DEPLOYE') ORDER BY m.precision DESC")
    List<ModelePredictif> findBestModelsByType(@Param("typeModele") String typeModele);

    /**
     * Compte les modèles par statut
     */
    @Query("SELECT m.status, COUNT(m) FROM ModelePredictif m GROUP BY m.status")
    List<Object[]> countByStatus();

    /**
     * Trouve les modèles par version
     */
    List<ModelePredictif> findByVersionOrderByDateEntrainementDesc(String version);

    /**
     * Trouve les modèles en erreur
     */
    @Query("SELECT m FROM ModelePredictif m WHERE m.status = 'ERREUR' ORDER BY m.updatedAt DESC")
    List<ModelePredictif> findModelsInError();
}

