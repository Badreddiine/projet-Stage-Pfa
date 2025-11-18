package com.sdsgpi.analysisservice.service;

import com.sdsgpi.analysisservice.dto.CreateRecommandationRequest;
import com.sdsgpi.analysisservice.dto.RecommandationMaintenanceDto;
import com.sdsgpi.analysisservice.entity.RecommandationMaintenance;
import com.sdsgpi.analysisservice.exception.RecommandationNotFoundException;
import com.sdsgpi.analysisservice.repository.RecommandationMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des recommandations de maintenance
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecommandationMaintenanceService {

    private final RecommandationMaintenanceRepository recommandationRepository;

    /**
     * Crée une nouvelle recommandation de maintenance
     */
    public RecommandationMaintenanceDto createRecommandation(CreateRecommandationRequest request) {
        log.info("Création d'une nouvelle recommandation pour l'équipement {}", request.getEquipmentId());

        RecommandationMaintenance recommandation = RecommandationMaintenance.builder()
                .equipmentId(request.getEquipmentId())
                .typeRecommandation(request.getTypeRecommandation())
                .titre(request.getTitre())
                .description(request.getDescription())
                .priorite(request.getPriorite())
                .dateSuggere(request.getDateSuggere())
                .dateCreation(LocalDateTime.now())
                .scoreConfiance(request.getScoreConfiance())
                .coutEstime(request.getCoutEstime())
                .status(RecommandationMaintenance.StatusRecommandation.EN_ATTENTE)
                .metadonnees(request.getMetadonnees())
                .build();

        RecommandationMaintenance savedRecommandation = recommandationRepository.save(recommandation);
        log.info("Recommandation créée avec l'ID {}", savedRecommandation.getId());

        return convertToDto(savedRecommandation);
    }

    /**
     * Récupère toutes les recommandations
     */
    @Transactional(readOnly = true)
    public List<RecommandationMaintenanceDto> getAllRecommandations() {
        return recommandationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une recommandation par ID
     */
    @Transactional(readOnly = true)
    public RecommandationMaintenanceDto getRecommandationById(Long id) {
        RecommandationMaintenance recommandation = recommandationRepository.findById(id)
                .orElseThrow(() -> new RecommandationNotFoundException("Recommandation non trouvée avec l'ID: " + id));
        return convertToDto(recommandation);
    }

    /**
     * Récupère les recommandations par équipement
     */
    @Transactional(readOnly = true)
    public List<RecommandationMaintenanceDto> getRecommandationsByEquipment(Long equipmentId) {
        return recommandationRepository.findByEquipmentIdOrderByDateCreationDesc(equipmentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les recommandations urgentes
     */
    @Transactional(readOnly = true)
    public List<RecommandationMaintenanceDto> getUrgentRecommandations() {
        return recommandationRepository.findUrgentRecommandations().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les recommandations par statut
     */
    @Transactional(readOnly = true)
    public List<RecommandationMaintenanceDto> getRecommandationsByStatus(RecommandationMaintenance.StatusRecommandation status) {
        return recommandationRepository.findByStatusOrderByDateCreationDesc(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Approuve une recommandation
     */
    public RecommandationMaintenanceDto approveRecommandation(Long id, String approvedBy) {
        log.info("Approbation de la recommandation {} par {}", id, approvedBy);

        RecommandationMaintenance recommandation = recommandationRepository.findById(id)
                .orElseThrow(() -> new RecommandationNotFoundException("Recommandation non trouvée avec l'ID: " + id));

        recommandation.setStatus(RecommandationMaintenance.StatusRecommandation.APPROUVE);
        recommandation.setApprouvePar(approvedBy);

        RecommandationMaintenance savedRecommandation = recommandationRepository.save(recommandation);
        return convertToDto(savedRecommandation);
    }

    /**
     * Rejette une recommandation
     */
    public RecommandationMaintenanceDto rejectRecommandation(Long id, String rejectedBy) {
        log.info("Rejet de la recommandation {} par {}", id, rejectedBy);

        RecommandationMaintenance recommandation = recommandationRepository.findById(id)
                .orElseThrow(() -> new RecommandationNotFoundException("Recommandation non trouvée avec l'ID: " + id));

        recommandation.setStatus(RecommandationMaintenance.StatusRecommandation.REJETE);
        recommandation.setApprouvePar(rejectedBy);

        RecommandationMaintenance savedRecommandation = recommandationRepository.save(recommandation);
        return convertToDto(savedRecommandation);
    }

    /**
     * Met à jour le statut d'une recommandation
     */
    public RecommandationMaintenanceDto updateStatus(Long id, RecommandationMaintenance.StatusRecommandation newStatus) {
        log.info("Mise à jour du statut de la recommandation {} vers {}", id, newStatus);

        RecommandationMaintenance recommandation = recommandationRepository.findById(id)
                .orElseThrow(() -> new RecommandationNotFoundException("Recommandation non trouvée avec l'ID: " + id));

        recommandation.setStatus(newStatus);
        RecommandationMaintenance savedRecommandation = recommandationRepository.save(recommandation);
        return convertToDto(savedRecommandation);
    }

    /**
     * Recherche des recommandations
     */
    @Transactional(readOnly = true)
    public List<RecommandationMaintenanceDto> searchRecommandations(String searchTerm) {
        return recommandationRepository.searchByTitleAndDescription(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Supprime une recommandation
     */
    public void deleteRecommandation(Long id) {
        log.info("Suppression de la recommandation {}", id);
        
        if (!recommandationRepository.existsById(id)) {
            throw new RecommandationNotFoundException("Recommandation non trouvée avec l'ID: " + id);
        }
        
        recommandationRepository.deleteById(id);
    }

    /**
     * Convertit une entité en DTO
     */
    private RecommandationMaintenanceDto convertToDto(RecommandationMaintenance recommandation) {
        return RecommandationMaintenanceDto.builder()
                .id(recommandation.getId())
                .equipmentId(recommandation.getEquipmentId())
                .typeRecommandation(recommandation.getTypeRecommandation())
                .titre(recommandation.getTitre())
                .description(recommandation.getDescription())
                .priorite(recommandation.getPriorite())
                .dateSuggere(recommandation.getDateSuggere())
                .dateCreation(recommandation.getDateCreation())
                .scoreConfiance(recommandation.getScoreConfiance())
                .coutEstime(recommandation.getCoutEstime())
                .status(recommandation.getStatus())
                .approuvePar(recommandation.getApprouvePar())
                .metadonnees(recommandation.getMetadonnees())
                .createdAt(recommandation.getCreatedAt())
                .updatedAt(recommandation.getUpdatedAt())
                .build();
    }
}

