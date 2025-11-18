package com.sdsgpi.mqttservice.handler;

import com.sdsgpi.mqttservice.service.MQTTMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqttMessageHandler {

    private final MQTTMessageService mqttMessageService;

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        try {
            String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
            String payload = message.getPayload().toString();

            log.info("Message reçu sur le topic: {} avec payload: {}", topic, payload);

            // Sauvegarder le message en base de données
            mqttMessageService.saveIncomingMessage(topic, payload);

        } catch (Exception e) {
            log.error("Erreur lors du traitement du message MQTT: {}", e.getMessage());
        }
    }
}