package com.incident.service.repository;

import com.incident.service.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByStatut(String statut);

    List<Incident> findBySeverite(String severite);

    List<Incident> findByUtilisateurAssigneId(Long utilisateurId);

    List<Incident> findByEquipementId(Long equipementId);

    @Query("SELECT i FROM Incident i WHERE i.dateCreation BETWEEN :dateDebut AND :dateFin")
    List<Incident> findByDateCreationBetween(@Param("dateDebut") LocalDateTime dateDebut, 
                                           @Param("dateFin") LocalDateTime dateFin);

    @Query("SELECT i FROM Incident i WHERE i.dateEcheance < :maintenant AND i.statut != 'RESOLU'")
    List<Incident> findIncidentsEnRetard(@Param("maintenant") LocalDateTime maintenant);

    @Query("SELECT COUNT(i) FROM Incident i WHERE i.statut = :statut")
    Long countByStatut(@Param("statut") String statut);
}

