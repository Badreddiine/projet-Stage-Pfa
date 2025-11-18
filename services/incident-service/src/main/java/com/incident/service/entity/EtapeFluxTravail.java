package com.incident.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "etape_flux_travail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtapeFluxTravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_id", nullable = false)
    private Long incidentId;

    @Column(name = "nom_etape", nullable = false)
    private String nomEtape;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "date_finalisee")
    private LocalDateTime dateFinalisee;

    @Column(name = "execute_par")
    private String executePar;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "ordre_etape")
    private Integer ordreEtape;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", insertable = false, updatable = false)
    private Incident incident;

    @PrePersist
    protected void onCreate() {
        if (dateDebut == null) {
            dateDebut = LocalDateTime.now();
        }
        if (statut == null) {
            statut = "EN_ATTENTE";
        }
    }
}

