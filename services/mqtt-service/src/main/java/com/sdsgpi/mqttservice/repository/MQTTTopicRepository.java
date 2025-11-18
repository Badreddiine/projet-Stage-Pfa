package com.sdsgpi.mqttservice.repository;

import com.sdsgpi.mqttservice.entity.MQTTTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MQTTTopicRepository extends JpaRepository<MQTTTopic, Long> {
    Optional<MQTTTopic> findByNom(String nom);
}


