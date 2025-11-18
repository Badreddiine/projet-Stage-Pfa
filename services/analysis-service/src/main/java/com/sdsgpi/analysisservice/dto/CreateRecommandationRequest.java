package com.sdsgpi.analysisservice.dto;

import com.sdsgpi.analysisservice.entity.RecommandationMaintenance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO pour la création d'une recommandation de maintenance
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecommandationRequest {

    @NotNull(message = "L'ID de l'équipement est obligatoire")
    private Long equipmentId;

    @NotBlank(message = "Le type de recommandation est obligatoire")
    private String typeRecommandation;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotNull(message = "La priorité est obligatoire")
    private RecommandationMaintenance.PrioriteRecommandation priorite;

    private LocalDateTime dateSuggere;

    @Positive(message = "Le score de confiance doit être positif")
    private Double scoreConfiance;

    @Positive(message = "Le coût estimé doit être positif")
    private Double coutEstime;

    private Map<String, String> metadonnees;
}

