package com.sdsgpi.analysisservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sdsgpi.analysisservice.entity.RapportAnalyse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO pour les rapports d'analyse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RapportAnalyseDto {

    private Long id;
    private String nomRapport;
    private String typeRapport;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateGeneration;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateDebut;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finPeriode;
    
    private RapportAnalyse.StatusRapport status;
    private String format;
    private Map<String, String> donnees;
    private String contenu;
    private String generePar;
    private Long tailleOctets;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}

