package com.incident.service.controller;

import com.incident.service.dto.CreateIncidentRequest;
import com.incident.service.dto.IncidentDTO;
import com.incident.service.dto.UpdateIncidentRequest;
import com.incident.service.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<IncidentDTO>> getAllIncidents() {
        log.info("Récupération de tous les incidents");
        List<IncidentDTO> incidents = incidentService.getAllIncidents();
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentDTO> getIncidentById(@PathVariable Long id) {
        log.info("Récupération de l'incident avec l'ID: {}", id);
        return incidentService.getIncidentById(id)
                .map(incident -> ResponseEntity.ok(incident))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByStatut(@PathVariable String statut) {
        log.info("Récupération des incidents avec le statut: {}", statut);
        List<IncidentDTO> incidents = incidentService.getIncidentsByStatut(statut);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/severite/{severite}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsBySeverite(@PathVariable String severite) {
        log.info("Récupération des incidents avec la sévérité: {}", severite);
        List<IncidentDTO> incidents = incidentService.getIncidentsBySeverite(severite);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByUtilisateur(@PathVariable Long utilisateurId) {
        log.info("Récupération des incidents pour l'utilisateur: {}", utilisateurId);
        List<IncidentDTO> incidents = incidentService.getIncidentsByUtilisateur(utilisateurId);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/retard")
    public ResponseEntity<List<IncidentDTO>> getIncidentsEnRetard() {
        log.info("Récupération des incidents en retard");
        List<IncidentDTO> incidents = incidentService.getIncidentsEnRetard();
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/count/{statut}")
    public ResponseEntity<Long> countIncidentsByStatut(@PathVariable String statut) {
        log.info("Comptage des incidents avec le statut: {}", statut);
        Long count = incidentService.countIncidentsByStatut(statut);
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<IncidentDTO> createIncident(@Valid @RequestBody CreateIncidentRequest request) {
        log.info("Création d'un nouvel incident: {}", request.getTitre());
        IncidentDTO incident = incidentService.createIncident(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(incident);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentDTO> updateIncident(@PathVariable Long id, 
                                                     @RequestBody UpdateIncidentRequest request) {
        log.info("Mise à jour de l'incident avec l'ID: {}", id);
        return incidentService.updateIncident(id, request)
                .map(incident -> ResponseEntity.ok(incident))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        log.info("Suppression de l'incident avec l'ID: {}", id);
        if (incidentService.deleteIncident(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

