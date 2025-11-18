package com.incident.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regle_escalade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegleEscalade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "severite", nullable = false)
    private String severite;

    @Column(name = "niveau_escalade", nullable = false)
    private Integer niveauEscalade;

    @Column(name = "delai_minutes", nullable = false)
    private Integer delaiMinutes;

    @Column(name = "role_cible", nullable = false)
    private String roleCible;

    @Column(name = "methode_notification", nullable = false)
    private String methodeNotification;

    @Column(name = "est_active")
    private Boolean estActive;

    @Column(name = "conditions", columnDefinition = "TEXT")
    private String conditions;

    @Column(name = "incident_id")
    private Long incidentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", insertable = false, updatable = false)
    private Incident incident;

    @PrePersist
    protected void onCreate() {
        if (estActive == null) {
            estActive = true;
        }
    }
}

