package com.sdsgpi.mqttservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MQTTTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String pattern;
    private String description;
    private String niveauAcces;
    private Boolean estActif;
    private Boolean retainMessage;
    private Integer qualiteService;

    @ElementCollection
    @CollectionTable(name = "mqtt_topic_statistics", joinColumns = @JoinColumn(name = "topic_id"))
    @MapKeyColumn(name = "stat_key")
    @Column(name = "stat_value")
    private Map<String, String> statistiques; // Changed to String for simplicity, can be adapted to JSON or other types
}


