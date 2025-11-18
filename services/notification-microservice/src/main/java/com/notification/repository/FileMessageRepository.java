package com.notification.repository;

import com.notification.entity.FileMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FileMessageRepository extends JpaRepository<FileMessage, Long> {
    
    List<FileMessage> findByStatut(String statut);
    
    List<FileMessage> findByTypeMessage(String typeMessage);
    
    List<FileMessage> findByStatutAndCompteurTentativesLessThan(String statut, Integer maxTentatives);
    
    @Query("SELECT f FROM FileMessage f WHERE f.statut = 'EN_ATTENTE' AND f.compteurTentatives < f.maxTentatives ORDER BY f.dateCreation ASC")
    List<FileMessage> findMessagesEnAttente();
    
    List<FileMessage> findByDateCreationBetween(LocalDateTime debut, LocalDateTime fin);
    
    List<FileMessage> findByTopicMQTT(String topicMQTT);
}

