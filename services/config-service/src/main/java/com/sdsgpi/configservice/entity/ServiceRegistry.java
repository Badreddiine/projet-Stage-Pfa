package com.sdsgpi.configservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomService;
    private String adresseIP;
    private Integer port;
    private String protocole;
    private String version;
    private LocalDateTime derniereVerification;

    @ElementCollection
    @CollectionTable(name = "service_endpoints", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "endpoint_url")
    private List<String> endpoints;

    @ElementCollection
    @CollectionTable(name = "service_configurations", joinColumns = @JoinColumn(name = "service_id"))
    @MapKeyColumn(name = "config_key")
    @Column(name = "config_value")
    private Map<String, String> configuration;

    private String healthCheckUri;

    @ElementCollection
    @CollectionTable(name = "service_dependencies", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "dependency_name")
    private Set<String> dependances;
}


