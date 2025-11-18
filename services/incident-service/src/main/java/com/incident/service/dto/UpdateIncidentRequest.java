package com.incident.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIncidentRequest {

    private String titre;
    private String description;
    private String severite;
    private String statut;
    private String priorite;
    private Long utilisateurAssigneId;
    private LocalDateTime dateResolution;
    private LocalDateTime dateEcheance;
}

