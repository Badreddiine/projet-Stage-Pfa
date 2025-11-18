package com.notification.service;


import com.notification.dto.Notificationdto;
import com.notification.dto.PreferenceUtilisateurdto;
import com.notification.entity.Notification;
import com.notification.repository.NotificationRepository;
import com.notification.repository.PreferenceUtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional

public class NotificationService {


    private final NotificationRepository notificationRepository;
    private final PreferenceUtilisateurRepository preferenceRepository;
    private final MQTTService mqttService;
    private final FileMessageService fileMessageService;

    public NotificationService(NotificationRepository notificationRepository, PreferenceUtilisateurRepository preferenceRepository, MQTTService mqttService, FileMessageService fileMessageService) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.mqttService = mqttService;
        this.fileMessageService = fileMessageService;
    }

    @Transactional
    public Notificationdto creerNotification(Notificationdto notification) {

        // Vérifier les préférences utilisateur
        List<PreferenceUtilisateurdto> preferences = preferenceRepository
                .findByUtilisateurIdAndEstActive(Long.valueOf(notification.getDestinataireId()), true)
                .stream()
                .map(PreferenceUtilisateurdto::new)
                .collect(Collectors.toList());

        if (preferences.isEmpty()) {
        }
        Notification nouvelleNotification = new Notification(notification);

        Notification savedNotification = notificationRepository.save(nouvelleNotification);

        // Traitement asynchrone de l'envoi
        traiterEnvoiNotification(notification);

        return notification;
    }

    @Transactional
    public void traiterEnvoiNotification(Notificationdto notification) {
        try {
            // Vérifier les préférences pour le type de notification
            List<PreferenceUtilisateurdto> preferences = preferenceRepository
                    .findByUtilisateurIdAndEstActive(Long.valueOf(notification.getDestinataireId()), true)
                    .stream()
                    .map(PreferenceUtilisateurdto::new)
                    .collect(Collectors.toList());

            for (PreferenceUtilisateurdto preference : preferences) {
                if (preference.getTypeNotification().equals(notification.getType())) {
                    // Envoyer via MQTT si configuré
                    if (preference.getTopicsMQTT() != null && !preference.getTopicsMQTT().isEmpty()) {
                        for (String topic : preference.getTopicsMQTT()) {
                            mqttService.publierMessage(topic, notification);
                        }
                    }

                    // Créer un message en file d'attente
                    fileMessageService.creerMessageFile(notification);
                }
            }

            // Mettre à jour le statut
            notification.setStatut("ENVOYE");
            notification.setDateEnvoi(LocalDateTime.now());
            Notification notification1 = new Notification(notification);
            notificationRepository.save(notification1);

        } catch (Exception e) {
            notification.setStatut("ERREUR");
            Notification notification2 = new Notification(notification);
            notificationRepository.save(notification2);
        }
    }

    public List<Notificationdto> obtenirNotificationsUtilisateur(String utilisateurId) {
        return notificationRepository.findByDestinataireId(utilisateurId)
                .stream()
                .map(Notificationdto::new)
                .collect(Collectors.toList());
    }

    public List<Notificationdto> obtenirNotificationsNonLues(String utilisateurId) {
        return notificationRepository.findNotificationsNonLues(utilisateurId)
                .stream()
                .map(Notificationdto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void marquerCommeLue(Long notificationId) {
        Optional<Notificationdto> notificationOpt = notificationRepository.findById(notificationId)
                .map(Notificationdto::new);
        if (notificationOpt.isPresent()) {
            Notificationdto notification = notificationOpt.get();
            notification.setDateLecture(LocalDateTime.now());
            notification.setStatut("LUE");
            Notification notification1 = new Notification(notification);
            notificationRepository.save(notification1);
        }
    }

    public Long compterNotificationsNonLues(String utilisateurId) {
        return notificationRepository.countNotificationsNonLues(utilisateurId);
    }

    public List<Notificationdto> obtenirNotificationsAEnvoyer() {
        return notificationRepository.findNotificationsAEnvoyer(LocalDateTime.now())
                .stream()
                .map(Notificationdto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void supprimerNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
