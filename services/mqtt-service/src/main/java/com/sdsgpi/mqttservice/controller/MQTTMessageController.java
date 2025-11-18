package com.sdsgpi.mqttservice.controller;

import com.sdsgpi.mqttservice.entity.MQTTMessage;
import com.sdsgpi.mqttservice.service.MQTTMessageService;
import com.sdsgpi.mqttservice.service.MqttGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mqtt/messages")
public class MQTTMessageController {

    @Autowired
    private MQTTMessageService mqttMessageService;

    @GetMapping
    public List<MQTTMessage> getAllMessages() {
        return mqttMessageService.findAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MQTTMessage> getMessageById(@PathVariable Long id) {
        return mqttMessageService.findMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Autowired
    private MqttGatewayService mqttGateway;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(
            @RequestParam String topic,
            @RequestParam String message,
            @RequestParam(defaultValue = "1") int qos) {
        try {
            mqttGateway.sendToMqtt(message, topic, qos);
            return ResponseEntity.ok("Message publié avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la publication: " + e.getMessage());
        }
    }
    @PostMapping
    public MQTTMessage createMessage(@RequestBody MQTTMessage message) {
        return mqttMessageService.saveMessage(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MQTTMessage> updateMessage(@PathVariable Long id, @RequestBody MQTTMessage message) {
        return mqttMessageService.findMessageById(id)
                .map(existingMessage -> {
                    message.setId(id);
                    return ResponseEntity.ok(mqttMessageService.saveMessage(message));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        mqttMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}


