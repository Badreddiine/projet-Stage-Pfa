package com.sdsgpi.configservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationSysteme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String cle;
    private String valeur;
    private String type;
    private String description;
    private Boolean estModifiable;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long modifiePar;
    private String version;
    private String environnement;
}


