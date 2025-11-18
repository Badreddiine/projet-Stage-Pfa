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
 * Entité ModelePredictif pour stocker les modèles prédictifs et leurs métadonnées
 */
@Entity
@Table(name = "modeles_predictifs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelePredictif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_modele", nullable = false)
    private String nomModele;

    @Column(name = "type_modele", nullable = false)
    private String typeModele;

    @Column(name = "algorithme", nullable = false)
    private String algorithme;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "precision")
    private Double precision;

    @Column(name = "date_entrainement", nullable = false)
    private LocalDateTime dateEntrainement;

    @Column(name = "derniere_utilisation")
    private LocalDateTime derniereUtilisation;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusModele status = StatusModele.ENTRAINE;

    @ElementCollection
    @CollectionTable(name = "modele_parametres", joinColumns = @JoinColumn(name = "modele_id"))
    @MapKeyColumn(name = "nom_parametre")
    @Column(name = "valeur_parametre", columnDefinition = "TEXT") // CORRECTION ICI
    private Map<String, String> parametres; // CORRECTION ICI : Object -> String

    @Column(name = "chemin_fichier")
    private String cheminFichier;

    @Column(name = "donnees_entrainement", columnDefinition = "TEXT")
    private String donneesEntrainement;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum StatusModele {
        EN_ENTRAINEMENT, ENTRAINE, DEPLOYE, ARCHIVE, ERREUR
    }

    /**
     * Vérifie si le modèle est prêt à être utilisé
     */
    public boolean isReady() {
        return status == StatusModele.ENTRAINE || status == StatusModele.DEPLOYE;
    }

    /**
     * Met à jour la dernière utilisation
     */
    public void updateLastUsage() {
        this.derniereUtilisation = LocalDateTime.now();
    }
}
