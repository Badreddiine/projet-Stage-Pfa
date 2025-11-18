package com.sdsgpi.mqttservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MQTTClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientId;
    private String nom;
    private String type;
    private String status;
    private LocalDateTime derniereConnexion;
    private String adresseIP;
    private String version;
    private Integer keepAlive;
    private Boolean cleanSession;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mqtt_client_permissions", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "permission")
    private Set<String> permissions;
}


