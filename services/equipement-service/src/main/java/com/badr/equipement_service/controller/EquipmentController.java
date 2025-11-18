package com.badr.equipement_service.controller;

import com.badr.equipement_service.dto.Equipmentdto;
import com.badr.equipement_service.entity.Equipment;
import com.badr.equipement_service.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/equipments")
// Suppression de @CrossOrigin - le Gateway gère les CORS
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public ResponseEntity<List<Equipmentdto>> getAllEquipments() {
        try {
            List<Equipmentdto> equipments = equipmentService.getAllEquipments();
            return ResponseEntity.ok(equipments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipmentdto> getEquipmentById(@PathVariable Long id) {
        try {
            return equipmentService.getEquipmentById(id)
                    .map(equipment -> ResponseEntity.ok(equipment))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@Valid @RequestBody Equipmentdto equipment) {
        try {
            Equipment createdEquipment = equipmentService.createEquipment(equipment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEquipment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @Valid @RequestBody Equipmentdto equipmentDetails) {
        try {
            // S'assurer que l'ID est défini dans le DTO
            equipmentDetails.setIdentifiant(id);

            Equipment updatedEquipment = equipmentService.updateEquipment(equipmentDetails);
            return ResponseEntity.ok(updatedEquipment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        try {
            equipmentService.deleteEquipment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Equipmentdto>> getEquipmentsByStatut(@PathVariable String statut) {
        try {
            List<Equipmentdto> equipments = equipmentService.getEquipmentsByStatut(statut);
            return ResponseEntity.ok(equipments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Equipmentdto>> getEquipmentsByType(@PathVariable String type) {
        try {
            List<Equipmentdto> equipments = equipmentService.getEquipmentsByType(type);
            return ResponseEntity.ok(equipments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Equipmentdto>> getActiveEquipments() {
        try {
            List<Equipmentdto> equipments = equipmentService.getActiveEquipments();
            return ResponseEntity.ok(equipments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/connection-status")
    public ResponseEntity<Equipment> updateConnectionStatus(
            @PathVariable Long id,
            @RequestParam LocalDateTime derniereConnexion,
            @RequestParam Integer tentatives) {
        try {
            Equipment updatedEquipment = equipmentService.updateConnectionStatus(id, derniereConnexion, tentatives);
            return ResponseEntity.ok(updatedEquipment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<Void> unlockEquipment(@PathVariable Long id) {
        try {
            equipmentService.unlockEquipment(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count/statut/{statut}")
    public ResponseEntity<Long> getEquipmentCountByStatut(@PathVariable String statut) {
        try {
            Long count = equipmentService.getEquipmentCountByStatut(statut);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

