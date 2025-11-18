package com.sdsgpi.mqttservice.service;

import com.sdsgpi.mqttservice.entity.MQTTTopic;
import com.sdsgpi.mqttservice.repository.MQTTTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MQTTTopicService {

    @Autowired
    private MQTTTopicRepository mqttTopicRepository;

    public List<MQTTTopic> findAllTopics() {
        return mqttTopicRepository.findAll();
    }

    public Optional<MQTTTopic> findTopicById(Long id) {
        return mqttTopicRepository.findById(id);
    }

    public MQTTTopic saveTopic(MQTTTopic topic) {
        return mqttTopicRepository.save(topic);
    }

    public void deleteTopic(Long id) {
        mqttTopicRepository.deleteById(id);
    }
    public Optional<MQTTTopic> findByNom(String nom) {
        return mqttTopicRepository.findByNom(nom);
    }
}


