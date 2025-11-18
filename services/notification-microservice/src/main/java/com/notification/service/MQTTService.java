package com.notification.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.dto.Notificationdto;
import com.notification.entity.Notification;
import com.notification.entity.SessionMQTT;
import com.notification.repository.SessionMQTTRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@Slf4j
public class MQTTService {
    private final SessionMQTTRepository sessionRepository;
    private final ObjectMapper objectMapper;
    public MQTTService(SessionMQTTRepository sessionRepository , ObjectMapper objectMapper) {
        this.sessionRepository = sessionRepository;
        this.objectMapper = objectMapper;
    }
    @Value("${mqtt.broker.url:tcp://localhost:1883}")
    private String brokerUrl;

    @Value("${mqtt.client.id:notification-service}")
    private String clientId;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;

    private MqttClient mqttClient;



    @PostConstruct
    public void initialiser() {
        try {
            String fullClientId = clientId + "-" + UUID.randomUUID().toString();
            mqttClient = new MqttClient(brokerUrl, fullClientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(60);

            if (!username.isEmpty()) {
                options.setUserName(username);
                options.setPassword(password.toCharArray());
            }

            // Callback pour les événements MQTT
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    traiterMessageRecu(topic, message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            mqttClient.connect(options);

        } catch (MqttException e) {
        }
    }

    public void publierMessage(String topic, Notificationdto notification) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                String messageJson = objectMapper.writeValueAsString(notification);
                MqttMessage message = new MqttMessage(messageJson.getBytes());
                message.setQos(1);
                message.setRetained(false);

                mqttClient.publish(topic, message);
            } else {
            }
        } catch (Exception e) {
        }
    }

    public void souscrireAuTopic(String topic) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.subscribe(topic, 1);
            }
        } catch (MqttException e) {
        }
    }

    public void desouscrireDuTopic(String topic) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.unsubscribe(topic);
            }
        } catch (MqttException e) {
        }
    }

    public SessionMQTT creerSession(String utilisateurId, String roleUtilisateur, String adresseIP) {
        SessionMQTT session = new SessionMQTT();
        session.setIdSession(UUID.randomUUID().toString());
        session.setUtilisateurId(utilisateurId);
        session.setRoleUtilisateur(roleUtilisateur);
        session.setAdresseIP(adresseIP);
        session.setDateConnexion(LocalDateTime.now());
        session.setStatutConnexion("ACTIVE");
        session.setClientMQTT(clientId);
        session.setQualiteService(1);

        return sessionRepository.save(session);
    }

    public void fermerSession(String sessionId) {
        sessionRepository.findById(sessionId).ifPresent(session -> {
            session.setStatutConnexion("FERME");
            session.setDateDeconnexion(LocalDateTime.now());
            sessionRepository.save(session);
        });
    }

    public List<SessionMQTT> obtenirSessionsActives() {
        return sessionRepository.findByStatutConnexion("ACTIVE");
    }

    public List<SessionMQTT> obtenirSessionsParTopic(String topic) {
        return sessionRepository.findActiveSessionsByTopic(topic);
    }

    private void traiterMessageRecu(String topic, MqttMessage message) {
        try {
            String contenu = new String(message.getPayload());

            // Ici vous pouvez ajouter la logique de traitement des messages entrants
            // Par exemple, mettre à jour les statuts de notification, traiter les accusés de réception, etc.

        } catch (Exception e) {
        }
    }

    @PreDestroy
    public void fermer() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                mqttClient.close();
            }
        } catch (MqttException e) {
        }

    }
}
