package com.sdsgpi.analysisservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sdsgpi.analysisservice.entity.MetriqueKPI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les métriques KPI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetriqueKPIDto {

    private Long id;
    private String nomMetrique;
    private String categorie;
    private Double valeurActuelle;
    private Double valeurCible;
    private Double valeurPrecedente;
    private String unite;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCalcul;
    
    private String periodeCalcul;
    private MetriqueKPI.StatusMetrique status;
    private Double seuilAlerte;
    private List<Double> historique;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Champs calculés
    private MetriqueKPI.TendanceMetrique tendance;
    private Double pourcentageAtteinteCible;
    private Boolean alerteActive;

    /**
     * Calcule le pourcentage d'atteinte de la cible
     */
    public Double getPourcentageAtteinteCible() {
        if (valeurCible == null || valeurActuelle == null || valeurCible == 0) {
            return null;
        }
        return (valeurActuelle / valeurCible) * 100;
    }

    /**
     * Vérifie si une alerte est active
     */
    public Boolean getAlerteActive() {
        if (seuilAlerte == null || valeurActuelle == null) {
            return false;
        }
        return valeurActuelle > seuilAlerte;
    }
}

