package com.sdsgpi.mqttservice.controller;

import com.sdsgpi.mqttservice.entity.MQTTClient;
import com.sdsgpi.mqttservice.service.MQTTClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mqtt/clients")
public class MQTTClientController {

    @Autowired
    private MQTTClientService mqttClientService;

    @GetMapping
    public List<MQTTClient> getAllClients() {
        return mqttClientService.findAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MQTTClient> getClientById(@PathVariable Long id) {
        return mqttClientService.findClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MQTTClient createClient(@RequestBody MQTTClient client) {
        return mqttClientService.saveClient(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MQTTClient> updateClient(@PathVariable Long id, @RequestBody MQTTClient client) {
        return mqttClientService.findClientById(id)
                .map(existingClient -> {
                    client.setId(id);
                    return ResponseEntity.ok(mqttClientService.saveClient(client));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        mqttClientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}


