package com.sdsgpi.analysisservice.controller;

import com.sdsgpi.analysisservice.dto.CreateMetriqueRequest;
import com.sdsgpi.analysisservice.dto.MetriqueKPIDto;
import com.sdsgpi.analysisservice.service.MetriqueKPIService;
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
 * Contrôleur REST pour la gestion des métriques KPI
 */
@RestController
@RequestMapping("/api/metriques")
@RequiredArgsConstructor
@Tag(name = "Métriques KPI", description = "API pour la gestion des métriques et indicateurs de performance")
public class MetriqueKPIController {

    private final MetriqueKPIService metriqueService;

    @Operation(summary = "Créer une nouvelle métrique KPI")
    @PostMapping
    public ResponseEntity<MetriqueKPIDto> createMetrique(
            @Valid @RequestBody CreateMetriqueRequest request) {
        MetriqueKPIDto metrique = metriqueService.createMetrique(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(metrique);
    }

    @Operation(summary = "Récupérer toutes les métriques actives")
    @GetMapping
    public ResponseEntity<List<MetriqueKPIDto>> getAllActiveMetrics() {
        List<MetriqueKPIDto> metriques = metriqueService.getAllActiveMetrics();
        return ResponseEntity.ok(metriques);
    }

    @Operation(summary = "Récupérer une métrique par ID")
    @GetMapping("/{id}")
    public ResponseEntity<MetriqueKPIDto> getMetriqueById(
            @Parameter(description = "ID de la métrique") @PathVariable Long id) {
        MetriqueKPIDto metrique = metriqueService.getMetriqueById(id);
        return ResponseEntity.ok(metrique);
    }

    @Operation(summary = "Récupérer une métrique par nom")
    @GetMapping("/name/{nomMetrique}")
    public ResponseEntity<MetriqueKPIDto> getMetriqueByName(
            @Parameter(description = "Nom de la métrique") @PathVariable String nomMetrique) {
        MetriqueKPIDto metrique = metriqueService.getMetriqueByName(nomMetrique);
        return ResponseEntity.ok(metrique);
    }

    @Operation(summary = "Récupérer les métriques par catégorie")
    @GetMapping("/category/{categorie}")
    public ResponseEntity<List<MetriqueKPIDto>> getMetriquesByCategory(
            @Parameter(description = "Catégorie de la métrique") @PathVariable String categorie) {
        List<MetriqueKPIDto> metriques = metriqueService.getMetriquesByCategory(categorie);
        return ResponseEntity.ok(metriques);
    }

    @Operation(summary = "Récupérer les métriques avec alerte")
    @GetMapping("/alerts")
    public ResponseEntity<List<MetriqueKPIDto>> getMetricsWithAlert() {
        List<MetriqueKPIDto> metriques = metriqueService.getMetricsWithAlert();
        return ResponseEntity.ok(metriques);
    }

    @Operation(summary = "Récupérer les métriques sous la cible")
    @GetMapping("/below-target")
    public ResponseEntity<List<MetriqueKPIDto>> getMetricsBelowTarget() {
        List<MetriqueKPIDto> metriques = metriqueService.getMetricsBelowTarget();
        return ResponseEntity.ok(metriques);
    }

    @Operation(summary = "Mettre à jour la valeur d'une métrique")
    @PutMapping("/{id}/value")
    public ResponseEntity<MetriqueKPIDto> updateMetriqueValue(
            @Parameter(description = "ID de la métrique") @PathVariable Long id,
            @Parameter(description = "Nouvelle valeur") @RequestParam Double nouvelleValeur) {
        MetriqueKPIDto metrique = metriqueService.updateMetriqueValue(id, nouvelleValeur);
        return ResponseEntity.ok(metrique);
    }

    @Operation(summary = "Mettre à jour la cible d'une métrique")
    @PutMapping("/{id}/target")
    public ResponseEntity<MetriqueKPIDto> updateMetriqueTarget(
            @Parameter(description = "ID de la métrique") @PathVariable Long id,
            @Parameter(description = "Nouvelle cible") @RequestParam Double nouvelleCible) {
        MetriqueKPIDto metrique = metriqueService.updateMetriqueTarget(id, nouvelleCible);
        return ResponseEntity.ok(metrique);
    }

    @Operation(summary = "Archiver une métrique")
    @PutMapping("/{id}/archive")
    public ResponseEntity<MetriqueKPIDto> archiveMetrique(
            @Parameter(description = "ID de la métrique") @PathVariable Long id) {
        MetriqueKPIDto metrique = metriqueService.archiveMetrique(id);
        return ResponseEntity.ok(metrique);
    }

    @Operation(summary = "Rechercher des métriques")
    @GetMapping("/search")
    public ResponseEntity<List<MetriqueKPIDto>> searchMetriques(
            @Parameter(description = "Terme de recherche") @RequestParam String searchTerm) {
        List<MetriqueKPIDto> metriques = metriqueService.searchMetriques(searchTerm);
        return ResponseEntity.ok(metriques);
    }

    @Operation(summary = "Récupérer les statistiques d'une métrique")
    @GetMapping("/{id}/stats")
    public ResponseEntity<MetriqueKPIService.MetriqueStatsDto> getMetriqueStats(
            @Parameter(description = "ID de la métrique") @PathVariable Long id) {
        MetriqueKPIService.MetriqueStatsDto stats = metriqueService.getMetriqueStats(id);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Supprimer une métrique")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetrique(
            @Parameter(description = "ID de la métrique") @PathVariable Long id) {
        metriqueService.deleteMetrique(id);
        return ResponseEntity.noContent().build();
    }
}

