package com.sdsgpi.analysisservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sdsgpi.analysisservice.entity.RecommandationMaintenance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO pour les recommandations de maintenance
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommandationMaintenanceDto {

    private Long id;
    private Long equipmentId;
    private String typeRecommandation;
    private String titre;
    private String description;
    private RecommandationMaintenance.PrioriteRecommandation priorite;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateSuggere;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreation;
    
    private Double scoreConfiance;
    private Double coutEstime;
    private RecommandationMaintenance.StatusRecommandation status;
    private String approuvePar;
    private Map<String, String> metadonnees;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}

