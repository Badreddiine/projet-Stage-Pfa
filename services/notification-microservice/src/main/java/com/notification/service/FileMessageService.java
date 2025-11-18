package com.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.dto.FileMessagedto;
import com.notification.dto.Notificationdto;
import com.notification.entity.FileMessage;
import com.notification.entity.Notification;
import com.notification.repository.FileMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class FileMessageService {


    private final FileMessageRepository fileMessageRepository;
    private final ObjectMapper objectMapper;
    public FileMessageService(FileMessageRepository fileMessageRepository, ObjectMapper objectMapper) {
        this.fileMessageRepository = fileMessageRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public FileMessage creerMessageFile(Notificationdto notification) {
        try {
            FileMessage fileMessage = new FileMessage();
            fileMessage.setNomFile("notification-" + notification.getId());
            fileMessage.setTypeMessage(notification.getType());
            fileMessage.setContenu(objectMapper.writeValueAsString(notification));
            fileMessage.setStatut("EN_ATTENTE");
            fileMessage.setDateCreation(LocalDateTime.now());
            fileMessage.setCompteurTentatives(0);
            fileMessage.setMaxTentatives(3);

            // Associer le topic MQTT si disponible
            if (notification.getMetadonnees() != null &&
                    notification.getMetadonnees().containsKey("topicMQTT")) {
                fileMessage.setTopicMQTT((String) notification.getMetadonnees().get("topicMQTT"));
            }

            FileMessage saved = fileMessageRepository.save(fileMessage);

            return saved;

        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer le message en file", e);
        }
    }

    @Scheduled(fixedDelay = 30000) // Toutes les 30 secondes
    @Transactional
    public void traiterMessagesEnAttente() {
        List<FileMessagedto> messagesEnAttente = fileMessageRepository.findMessagesEnAttente()
                .stream()
                .map(FileMessagedto::new)
                .collect(Collectors.toList());


        for (FileMessagedto message : messagesEnAttente) {
            traiterMessage(message);
        }
    }

    @Async
    @Transactional
    public void traiterMessage(FileMessagedto message) {
        try {

            // Incrémenter le compteur de tentatives
            message.setCompteurTentatives(message.getCompteurTentatives() + 1);

            // Simuler le traitement du message
            // Ici vous pouvez ajouter la logique spécifique selon le type de message
            boolean succes = traiterSelonType(message);

            if (succes) {
                message.setStatut("TRAITE");
                message.setDateTraitement(LocalDateTime.now());
            } else {
                if (message.getCompteurTentatives() >= message.getMaxTentatives()) {
                    message.setStatut("ECHEC");
                    message.setMessageErreur("Nombre maximum de tentatives atteint");

                } else {
                    message.setStatut("EN_ATTENTE");

                }
            }
                FileMessage message1= new FileMessage(message);
            fileMessageRepository.save(message1);

        } catch (Exception e) {
            message.setStatut("ERREUR");
            message.setMessageErreur(e.getMessage());
            FileMessage message1= new FileMessage(message);
            fileMessageRepository.save(message1);
        }
    }

    private boolean traiterSelonType(FileMessagedto message) {
        try {
            switch (message.getTypeMessage()) {
                case "EMAIL":
                    return traiterEmail(message);
                case "SMS":
                    return traiterSMS(message);
                case "PUSH":
                    return traiterPushNotification(message);
                case "MQTT":
                    return traiterMQTT(message);
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean traiterEmail(FileMessagedto message) {
        // Logique d'envoi d'email
        // Simuler un succès
        return true;
    }

    private boolean traiterSMS(FileMessagedto message) {
        // Logique d'envoi de SMS
        // Simuler un succès
        return true;
    }

    private boolean traiterPushNotification(FileMessagedto message) {
        // Logique d'envoi de notification push
        // Simuler un succès
        return true;
    }

    private boolean traiterMQTT(FileMessagedto message) {
        // Logique d'envoi via MQTT
        // Simuler un succès
        return true;
    }

    public List<FileMessagedto> obtenirMessagesParStatut(String statut) {
        return fileMessageRepository.findByStatut(statut)
                .stream()
                .map(FileMessagedto::new)
                .collect(Collectors.toList());
    }

    public Optional<FileMessagedto> obtenirMessageParId(Long id) {
        return fileMessageRepository.findById(id)
                .map(FileMessagedto::new);

    }

    @Transactional
    public void relancerMessage(Long messageId) {
        Optional<FileMessage> messageOpt = fileMessageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            FileMessage message = messageOpt.get();
            message.setStatut("EN_ATTENTE");
            message.setCompteurTentatives(0);
            message.setMessageErreur(null);
            fileMessageRepository.save(message);
        }
    }

    @Scheduled(cron = "0 0 2 * * ?") // Tous les jours à 2h du matin
    @Transactional
    public void nettoyerAnciennesMessages() {
        LocalDateTime dateLimit = LocalDateTime.now().minusDays(30);
        List<FileMessage> anciennesMessages = fileMessageRepository
                .findByDateCreationBetween(LocalDateTime.MIN, dateLimit);

        for (FileMessage message : anciennesMessages) {
            if ("TRAITE".equals(message.getStatut()) || "ECHEC".equals(message.getStatut())) {
                fileMessageRepository.delete(message);
            }
        }

    }
}


