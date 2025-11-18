package com.sdsgpi.mqttservice.repository;

import com.sdsgpi.mqttservice.entity.MQTTClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MQTTClientRepository extends JpaRepository<MQTTClient, Long> {
}


