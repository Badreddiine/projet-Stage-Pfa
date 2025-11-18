package com.sdsgpi.mqttservice.service;

import com.sdsgpi.mqttservice.entity.MQTTMessage;
import com.sdsgpi.mqttservice.entity.MQTTTopic;
import com.sdsgpi.mqttservice.repository.MQTTMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MQTTMessageService {

    @Autowired
    private MQTTMessageRepository mqttMessageRepository;
   @Autowired
   private MQTTTopicService mqttTopicService;
    public List<MQTTMessage> findAllMessages() {
        return mqttMessageRepository.findAll();
    }

    public Optional<MQTTMessage> findMessageById(Long id) {
        return mqttMessageRepository.findById(id);
    }

    public MQTTMessage saveMessage(MQTTMessage message) {
        return mqttMessageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        mqttMessageRepository.deleteById(id);
    }
    public void saveIncomingMessage(String topic, String payload) {
        MQTTMessage message = new MQTTMessage();
        message.setPayload(payload);
        message.setHorodatage(LocalDateTime.now());
        message.setQualiteService(1);
        message.setRetain(false);
        message.setTaille(payload.length());

        // Trouver ou créer le topic
        Optional<MQTTTopic> mqttTopic = mqttTopicService.findByNom(topic);
        if (mqttTopic.isPresent()) {
            message.setTopicId(mqttTopic.get().getId());
        }

        mqttMessageRepository.save(message);
    }
}


