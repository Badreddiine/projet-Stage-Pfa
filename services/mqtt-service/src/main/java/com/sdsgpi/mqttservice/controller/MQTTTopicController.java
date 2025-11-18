package com.sdsgpi.mqttservice.controller;

import com.sdsgpi.mqttservice.entity.MQTTTopic;
import com.sdsgpi.mqttservice.service.MQTTTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mqtt/topics")
public class MQTTTopicController {

    @Autowired
    private MQTTTopicService mqttTopicService;

    @GetMapping
    public List<MQTTTopic> getAllTopics() {
        return mqttTopicService.findAllTopics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MQTTTopic> getTopicById(@PathVariable Long id) {
        return mqttTopicService.findTopicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MQTTTopic createTopic(@RequestBody MQTTTopic topic) {
        return mqttTopicService.saveTopic(topic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MQTTTopic> updateTopic(@PathVariable Long id, @RequestBody MQTTTopic topic) {
        return mqttTopicService.findTopicById(id)
                .map(existingTopic -> {
                    topic.setId(id);
                    return ResponseEntity.ok(mqttTopicService.saveTopic(topic));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        mqttTopicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}


