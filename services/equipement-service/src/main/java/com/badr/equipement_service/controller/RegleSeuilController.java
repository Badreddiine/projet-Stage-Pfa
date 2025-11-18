package com.badr.equipement_service.controller;

import com.badr.equipement_service.dto.RegleSeuildto;
import com.badr.equipement_service.entity.RegleSeuil;
import com.badr.equipement_service.service.RegleSeuilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regles-seuil")
// Suppression de @CrossOrigin - le Gateway gère les CORS
public class RegleSeuilController {

    private final RegleSeuilService regleSeuilService;

    public RegleSeuilController(RegleSeuilService regleSeuilService) {
        this.regleSeuilService = regleSeuilService;
    }

    @GetMapping
    public ResponseEntity<List<RegleSeuildto>> getAllReglesSeui() {
        List<RegleSeuildto> regles = regleSeuilService.getAllReglesSeui();
        return ResponseEntity.ok(regles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegleSeuildto> getRegleSeuilById(@PathVariable Long id) {
        return regleSeuilService.getRegleSeuilById(id)
                .map(regle -> ResponseEntity.ok(regle))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RegleSeuil> createRegleSeuil(@Valid @RequestBody RegleSeuildto regleSeuil) {
        RegleSeuil createdRegle = regleSeuilService.createRegleSeuil(regleSeuil);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegleSeuil> updateRegleSeuil(@Valid @RequestBody RegleSeuildto regleDetails) {
        Long id = regleDetails.getIdentifiant();
        try {
            RegleSeuil updatedRegle = regleSeuilService.updateRegleSeuil(regleDetails);
            return ResponseEntity.ok(updatedRegle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegleSeuil(@PathVariable Long id) {
        regleSeuilService.deleteRegleSeuil(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/equipement/{equipementId}")
    public ResponseEntity<List<RegleSeuildto>> getReglesByEquipement(@PathVariable Long equipementId) {
        List<RegleSeuildto> regles = regleSeuilService.getReglesByEquipement(equipementId);
        return ResponseEntity.ok(regles);
    }

    @GetMapping("/equipement/{equipementId}/active")
    public ResponseEntity<List<RegleSeuildto>> getActiveReglesByEquipement(@PathVariable Long equipementId) {
        List<RegleSeuildto> regles = regleSeuilService.getActiveReglesByEquipement(equipementId);
        return ResponseEntity.ok(regles);
    }

    @GetMapping("/parametre/{parametre}")
    public ResponseEntity<List<RegleSeuildto>> getReglesByParametre(@PathVariable String parametre) {
        List<RegleSeuildto> regles = regleSeuilService.getReglesByParametre(parametre);
        return ResponseEntity.ok(regles);
    }

    @GetMapping("/niveau-alerte/{niveauAlerte}")
    public ResponseEntity<List<RegleSeuildto>> getReglesByNiveauAlerte(@PathVariable String niveauAlerte) {
        List<RegleSeuildto> regles = regleSeuilService.getReglesByNiveauAlerte(niveauAlerte);
        return ResponseEntity.ok(regles);
    }

    @GetMapping("/equipement/{equipementId}/parametre/{parametre}/active")
    public ResponseEntity<List<RegleSeuildto>> getActiveReglesByEquipementAndParametre(
            @PathVariable Long equipementId,
            @PathVariable String parametre) {
        List<RegleSeuildto> regles = regleSeuilService.getActiveReglesByEquipementAndParametre(equipementId, parametre);
        return ResponseEntity.ok(regles);
    }

    @GetMapping("/violated")
    public ResponseEntity<List<RegleSeuildto>> checkViolatedRules(@RequestParam Double valeur) {
        List<RegleSeuildto> regles = regleSeuilService.checkViolatedRules(valeur);
        return ResponseEntity.ok(regles);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<RegleSeuil> activateRegle(@PathVariable Long id) {
        try {
            RegleSeuil activatedRegle = regleSeuilService.activateRegle(id);
            return ResponseEntity.ok(activatedRegle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<RegleSeuil> deactivateRegle(@PathVariable Long id) {
        try {
            RegleSeuil deactivatedRegle = regleSeuilService.deactivateRegle(id);
            return ResponseEntity.ok(deactivatedRegle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count/equipement/{equipementId}/active")
    public ResponseEntity<Long> getActiveRulesCountByEquipement(@PathVariable Long equipementId) {
        Long count = regleSeuilService.getActiveRulesCountByEquipement(equipementId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/equipement/{equipementId}/parametre/{parametre}/check")
    public ResponseEntity<Boolean> checkValueWithinThresholds(
            @PathVariable Long equipementId,
            @PathVariable String parametre,
            @RequestParam Double valeur) {
        boolean withinThresholds = regleSeuilService.isValueWithinThresholds(equipementId, parametre, valeur);
        return ResponseEntity.ok(withinThresholds);
    }
}

