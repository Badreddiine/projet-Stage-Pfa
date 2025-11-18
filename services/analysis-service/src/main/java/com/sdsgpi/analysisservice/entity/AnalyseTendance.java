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
 * Entité AnalyseTendance pour stocker les analyses de tendance
 */
@Entity
@Table(name = "analyses_tendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyseTendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_analyse", nullable = false)
    private String typeAnalyse;

    @Column(name = "type_equipement")
    private String typeEquipement;

    @Column(name = "metrique", nullable = false)
    private String metrique;

    @Column(name = "debut_periode", nullable = false)
    private LocalDateTime debutPeriode;

    @Column(name = "fin_periode", nullable = false)
    private LocalDateTime finPeriode;

    @Column(name = "pente")
    private Double pente;

    @Column(name = "correlation")
    private Double correlation;

    @Column(name = "tendance", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeTendance tendance;

    @Column(name = "interpretation", columnDefinition = "TEXT")
    private String interpretation;

    @ElementCollection
    @CollectionTable(name = "analyse_donnees_statistiques", joinColumns = @JoinColumn(name = "analyse_id"))
    @MapKeyColumn(name = "statistique")
    @Column(name = "valeur", columnDefinition = "TEXT") // CORRECTION ICI : Ajout de columnDefinition et changement de type
    private Map<String, String> donneesStatistiques; // CORRECTION ICI : Changement de Object à String

    @Column(name = "niveau_confiance")
    private Double niveauConfiance;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TypeTendance {
        CROISSANTE, DECROISSANTE, STABLE, CYCLIQUE, VOLATILE
    }

    /**
     * Détermine la force de la tendance basée sur la corrélation
     */
    public ForceTendance getForceTendance() {
        if (correlation == null) {
            return ForceTendance.INDETERMINEE;
        }

        double absCorrelation = Math.abs(correlation);

        if (absCorrelation >= 0.8) {
            return ForceTendance.TRES_FORTE;
        } else if (absCorrelation >= 0.6) {
            return ForceTendance.FORTE;
        } else if (absCorrelation >= 0.4) {
            return ForceTendance.MODEREE;
        } else if (absCorrelation >= 0.2) {
            return ForceTendance.FAIBLE;
        } else {
            return ForceTendance.TRES_FAIBLE;
        }
    }

    public enum ForceTendance {
        TRES_FORTE, FORTE, MODEREE, FAIBLE, TRES_FAIBLE, INDETERMINEE
    }
}
