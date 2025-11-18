package com.incident.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gestionnaire_sla")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GestionnaireSLA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "severite", nullable = false)
    private String severite;

    @Column(name = "temps_response_minutes", nullable = false)
    private Integer tempsResponseMinutes;

    @Column(name = "temps_resolution_minutes", nullable = false)
    private Integer tempsResolutionMinutes;

    @Column(name = "heures_affaires", nullable = false)
    private String heuresAffaires;

    @Column(name = "taux_penalite")
    private Double tauxPenalite;
}

