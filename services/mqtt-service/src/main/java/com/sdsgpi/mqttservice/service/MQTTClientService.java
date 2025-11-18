package com.sdsgpi.mqttservice.service;

import com.sdsgpi.mqttservice.entity.MQTTClient;
import com.sdsgpi.mqttservice.repository.MQTTClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MQTTClientService {

    @Autowired
    private MQTTClientRepository mqttClientRepository;

    public List<MQTTClient> findAllClients() {
        return mqttClientRepository.findAll();
    }

    public Optional<MQTTClient> findClientById(Long id) {
        return mqttClientRepository.findById(id);
    }

    public MQTTClient saveClient(MQTTClient client) {
        return mqttClientRepository.save(client);
    }

    public void deleteClient(Long id) {
        mqttClientRepository.deleteById(id);
    }
}


