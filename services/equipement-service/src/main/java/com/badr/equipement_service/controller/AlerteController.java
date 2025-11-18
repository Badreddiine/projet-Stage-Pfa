package com.badr.equipement_service.controller;

import com.badr.equipement_service.dto.Alertedto;
import com.badr.equipement_service.entity.Alerte;
import com.badr.equipement_service.service.AlerteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alertes")
// Suppression de @CrossOrigin - le Gateway gère les CORS
public class AlerteController {

    private final AlerteService alerteService;

    public AlerteController(AlerteService alerteService) {
        this.alerteService = alerteService;
    }

    @GetMapping
    public ResponseEntity<List<Alertedto>> getAllAlertes() {
        List<Alertedto> alertes = alerteService.getAllAlertes();
        return ResponseEntity.ok(alertes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alertedto> getAlerteById(@PathVariable Long id) {
        return alerteService.getAlerteById(id)
                .map(alerte -> ResponseEntity.ok(alerte))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Alerte> createAlerte(@Valid @RequestBody Alertedto alerte) {
        Alerte createdAlerte = alerteService.createAlerte(alerte);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlerte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alerte> updateAlerte(@Valid @RequestBody Alertedto alerteDetails) {
        try {
            Alerte updatedAlerte = alerteService.updateAlerte(alerteDetails);
            return ResponseEntity.ok(updatedAlerte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlerte(@PathVariable Long id) {
        alerteService.deleteAlerte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/equipement/{equipementId}")
    public ResponseEntity<List<Alertedto>> getAlertesByEquipement(@PathVariable Long equipementId) {
        List<Alertedto> alertes = alerteService.getAlertesByEquipement(equipementId);
        return ResponseEntity.ok(alertes);
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Alertedto>> getAlertesByStatut(@PathVariable String statut) {
        List<Alertedto> alertes = alerteService.getAlertesByStatut(statut);
        return ResponseEntity.ok(alertes);
    }

    @GetMapping("/severite/{severite}")
    public ResponseEntity<List<Alertedto>> getAlertesBySeverite(@PathVariable String severite) {
        List<Alertedto> alertes = alerteService.getAlertesBySeverite(severite);
        return ResponseEntity.ok(alertes);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Alertedto>> getActiveAlertes() {
        List<Alertedto> alertes = alerteService.getActiveAlertes();
        return ResponseEntity.ok(alertes);
    }

    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<Alerte> acknowledgeAlerte(@PathVariable Long id) {
        try {
            Alerte acknowledgedAlerte = alerteService.acknowledgeAlerte(id);
            return ResponseEntity.ok(acknowledgedAlerte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<Alerte> resolveAlerte(@PathVariable Long id) {
        try {
            Alerte resolvedAlerte = alerteService.resolveAlerte(id);
            return ResponseEntity.ok(resolvedAlerte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Alertedto>> getAlertesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        List<Alertedto> alertes = alerteService.getAlertesByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(alertes);
    }

    @GetMapping("/count/equipement/{equipementId}/active")
    public ResponseEntity<Long> getActiveAlertesCountByEquipement(@PathVariable Long equipementId) {
        Long count = alerteService.getActiveAlertesCountByEquipement(equipementId);
        return ResponseEntity.ok(count);
    }
}

