package com.notification.repository;

import com.notification.entity.CanalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CanalNotificationRepository extends JpaRepository<CanalNotification, Long> {
    
    List<CanalNotification> findByEstActif(Boolean estActif);
    
    Optional<CanalNotification> findByNomCanal(String nomCanal);
    
    List<CanalNotification> findByTypeCanal(String typeCanal);
    
    List<CanalNotification> findBySupportMQTT(Boolean supportMQTT);
    
    List<CanalNotification> findByTypeCanalAndEstActif(String typeCanal, Boolean estActif);
    
    Optional<CanalNotification> findByTopicMQTT(String topicMQTT);
}

