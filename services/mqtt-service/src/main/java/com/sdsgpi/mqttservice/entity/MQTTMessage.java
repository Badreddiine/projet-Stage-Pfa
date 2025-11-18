package com.sdsgpi.mqttservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MQTTMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long topicId;
    @Lob // For large text/binary data
    private String payload;
    private LocalDateTime horodatage;
    private Integer qualiteService;
    private Boolean retain;
    private String clientId;
    private Integer taille;
    private String checksum;
}


