package com.sdsgpi.analysisservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité MetriqueKPI pour stocker les métriques et indicateurs de performance
 */
@Entity
@Table(name = "metriques_kpi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetriqueKPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_metrique", nullable = false)
    private String nomMetrique;

    @Column(name = "categorie", nullable = false)
    private String categorie;

    @Column(name = "valeur_actuelle")
    private Double valeurActuelle;

    @Column(name = "valeur_cible")
    private Double valeurCible;

    @Column(name = "valeur_precedente")
    private Double valeurPrecedente;

    @Column(name = "unite", nullable = false)
    private String unite;

    @Column(name = "date_calcul", nullable = false)
    private LocalDateTime dateCalcul;

    @Column(name = "periode_calcul", nullable = false)
    private String periodeCalcul;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusMetrique status = StatusMetrique.ACTIF;

    @Column(name = "seuil_alerte")
    private Double seuilAlerte;

    @ElementCollection
    @CollectionTable(name = "metrique_historique", joinColumns = @JoinColumn(name = "metrique_id"))
    @Column(name = "valeur")
    private List<Double> historique;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum StatusMetrique {
        ACTIF, INACTIF, ARCHIVE
    }

    /**
     * Calcule la tendance par rapport à la valeur précédente
     */
    public TendanceMetrique getTendance() {
        if (valeurPrecedente == null || valeurActuelle == null) {
            return TendanceMetrique.STABLE;
        }
        
        double difference = valeurActuelle - valeurPrecedente;
        double pourcentage = Math.abs(difference / valeurPrecedente) * 100;
        
        if (pourcentage < 1.0) {
            return TendanceMetrique.STABLE;
        } else if (difference > 0) {
            return TendanceMetrique.HAUSSE;
        } else {
            return TendanceMetrique.BAISSE;
        }
    }

    public enum TendanceMetrique {
        HAUSSE, BAISSE, STABLE
    }
}

