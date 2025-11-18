package com.badr.equipement_service.repository;

import com.badr.equipement_service.entity.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {

    List<Alerte> findByEquipementId(Long equipementId);

    List<Alerte> findByStatut(String statut);

    List<Alerte> findBySeverite(String severite);

    List<Alerte> findByTypeAlerte(String typeAlerte);

    @Query("SELECT a FROM Alerte a WHERE a.equipementId = :equipementId AND a.statut = :statut")
    List<Alerte> findByEquipementIdAndStatut(@Param("equipementId") Long equipementId, @Param("statut") String statut);

    @Query("SELECT a FROM Alerte a WHERE a.dateCreation BETWEEN :dateDebut AND :dateFin")
    List<Alerte> findByDateCreationBetween(@Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

    @Query("SELECT COUNT(a) FROM Alerte a WHERE a.equipementId = :equipementId AND a.statut = 'ACTIVE'")
    Long countActiveAlertesByEquipement(@Param("equipementId") Long equipementId);

    @Query("SELECT a FROM Alerte a WHERE a.statut = 'ACTIVE' ORDER BY a.severite DESC, a.dateCreation DESC")
    List<Alerte> findActiveAlertesOrderedBySeverityAndDate();
}
