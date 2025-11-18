package com.sdsgpi.analysisservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entité RapportAnalyse pour stocker les rapports d'analyse
 */
@Entity
@Table(name = "rapports_analyse")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RapportAnalyse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_rapport", nullable = false)
    private String nomRapport;

    @Column(name = "type_rapport", nullable = false)
    private String typeRapport;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_generation", nullable = false)
    private LocalDateTime dateGeneration;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "fin_periode", nullable = false)
    private LocalDateTime finPeriode;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusRapport status = StatusRapport.EN_COURS;

    @Column(name = "format", nullable = false)
    private String format;

    @ElementCollection
    @CollectionTable(name = "rapport_donnees", joinColumns = @JoinColumn(name = "rapport_id"))
    @MapKeyColumn(name = "cle")
    @Column(name = "valeur", columnDefinition = "TEXT") // CORRECTION ICI
    private Map<String, String> donnees; // CORRECTION ICI : Object -> String

    @Column(name = "contenu", columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "genere_par", nullable = false)
    private String generePar;

    @Column(name = "taille_octets")
    private Long tailleOctets;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum StatusRapport {
        EN_COURS, TERMINE, ERREUR, ANNULE
    }
}
