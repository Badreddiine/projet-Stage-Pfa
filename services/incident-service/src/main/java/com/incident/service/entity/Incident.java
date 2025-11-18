package com.incident.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "incident")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "severite", nullable = false)
    private String severite;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "priorite")
    private String priorite;

    @Column(name = "equipement_id")
    private Long equipementId;

    @Column(name = "utilisateur_assigne_id")
    private Long utilisateurAssigneId;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_resolution")
    private LocalDateTime dateResolution;

    @Column(name = "date_echeance")
    private LocalDateTime dateEcheance;



    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EtapeFluxTravail> etapesFluxTravail;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegleEscalade> reglesEscalade;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        if (statut == null) {
            statut = "NOUVEAU";
        }
    }
}

