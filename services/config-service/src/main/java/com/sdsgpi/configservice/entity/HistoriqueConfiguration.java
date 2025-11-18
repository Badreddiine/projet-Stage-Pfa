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
public class HistoriqueConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long configurationId;
    private String ancienneValeur;
    private String nouvelleValeur;
    private Long utilisateurId;
    private LocalDateTime dateModification;
    private String raisonModification;
}


