package com.sdsgpi.analysisservice.controller;

import com.sdsgpi.analysisservice.dto.CreateRecommandationRequest;
import com.sdsgpi.analysisservice.dto.RecommandationMaintenanceDto;
import com.sdsgpi.analysisservice.entity.RecommandationMaintenance;
import com.sdsgpi.analysisservice.service.RecommandationMaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des recommandations de maintenance
 */
@RestController
@RequestMapping("/api/recommandations")
@RequiredArgsConstructor
@Tag(name = "Recommandations de Maintenance", description = "API pour la gestion des recommandations de maintenance")
public class RecommandationMaintenanceController {

    private final RecommandationMaintenanceService recommandationService;

    @Operation(summary = "Créer une nouvelle recommandation de maintenance")
    @PostMapping
    public ResponseEntity<RecommandationMaintenanceDto> createRecommandation(
            @Valid @RequestBody CreateRecommandationRequest request) {
        RecommandationMaintenanceDto recommandation = recommandationService.createRecommandation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(recommandation);
    }

    @Operation(summary = "Récupérer toutes les recommandations")
    @GetMapping
    public ResponseEntity<List<RecommandationMaintenanceDto>> getAllRecommandations() {
        List<RecommandationMaintenanceDto> recommandations = recommandationService.getAllRecommandations();
        return ResponseEntity.ok(recommandations);
    }

    @Operation(summary = "Récupérer une recommandation par ID")
    @GetMapping("/{id}")
    public ResponseEntity<RecommandationMaintenanceDto> getRecommandationById(
            @Parameter(description = "ID de la recommandation") @PathVariable Long id) {
        RecommandationMaintenanceDto recommandation = recommandationService.getRecommandationById(id);
        return ResponseEntity.ok(recommandation);
    }

    @Operation(summary = "Récupérer les recommandations par équipement")
    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<RecommandationMaintenanceDto>> getRecommandationsByEquipment(
            @Parameter(description = "ID de l'équipement") @PathVariable Long equipmentId) {
        List<RecommandationMaintenanceDto> recommandations = 
            recommandationService.getRecommandationsByEquipment(equipmentId);
        return ResponseEntity.ok(recommandations);
    }

    @Operation(summary = "Récupérer les recommandations urgentes")
    @GetMapping("/urgent")
    public ResponseEntity<List<RecommandationMaintenanceDto>> getUrgentRecommandations() {
        List<RecommandationMaintenanceDto> recommandations = recommandationService.getUrgentRecommandations();
        return ResponseEntity.ok(recommandations);
    }

    @Operation(summary = "Récupérer les recommandations par statut")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RecommandationMaintenanceDto>> getRecommandationsByStatus(
            @Parameter(description = "Statut de la recommandation") 
            @PathVariable RecommandationMaintenance.StatusRecommandation status) {
        List<RecommandationMaintenanceDto> recommandations = 
            recommandationService.getRecommandationsByStatus(status);
        return ResponseEntity.ok(recommandations);
    }

    @Operation(summary = "Approuver une recommandation")
    @PutMapping("/{id}/approve")
    public ResponseEntity<RecommandationMaintenanceDto> approveRecommandation(
            @Parameter(description = "ID de la recommandation") @PathVariable Long id,
            @Parameter(description = "Utilisateur qui approuve") @RequestParam String approvedBy) {
        RecommandationMaintenanceDto recommandation = 
            recommandationService.approveRecommandation(id, approvedBy);
        return ResponseEntity.ok(recommandation);
    }

    @Operation(summary = "Rejeter une recommandation")
    @PutMapping("/{id}/reject")
    public ResponseEntity<RecommandationMaintenanceDto> rejectRecommandation(
            @Parameter(description = "ID de la recommandation") @PathVariable Long id,
            @Parameter(description = "Utilisateur qui rejette") @RequestParam String rejectedBy) {
        RecommandationMaintenanceDto recommandation = 
            recommandationService.rejectRecommandation(id, rejectedBy);
        return ResponseEntity.ok(recommandation);
    }

    @Operation(summary = "Mettre à jour le statut d'une recommandation")
    @PutMapping("/{id}/status")
    public ResponseEntity<RecommandationMaintenanceDto> updateStatus(
            @Parameter(description = "ID de la recommandation") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") 
            @RequestParam RecommandationMaintenance.StatusRecommandation status) {
        RecommandationMaintenanceDto recommandation = recommandationService.updateStatus(id, status);
        return ResponseEntity.ok(recommandation);
    }

    @Operation(summary = "Rechercher des recommandations")
    @GetMapping("/search")
    public ResponseEntity<List<RecommandationMaintenanceDto>> searchRecommandations(
            @Parameter(description = "Terme de recherche") @RequestParam String searchTerm) {
        List<RecommandationMaintenanceDto> recommandations = 
            recommandationService.searchRecommandations(searchTerm);
        return ResponseEntity.ok(recommandations);
    }

    @Operation(summary = "Supprimer une recommandation")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommandation(
            @Parameter(description = "ID de la recommandation") @PathVariable Long id) {
        recommandationService.deleteRecommandation(id);
        return ResponseEntity.noContent().build();
    }
}

