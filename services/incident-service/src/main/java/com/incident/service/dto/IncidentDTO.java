package com.incident.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDTO {

    private Long id;
    private String titre;
    private String description;
    private String severite;
    private String statut;
    private String priorite;
    private Long equipementId;
    private Long utilisateurAssigneId;
    private LocalDateTime dateCreation;
    private LocalDateTime dateResolution;
    private LocalDateTime dateEcheance;
    private String nomUtilisateurAssigne;
}

