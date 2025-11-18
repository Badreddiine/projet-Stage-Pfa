package com.sdsgpi.analysisservice.repository;

import com.sdsgpi.analysisservice.entity.AnalyseTendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyseTendanceRepository extends JpaRepository<AnalyseTendance, Long> {

    /**
     * Trouve les analyses par type d'analyse
     */
    List<AnalyseTendance> findByTypeAnalyseOrderByCreatedAtDesc(String typeAnalyse);

    /**
     * Trouve les analyses par type d'équipement
     */
    List<AnalyseTendance> findByTypeEquipementOrderByCreatedAtDesc(String typeEquipement);

    /**
     * Trouve les analyses par métrique
     */
    List<AnalyseTendance> findByMetriqueOrderByCreatedAtDesc(String metrique);

    /**
     * Trouve les analyses par type de tendance
     */
    List<AnalyseTendance> findByTendanceOrderByCreatedAtDesc(AnalyseTendance.TypeTendance tendance);

    /**
     * Trouve les analyses dans une période donnée
     */
    List<AnalyseTendance> findByDebutPeriodeBetweenOrderByCreatedAtDesc(LocalDateTime debut, LocalDateTime fin);

    /**
     * Trouve les analyses avec un niveau de confiance minimum
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE a.niveauConfiance >= :niveauMin ORDER BY a.niveauConfiance DESC")
    List<AnalyseTendance> findByNiveauConfianceGreaterThanEqual(@Param("niveauMin") Double niveauMin);

    /**
     * Trouve les analyses avec corrélation forte (positive ou négative)
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE ABS(a.correlation) >= :correlationMin ORDER BY ABS(a.correlation) DESC")
    List<AnalyseTendance> findByStrongCorrelation(@Param("correlationMin") Double correlationMin);

    /**
     * Trouve les tendances croissantes avec forte corrélation
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE a.tendance = 'CROISSANTE' AND a.correlation >= :correlationMin ORDER BY a.correlation DESC")
    List<AnalyseTendance> findGrowingTrendsWithStrongCorrelation(@Param("correlationMin") Double correlationMin);

    /**
     * Trouve les tendances décroissantes avec forte corrélation
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE a.tendance = 'DECROISSANTE' AND a.correlation <= :correlationMax ORDER BY a.correlation ASC")
    List<AnalyseTendance> findDecliningTrendsWithStrongCorrelation(@Param("correlationMax") Double correlationMax);

    /**
     * Recherche textuelle dans interprétation
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE LOWER(a.interpretation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY a.createdAt DESC")
    List<AnalyseTendance> searchByInterpretation(@Param("searchTerm") String searchTerm);

    /**
     * Trouve les analyses par métrique et type d'équipement
     */
    List<AnalyseTendance> findByMetriqueAndTypeEquipementOrderByCreatedAtDesc(String metrique, String typeEquipement);

    /**
     * Compte les analyses par type de tendance
     */
    @Query("SELECT a.tendance, COUNT(a) FROM AnalyseTendance a GROUP BY a.tendance")
    List<Object[]> countByTendanceType();

    /**
     * Trouve les analyses récentes (dernières 24h)
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE a.createdAt >= :since ORDER BY a.createdAt DESC")
    List<AnalyseTendance> findRecentAnalyses(@Param("since") LocalDateTime since);

    /**
     * Trouve les analyses avec pente significative
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE ABS(a.pente) >= :penteMin ORDER BY ABS(a.pente) DESC")
    List<AnalyseTendance> findBySignificantSlope(@Param("penteMin") Double penteMin);

    /**
     * Trouve les analyses volatiles
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE a.tendance = 'VOLATILE' ORDER BY a.createdAt DESC")
    List<AnalyseTendance> findVolatileAnalyses();

    /**
     * Trouve les analyses cycliques
     */
    @Query("SELECT a FROM AnalyseTendance a WHERE a.tendance = 'CYCLIQUE' ORDER BY a.createdAt DESC")
    List<AnalyseTendance> findCyclicAnalyses();
}

