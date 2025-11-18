package com.badr.equipement_service.repository;
import com.badr.equipement_service.entity.DonneesCapteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface DonneesCapteurRepository extends JpaRepository<DonneesCapteur, Long> {

    List<DonneesCapteur> findByIdentifiantEquipement(Long identifiantEquipement);

    List<DonneesCapteur> findByTypeCapteur(String typeCapteur);

    List<DonneesCapteur> findByQualite(String qualite);

    @Query("SELECT d FROM DonneesCapteur d WHERE d.identifiantEquipement = :equipementId AND d.typeCapteur = :typeCapteur ORDER BY d.horodatage DESC")
    List<DonneesCapteur> findByEquipementAndTypeCapteurOrderByHorodatageDesc(@Param("equipementId") Long equipementId, @Param("typeCapteur") String typeCapteur);

    @Query("SELECT d FROM DonneesCapteur d WHERE d.horodatage BETWEEN :dateDebut AND :dateFin")
    List<DonneesCapteur> findByHorodatageBetween(@Param("dateDebut") Timestamp dateDebut, @Param("dateFin") Timestamp dateFin);

    @Query("SELECT d FROM DonneesCapteur d WHERE d.identifiantEquipement = :equipementId AND d.horodatage >= :depuis ORDER BY d.horodatage DESC")
    List<DonneesCapteur> findRecentDataByEquipement(@Param("equipementId") Long equipementId, @Param("depuis") Timestamp depuis);

    @Query("SELECT AVG(d.valeur) FROM DonneesCapteur d WHERE d.identifiantEquipement = :equipementId AND d.typeCapteur = :typeCapteur AND d.horodatage >= :depuis")
    Double findAverageValueByEquipementAndTypeCapteur(@Param("equipementId") Long equipementId, @Param("typeCapteur") String typeCapteur, @Param("depuis") Timestamp depuis);

    @Query("SELECT d FROM DonneesCapteur d WHERE d.identifiantEquipement = :equipementId ORDER BY d.horodatage DESC LIMIT 1")
    DonneesCapteur findLatestByEquipement(@Param("equipementId") Long equipementId);
}
