package com.notification.controller;

import com.notification.dto.PreferenceUtilisateurdto;
import com.notification.entity.PreferenceUtilisateur;
import com.notification.repository.PreferenceUtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/preferences")


@CrossOrigin(origins = "*")
public class PreferenceUtilisateurController {



    private final PreferenceUtilisateurRepository preferenceRepository;
    public PreferenceUtilisateurController(PreferenceUtilisateurRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }
    @PostMapping
    public ResponseEntity<PreferenceUtilisateur> creerPreference(@Valid @RequestBody PreferenceUtilisateurdto preferenceDto) {
        try {
            PreferenceUtilisateur preference = new PreferenceUtilisateur(preferenceDto);
            preference.setDateMiseAJour(LocalDateTime.now());

            PreferenceUtilisateur nouvellePreference = preferenceRepository.save(preference);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvellePreference);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<PreferenceUtilisateurdto>> obtenirPreferencesUtilisateur(@PathVariable Long utilisateurId) {
        try {
            List<PreferenceUtilisateurdto> preferences = preferenceRepository.findByUtilisateurId(utilisateurId)
                    .stream()
                    .map(PreferenceUtilisateurdto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}/actives")
    public ResponseEntity<List<PreferenceUtilisateurdto>> obtenirPreferencesActives(@PathVariable Long utilisateurId) {
        try {
            List<PreferenceUtilisateurdto> preferences = preferenceRepository.findByUtilisateurIdAndEstActive(utilisateurId, true)
                    .stream()
                    .map(PreferenceUtilisateurdto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{preferenceId}")
    public ResponseEntity<PreferenceUtilisateurdto> obtenirPreferenceParId(@PathVariable Long preferenceId) {
        try {
            Optional<PreferenceUtilisateur> preference = preferenceRepository.findById(preferenceId);
            return preference.map(p -> ResponseEntity.ok(new PreferenceUtilisateurdto(p)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{preferenceId}")
    public ResponseEntity<PreferenceUtilisateur> modifierPreference(
            @PathVariable Long preferenceId,
            @Valid @RequestBody PreferenceUtilisateurdto preferenceDto) {
        try {
            Optional<PreferenceUtilisateur> preferenceOpt = preferenceRepository.findById(preferenceId);
            if (preferenceOpt.isPresent()) {
                PreferenceUtilisateur preference = preferenceOpt.get();

                // Mise à jour des champs
                preference.setTypeNotification(preferenceDto.getTypeNotification());
                preference.setEstActive(preferenceDto.getEstActive());
                preference.setTopicsMQTT(preferenceDto.getTopicsMQTT());
                preference.setDateMiseAJour(LocalDateTime.now());

                PreferenceUtilisateur preferenceModifiee = preferenceRepository.save(preference);
                return ResponseEntity.ok(preferenceModifiee);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{preferenceId}")
    public ResponseEntity<Void> supprimerPreference(@PathVariable Long preferenceId) {
        try {
            if (preferenceRepository.existsById(preferenceId)) {
                preferenceRepository.deleteById(preferenceId);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{preferenceId}/activer")
    public ResponseEntity<Void> activerPreference(@PathVariable Long preferenceId) {
        try {
            Optional<PreferenceUtilisateur> preferenceOpt = preferenceRepository.findById(preferenceId);
            if (preferenceOpt.isPresent()) {
                PreferenceUtilisateur preference = preferenceOpt.get();
                preference.setEstActive(true);
                preference.setDateMiseAJour(LocalDateTime.now());
                preferenceRepository.save(preference);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{preferenceId}/desactiver")
    public ResponseEntity<Void> desactiverPreference(@PathVariable Long preferenceId) {
        try {
            Optional<PreferenceUtilisateur> preferenceOpt = preferenceRepository.findById(preferenceId);
            if (preferenceOpt.isPresent()) {
                PreferenceUtilisateur preference = preferenceOpt.get();
                preference.setEstActive(false);
                preference.setDateMiseAJour(LocalDateTime.now());
                preferenceRepository.save(preference);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/type/{typeNotification}")
    public ResponseEntity<List<PreferenceUtilisateurdto>> obtenirPreferencesParType(@PathVariable String typeNotification) {
        try {
            List<PreferenceUtilisateurdto> preferences = preferenceRepository.findByTypeNotificationAndEstActive(typeNotification, true)
                    .stream()
                    .map(PreferenceUtilisateurdto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

