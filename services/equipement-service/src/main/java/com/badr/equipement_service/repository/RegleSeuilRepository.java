package com.badr.equipement_service.repository;

import com.badr.equipement_service.entity.RegleSeuil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegleSeuilRepository extends JpaRepository<RegleSeuil, Long> {

    List<RegleSeuil> findByIdentifiantEquipement(Long identifiantEquipement);

    List<RegleSeuil> findByParametre(String parametre);

    List<RegleSeuil> findByNiveauAlerte(String niveauAlerte);

    List<RegleSeuil> findByActif(Boolean actif);

    @Query("SELECT r FROM RegleSeuil r WHERE r.identifiantEquipement = :equipementId AND r.actif = true")
    List<RegleSeuil> findActiveRulesByEquipement(@Param("equipementId") Long equipementId);

    @Query("SELECT r FROM RegleSeuil r WHERE r.identifiantEquipement = :equipementId AND r.parametre = :parametre AND r.actif = true")
    List<RegleSeuil> findActiveRulesByEquipementAndParametre(@Param("equipementId") Long equipementId, @Param("parametre") String parametre);

    @Query("SELECT r FROM RegleSeuil r WHERE :valeur < r.valeurMin OR :valeur > r.valeurMax")
    List<RegleSeuil> findRulesViolatedByValue(@Param("valeur") Double valeur);

    @Query("SELECT COUNT(r) FROM RegleSeuil r WHERE r.identifiantEquipement = :equipementId AND r.actif = true")
    Long countActiveRulesByEquipement(@Param("equipementId") Long equipementId);
}

