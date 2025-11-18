package com.incident.service.controller;

import com.incident.service.entity.RegleEscalade;
import com.incident.service.service.EscaladeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escalades")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EscaladeController {

    private final EscaladeService escaladeService;

    @GetMapping("/regles/{severite}")
    public ResponseEntity<List<RegleEscalade>> getReglesEscaladeBySeverite(@PathVariable String severite) {
        log.info("Récupération des règles d'escalade pour la sévérité: {}", severite);
        List<RegleEscalade> regles = escaladeService.getReglesEscaladeBySeverite(severite);
        return ResponseEntity.ok(regles);
    }

    @PostMapping("/regles")
    public ResponseEntity<RegleEscalade> createRegleEscalade(@RequestBody RegleEscalade regle) {
        log.info("Création d'une nouvelle règle d'escalade pour la sévérité: {}", regle.getSeverite());
        RegleEscalade nouvelleRegle = escaladeService.createRegleEscalade(regle);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleRegle);
    }

    @DeleteMapping("/regles/{id}")
    public ResponseEntity<Void> deleteRegleEscalade(@PathVariable Long id) {
        log.info("Suppression de la règle d'escalade avec l'ID: {}", id);
        escaladeService.deleteRegleEscalade(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verifier")
    public ResponseEntity<String> verifierEscalades() {
        log.info("Déclenchement manuel de la vérification des escalades");
        escaladeService.verifierEscalades();
        return ResponseEntity.ok("Vérification des escalades déclenchée");
    }
}

