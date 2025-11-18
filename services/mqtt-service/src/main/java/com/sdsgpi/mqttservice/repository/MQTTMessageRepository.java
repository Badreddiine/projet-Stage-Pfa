package com.sdsgpi.mqttservice.repository;

import com.sdsgpi.mqttservice.entity.MQTTMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MQTTMessageRepository extends JpaRepository<MQTTMessage, Long> {
}


