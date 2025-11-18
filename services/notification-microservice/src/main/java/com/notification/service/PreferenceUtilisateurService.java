package com.notification.service;

import com.notification.dto.PreferenceUtilisateurdto;
import com.notification.entity.PreferenceUtilisateur;
import com.notification.repository.PreferenceUtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class PreferenceUtilisateurService {



    private final PreferenceUtilisateurRepository preferenceRepository;
    public PreferenceUtilisateurService(PreferenceUtilisateurRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }
    @Transactional
    public PreferenceUtilisateur creerPreference(PreferenceUtilisateurdto preferenceDto) {
        PreferenceUtilisateur preference = new PreferenceUtilisateur(preferenceDto);
        preference.setDateMiseAJour(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }

    public List<PreferenceUtilisateurdto> obtenirPreferencesUtilisateur(Long utilisateurId) {
        return preferenceRepository.findByUtilisateurId(utilisateurId)
                .stream()
                .map(PreferenceUtilisateurdto::new)
                .collect(Collectors.toList());
    }

    public List<PreferenceUtilisateurdto> obtenirPreferencesActives(Long utilisateurId) {
        return preferenceRepository.findByUtilisateurIdAndEstActive(utilisateurId, true)
                .stream()
                .map(PreferenceUtilisateurdto::new)
                .collect(Collectors.toList());
    }

    public Optional<PreferenceUtilisateurdto> obtenirPreferenceParId(Long id) {
        return preferenceRepository.findById(id)
                .map(PreferenceUtilisateurdto::new);
    }

    @Transactional
    public PreferenceUtilisateur modifierPreference(Long id, PreferenceUtilisateurdto preferenceDto) {
        return preferenceRepository.findById(id)
                .map(preference -> {
                    preference.setTypeNotification(preferenceDto.getTypeNotification());
                    preference.setEstActive(preferenceDto.getEstActive());
                    preference.setTopicsMQTT(preferenceDto.getTopicsMQTT());
                    preference.setDateMiseAJour(LocalDateTime.now());
                    return preferenceRepository.save(preference);
                })
                .orElseThrow(() -> new RuntimeException("Préférence non trouvée avec l'ID: " + id));
    }

    @Transactional
    public void supprimerPreference(Long id) {
        preferenceRepository.deleteById(id);
    }

    @Transactional
    public PreferenceUtilisateur activerPreference(Long id) {
        return preferenceRepository.findById(id)
                .map(preference -> {
                    preference.setEstActive(true);
                    preference.setDateMiseAJour(LocalDateTime.now());
                    return preferenceRepository.save(preference);
                })
                .orElseThrow(() -> new RuntimeException("Préférence non trouvée avec l'ID: " + id));
    }

    @Transactional
    public PreferenceUtilisateur desactiverPreference(Long id) {
        return preferenceRepository.findById(id)
                .map(preference -> {
                    preference.setEstActive(false);
                    preference.setDateMiseAJour(LocalDateTime.now());
                    return preferenceRepository.save(preference);
                })
                .orElseThrow(() -> new RuntimeException("Préférence non trouvée avec l'ID: " + id));
    }

    public List<PreferenceUtilisateurdto> obtenirPreferencesParType(String typeNotification) {
        return preferenceRepository.findByTypeNotificationAndEstActive(typeNotification, true)
                .stream()
                .map(PreferenceUtilisateurdto::new)
                .collect(Collectors.toList());
    }
}

