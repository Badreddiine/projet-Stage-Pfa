package com.incident.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIncidentRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotBlank(message = "La sévérité est obligatoire")
    private String severite;

    private String priorite;

    @NotNull(message = "L'ID de l'équipement est obligatoire")
    private Long equipementId;

    private Long utilisateurAssigneId;
}

