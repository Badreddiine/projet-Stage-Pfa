package com.notification.repository;

import com.notification.entity.SessionMQTT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionMQTTRepository extends JpaRepository<SessionMQTT, String> {
    
    List<SessionMQTT> findByUtilisateurId(String utilisateurId);
    
    List<SessionMQTT> findByStatutConnexion(String statutConnexion);
    
    Optional<SessionMQTT> findByUtilisateurIdAndStatutConnexion(String utilisateurId, String statutConnexion);
    
    @Query("SELECT s FROM SessionMQTT s WHERE :topic MEMBER OF s.topicsAbonnes")
    List<SessionMQTT> findByTopicAbonne(@Param("topic") String topic);
    
    List<SessionMQTT> findByClientMQTT(String clientMQTT);
    
    @Query("SELECT s FROM SessionMQTT s WHERE s.statutConnexion = 'ACTIVE' AND :topic MEMBER OF s.topicsAbonnes")
    List<SessionMQTT> findActiveSessionsByTopic(@Param("topic") String topic);
}

