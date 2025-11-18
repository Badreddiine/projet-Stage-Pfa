package com.badr.equipement_service.controller;

import com.badr.equipement_service.dto.DonneesCapteurdto;
import com.badr.equipement_service.entity.DonneesCapteur;
import com.badr.equipement_service.service.DonneesCapteurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/donnees-capteur")
// Suppression de @CrossOrigin - le Gateway gère les CORS
public class DonneesCapteurController {

    private final DonneesCapteurService donneesCapteurService;

    public DonneesCapteurController(DonneesCapteurService donneesCapteurService) {
        this.donneesCapteurService = donneesCapteurService;
    }

    @GetMapping
    public ResponseEntity<List<DonneesCapteurdto>> getAllDonneesCapteur() {
        List<DonneesCapteurdto> donnees = donneesCapteurService.getAllDonneesCapteur();
        return ResponseEntity.ok(donnees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonneesCapteurdto> getDonneesCapteurById(@PathVariable Long id) {
        return donneesCapteurService.getDonneesCapteurById(id)
                .map(donnees -> ResponseEntity.ok(donnees))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DonneesCapteur> createDonneesCapteur(@Valid @RequestBody DonneesCapteurdto donneesCapteur) {
        DonneesCapteur createdDonnees = donneesCapteurService.createDonneesCapteur(donneesCapteur);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDonnees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonneesCapteur> updateDonneesCapteur(@Valid @RequestBody DonneesCapteurdto donneesDetails) {
        Long id = donneesDetails.getIdentifiant();
        try {
            DonneesCapteur updatedDonnees = donneesCapteurService.updateDonneesCapteur(donneesDetails);
            return ResponseEntity.ok(updatedDonnees);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonneesCapteur(@PathVariable Long id) {
        donneesCapteurService.deleteDonneesCapteur(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/equipement/{equipementId}")
    public ResponseEntity<List<DonneesCapteurdto>> getDonneesByEquipement(@PathVariable Long equipementId) {
        List<DonneesCapteurdto> donnees = donneesCapteurService.getDonneesByEquipement(equipementId);
        return ResponseEntity.ok(donnees);
    }

    @GetMapping("/type/{typeCapteur}")
    public ResponseEntity<List<DonneesCapteurdto>> getDonneesByTypeCapteur(@PathVariable String typeCapteur) {
        List<DonneesCapteurdto> donnees = donneesCapteurService.getDonneesByTypeCapteur(typeCapteur);
        return ResponseEntity.ok(donnees);
    }

    @GetMapping("/equipement/{equipementId}/type/{typeCapteur}")
    public ResponseEntity<List<DonneesCapteurdto>> getDonneesByEquipementAndType(
            @PathVariable Long equipementId,
            @PathVariable String typeCapteur) {
        List<DonneesCapteurdto> donnees = donneesCapteurService.getDonneesByEquipementAndType(equipementId, typeCapteur);
        return ResponseEntity.ok(donnees);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<DonneesCapteurdto>> getDonneesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        Timestamp tsDebut = Timestamp.valueOf(dateDebut);
        Timestamp tsFin = Timestamp.valueOf(dateFin);
        List<DonneesCapteurdto> donnees = donneesCapteurService.getDonneesByDateRange(tsDebut, tsFin);
        return ResponseEntity.ok(donnees);
    }

    @GetMapping("/equipement/{equipementId}/recent")
    public ResponseEntity<List<DonneesCapteurdto>> getRecentDataByEquipement(
            @PathVariable Long equipementId,
            @RequestParam(defaultValue = "24") int heures) {
        List<DonneesCapteurdto> donnees = donneesCapteurService.getRecentDataByEquipement(equipementId, heures);
        return ResponseEntity.ok(donnees);
    }

    @GetMapping("/equipement/{equipementId}/type/{typeCapteur}/average")
    public ResponseEntity<Double> getAverageValueByEquipementAndType(
            @PathVariable Long equipementId,
            @PathVariable String typeCapteur,
            @RequestParam(defaultValue = "24") int heures) {
        Double average = donneesCapteurService.getAverageValueByEquipementAndType(equipementId, typeCapteur, heures);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/equipement/{equipementId}/latest")
    public ResponseEntity<DonneesCapteurdto> getLatestDataByEquipement(@PathVariable Long equipementId) {
        DonneesCapteurdto donnees = donneesCapteurService.getLatestDataByEquipement(equipementId);
        if (donnees != null) {
            return ResponseEntity.ok(donnees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/qualite/{qualite}")
    public ResponseEntity<List<DonneesCapteurdto>> getDonneesByQualite(@PathVariable String qualite) {
        List<DonneesCapteurdto> donnees = donneesCapteurService.getDonneesByQualite(qualite);
        return ResponseEntity.ok(donnees);
    }
}

