package com.notification.repository;

import com.notification.entity.PreferenceUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceUtilisateurRepository extends JpaRepository<PreferenceUtilisateur, Long> {
    
    List<PreferenceUtilisateur> findByUtilisateurId(Long utilisateurId);
    
    List<PreferenceUtilisateur> findByUtilisateurIdAndEstActive(Long utilisateurId, Boolean estActive);
    
    Optional<PreferenceUtilisateur> findByUtilisateurIdAndCanalIdAndTypeNotification(
            Long utilisateurId, Long canalId, String typeNotification);
    
    List<PreferenceUtilisateur> findByTypeNotificationAndEstActive(String typeNotification, Boolean estActive);
    
    @Query("SELECT p FROM PreferenceUtilisateur p WHERE p.utilisateurId = :utilisateurId AND :topic MEMBER OF p.topicsMQTT")
    List<PreferenceUtilisateur> findByUtilisateurIdAndTopicMQTT(@Param("utilisateurId") Long utilisateurId, @Param("topic") String topic);
    
    void deleteByUtilisateurIdAndCanalId(Long utilisateurId, Long canalId);
}

