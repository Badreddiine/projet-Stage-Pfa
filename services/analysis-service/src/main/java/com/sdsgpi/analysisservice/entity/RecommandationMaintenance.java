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
 * Entité RecommandationMaintenance pour stocker les recommandations de maintenance
 */
@Entity
@Table(name = "recommandations_maintenance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommandationMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "type_recommandation", nullable = false)
    private String typeRecommandation;

    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "priorite", nullable = false)
    @Enumerated(EnumType.STRING)
    private PrioriteRecommandation priorite;

    @Column(name = "date_suggere")
    private LocalDateTime dateSuggere;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "score_confiance")
    private Double scoreConfiance;

    @Column(name = "cout_estime")
    private Double coutEstime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusRecommandation status = StatusRecommandation.EN_ATTENTE;

    @Column(name = "approuve_par")
    private String approuvePar;

    @ElementCollection
    @CollectionTable(name = "recommandation_metadonnees", joinColumns = @JoinColumn(name = "recommandation_id"))
    @MapKeyColumn(name = "cle")
    @Column(name = "valeur", columnDefinition = "TEXT") // CORRECTION ICI
    private Map<String, String> metadonnees; // CORRECTION ICI : Object -> String

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum PrioriteRecommandation {
        FAIBLE, MOYENNE, HAUTE, CRITIQUE
    }

    public enum StatusRecommandation {
        EN_ATTENTE, APPROUVE, REJETE, EN_COURS, TERMINE
    }
}
