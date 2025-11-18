package com.notification.repository;

import com.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByDestinataireId(String destinataireId);
    List<Notification> findByDestinataireIdAndStatut(String destinataireId, String statut);
    
    List<Notification> findByType(String type);
    
    List<Notification> findByCanal(String canal);
    
    List<Notification> findByStatut(String statut);
    
    List<Notification> findByPriorite(String priorite);
    
    @Query("SELECT n FROM Notification n WHERE n.destinataireId = :destinataireId AND n.dateLecture IS NULL ORDER BY n.dateCreation DESC")
    List<Notification> findNotificationsNonLues(@Param("destinataireId") String destinataireId);
    
    @Query("SELECT n FROM Notification n WHERE n.statut = 'CREE' AND n.dateCreation <= :dateLimit ORDER BY n.priorite DESC, n.dateCreation ASC")
    List<Notification> findNotificationsAEnvoyer(@Param("dateLimit") LocalDateTime dateLimit);
    
    List<Notification> findByDateCreationBetween(LocalDateTime debut, LocalDateTime fin);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataireId = :destinataireId AND n.dateLecture IS NULL")
    Long countNotificationsNonLues(@Param("destinataireId") String destinataireId);
}

