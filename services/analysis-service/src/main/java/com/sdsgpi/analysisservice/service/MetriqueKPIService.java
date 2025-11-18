package com.sdsgpi.analysisservice.service;

import com.sdsgpi.analysisservice.dto.CreateMetriqueRequest;
import com.sdsgpi.analysisservice.dto.MetriqueKPIDto;
import com.sdsgpi.analysisservice.entity.MetriqueKPI;
import com.sdsgpi.analysisservice.exception.MetriqueNotFoundException;
import com.sdsgpi.analysisservice.repository.MetriqueKPIRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des métriques KPI
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MetriqueKPIService {

    private final MetriqueKPIRepository metriqueRepository;

    /**
     * Crée une nouvelle métrique KPI
     */
    public MetriqueKPIDto createMetrique(CreateMetriqueRequest request) {
        log.info("Création d'une nouvelle métrique KPI: {}", request.getNomMetrique());

        MetriqueKPI metrique = MetriqueKPI.builder()
                .nomMetrique(request.getNomMetrique())
                .categorie(request.getCategorie())
                .valeurActuelle(request.getValeurActuelle())
                .valeurCible(request.getValeurCible())
                .valeurPrecedente(request.getValeurPrecedente())
                .unite(request.getUnite())
                .dateCalcul(LocalDateTime.now())
                .periodeCalcul(request.getPeriodeCalcul())
                .status(MetriqueKPI.StatusMetrique.ACTIF)
                .seuilAlerte(request.getSeuilAlerte())
                .historique(new ArrayList<>())
                .build();

        // Ajouter la valeur actuelle à l'historique
        if (metrique.getHistorique() != null) {
            metrique.getHistorique().add(request.getValeurActuelle());
        }

        MetriqueKPI savedMetrique = metriqueRepository.save(metrique);
        log.info("Métrique KPI créée avec l'ID {}", savedMetrique.getId());

        return convertToDto(savedMetrique);
    }

    /**
     * Récupère toutes les métriques actives
     */
    @Transactional(readOnly = true)
    public List<MetriqueKPIDto> getAllActiveMetrics() {
        return metriqueRepository.findActiveMetrics().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une métrique par ID
     */
    @Transactional(readOnly = true)
    public MetriqueKPIDto getMetriqueById(Long id) {
        MetriqueKPI metrique = metriqueRepository.findById(id)
                .orElseThrow(() -> new MetriqueNotFoundException("Métrique non trouvée avec l'ID: " + id));
        return convertToDto(metrique);
    }

    /**
     * Récupère une métrique par nom
     */
    @Transactional(readOnly = true)
    public MetriqueKPIDto getMetriqueByName(String nomMetrique) {
        MetriqueKPI metrique = metriqueRepository.findByNomMetrique(nomMetrique)
                .orElseThrow(() -> new MetriqueNotFoundException("Métrique non trouvée avec le nom: " + nomMetrique));
        return convertToDto(metrique);
    }

    /**
     * Récupère les métriques par catégorie
     */
    @Transactional(readOnly = true)
    public List<MetriqueKPIDto> getMetriquesByCategory(String categorie) {
        return metriqueRepository.findByCategorieOrderByDateCalculDesc(categorie).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les métriques avec alerte
     */
    @Transactional(readOnly = true)
    public List<MetriqueKPIDto> getMetricsWithAlert() {
        return metriqueRepository.findMetricsWithAlert().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les métriques sous la cible
     */
    @Transactional(readOnly = true)
    public List<MetriqueKPIDto> getMetricsBelowTarget() {
        return metriqueRepository.findMetricsBelowTarget().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour la valeur d'une métrique
     */
    public MetriqueKPIDto updateMetriqueValue(Long id, Double nouvelleValeur) {
        log.info("Mise à jour de la valeur de la métrique {} avec la nouvelle valeur {}", id, nouvelleValeur);

        MetriqueKPI metrique = metriqueRepository.findById(id)
                .orElseThrow(() -> new MetriqueNotFoundException("Métrique non trouvée avec l'ID: " + id));

        // Sauvegarder l'ancienne valeur
        metrique.setValeurPrecedente(metrique.getValeurActuelle());
        
        // Mettre à jour avec la nouvelle valeur
        metrique.setValeurActuelle(nouvelleValeur);
        metrique.setDateCalcul(LocalDateTime.now());

        // Ajouter à l'historique
        if (metrique.getHistorique() == null) {
            metrique.setHistorique(new ArrayList<>());
        }
        metrique.getHistorique().add(nouvelleValeur);

        // Limiter l'historique à 100 valeurs
        if (metrique.getHistorique().size() > 100) {
            metrique.getHistorique().remove(0);
        }

        MetriqueKPI savedMetrique = metriqueRepository.save(metrique);
        return convertToDto(savedMetrique);
    }

    /**
     * Met à jour la cible d'une métrique
     */
    public MetriqueKPIDto updateMetriqueTarget(Long id, Double nouvelleCible) {
        log.info("Mise à jour de la cible de la métrique {} avec la nouvelle cible {}", id, nouvelleCible);

        MetriqueKPI metrique = metriqueRepository.findById(id)
                .orElseThrow(() -> new MetriqueNotFoundException("Métrique non trouvée avec l'ID: " + id));

        metrique.setValeurCible(nouvelleCible);
        MetriqueKPI savedMetrique = metriqueRepository.save(metrique);
        return convertToDto(savedMetrique);
    }

    /**
     * Archive une métrique
     */
    public MetriqueKPIDto archiveMetrique(Long id) {
        log.info("Archivage de la métrique {}", id);

        MetriqueKPI metrique = metriqueRepository.findById(id)
                .orElseThrow(() -> new MetriqueNotFoundException("Métrique non trouvée avec l'ID: " + id));

        metrique.setStatus(MetriqueKPI.StatusMetrique.ARCHIVE);
        MetriqueKPI savedMetrique = metriqueRepository.save(metrique);
        return convertToDto(savedMetrique);
    }

    /**
     * Recherche des métriques
     */
    @Transactional(readOnly = true)
    public List<MetriqueKPIDto> searchMetriques(String searchTerm) {
        return metriqueRepository.searchByName(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Calcule les statistiques d'une métrique
     */
    @Transactional(readOnly = true)
    public MetriqueStatsDto getMetriqueStats(Long id) {
        MetriqueKPI metrique = metriqueRepository.findById(id)
                .orElseThrow(() -> new MetriqueNotFoundException("Métrique non trouvée avec l'ID: " + id));

        List<Double> historique = metrique.getHistorique();
        if (historique == null || historique.isEmpty()) {
            return MetriqueStatsDto.builder()
                    .metriqueId(id)
                    .nombreValeurs(0)
                    .build();
        }

        double moyenne = historique.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double min = historique.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double max = historique.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

        return MetriqueStatsDto.builder()
                .metriqueId(id)
                .nombreValeurs(historique.size())
                .moyenne(moyenne)
                .minimum(min)
                .maximum(max)
                .valeurActuelle(metrique.getValeurActuelle())
                .tendance(metrique.getTendance())
                .build();
    }

    /**
     * Supprime une métrique
     */
    public void deleteMetrique(Long id) {
        log.info("Suppression de la métrique {}", id);
        
        if (!metriqueRepository.existsById(id)) {
            throw new MetriqueNotFoundException("Métrique non trouvée avec l'ID: " + id);
        }
        
        metriqueRepository.deleteById(id);
    }

    /**
     * Convertit une entité en DTO
     */
    private MetriqueKPIDto convertToDto(MetriqueKPI metrique) {
        MetriqueKPIDto dto = MetriqueKPIDto.builder()
                .id(metrique.getId())
                .nomMetrique(metrique.getNomMetrique())
                .categorie(metrique.getCategorie())
                .valeurActuelle(metrique.getValeurActuelle())
                .valeurCible(metrique.getValeurCible())
                .valeurPrecedente(metrique.getValeurPrecedente())
                .unite(metrique.getUnite())
                .dateCalcul(metrique.getDateCalcul())
                .periodeCalcul(metrique.getPeriodeCalcul())
                .status(metrique.getStatus())
                .seuilAlerte(metrique.getSeuilAlerte())
                .historique(metrique.getHistorique())
                .createdAt(metrique.getCreatedAt())
                .updatedAt(metrique.getUpdatedAt())
                .tendance(metrique.getTendance())
                .build();

        return dto;
    }

    /**
     * DTO pour les statistiques d'une métrique
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class MetriqueStatsDto {
        private Long metriqueId;
        private Integer nombreValeurs;
        private Double moyenne;
        private Double minimum;
        private Double maximum;
        private Double valeurActuelle;
        private MetriqueKPI.TendanceMetrique tendance;
    }
}

