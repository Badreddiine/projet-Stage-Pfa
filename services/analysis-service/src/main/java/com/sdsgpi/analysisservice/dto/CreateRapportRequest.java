package com.sdsgpi.analysisservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO pour la création d'un rapport d'analyse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRapportRequest {

    @NotBlank(message = "Le nom du rapport est obligatoire")
    private String nomRapport;

    @NotBlank(message = "Le type de rapport est obligatoire")
    private String typeRapport;

    private String description;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime finPeriode;

    @NotBlank(message = "Le format est obligatoire")
    private String format;

    private Map<String, String> donnees;
}

