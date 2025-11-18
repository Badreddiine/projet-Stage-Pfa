package com.sdsgpi.analysisservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'une métrique KPI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMetriqueRequest {

    @NotBlank(message = "Le nom de la métrique est obligatoire")
    private String nomMetrique;

    @NotBlank(message = "La catégorie est obligatoire")
    private String categorie;

    @NotNull(message = "La valeur actuelle est obligatoire")
    private Double valeurActuelle;

    private Double valeurCible;
    private Double valeurPrecedente;

    @NotBlank(message = "L'unité est obligatoire")
    private String unite;

    @NotBlank(message = "La période de calcul est obligatoire")
    private String periodeCalcul;

    private Double seuilAlerte;
}

