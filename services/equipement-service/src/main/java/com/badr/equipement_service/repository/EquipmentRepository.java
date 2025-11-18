package com.badr.equipement_service.repository;



import com.badr.equipement_service.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByStatut(String statut);

    List<Equipment> findByTypeEquipment(String typeEquipment);

    List<Equipment> findByEmplacement(String emplacement);

    List<Equipment> findByEstActif(Boolean estActif);

    Optional<Equipment> findByNom(String nom);


    @Query("SELECT e FROM Equipment e WHERE e.compteVerrouille = false AND e.estActif = true")
    List<Equipment> findActiveAndUnlockedEquipments();

    @Query("SELECT e FROM Equipment e WHERE e.tentativesConnexion > :maxTentatives")
    List<Equipment> findEquipmentsWithExcessiveConnectionAttempts(@Param("maxTentatives") Integer maxTentatives);

    @Query("SELECT COUNT(e) FROM Equipment e WHERE e.statut = :statut")
    Long countByStatut(@Param("statut") String statut);
}

