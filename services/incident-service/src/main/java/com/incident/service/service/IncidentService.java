package com.incident.service.service;

import com.incident.service.dto.CreateIncidentRequest;
import com.incident.service.dto.IncidentDTO;
import com.incident.service.dto.UpdateIncidentRequest;
import com.incident.service.entity.GestionnaireSLA;
import com.incident.service.entity.Incident;
import com.incident.service.repository.GestionnaireSLARepository;
import com.incident.service.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final GestionnaireSLARepository gestionnaireSLARepository;

    public List<IncidentDTO> getAllIncidents() {
        return incidentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<IncidentDTO> getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<IncidentDTO> getIncidentsByStatut(String statut) {
        return incidentRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<IncidentDTO> getIncidentsBySeverite(String severite) {
        return incidentRepository.findBySeverite(severite).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<IncidentDTO> getIncidentsByUtilisateur(Long utilisateurId) {
        return incidentRepository.findByUtilisateurAssigneId(utilisateurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public IncidentDTO createIncident(CreateIncidentRequest request) {
        Incident incident = new Incident();
        incident.setTitre(request.getTitre());
        incident.setDescription(request.getDescription());
        incident.setSeverite(request.getSeverite());
        incident.setPriorite(request.getPriorite());
        incident.setEquipementId(request.getEquipementId());
        incident.setUtilisateurAssigneId(request.getUtilisateurAssigneId());
        incident.setStatut("NOUVEAU");

        // Calculer la date d'échéance basée sur le SLA
        calculateEcheance(incident);

        Incident savedIncident = incidentRepository.save(incident);
        log.info("Incident créé avec l'ID: {}", savedIncident.getId());

        return convertToDTO(savedIncident);
    }

    public Optional<IncidentDTO> updateIncident(Long id, UpdateIncidentRequest request) {
        return incidentRepository.findById(id)
                .map(incident -> {
                    if (request.getTitre() != null) {
                        incident.setTitre(request.getTitre());
                    }
                    if (request.getDescription() != null) {
                        incident.setDescription(request.getDescription());
                    }
                    if (request.getSeverite() != null) {
                        incident.setSeverite(request.getSeverite());
                        // Recalculer l'échéance si la sévérité change
                        calculateEcheance(incident);
                    }
                    if (request.getStatut() != null) {
                        incident.setStatut(request.getStatut());
                        if ("RESOLU".equals(request.getStatut()) && incident.getDateResolution() == null) {
                            incident.setDateResolution(LocalDateTime.now());
                        }
                    }
                    if (request.getPriorite() != null) {
                        incident.setPriorite(request.getPriorite());
                    }
                    if (request.getUtilisateurAssigneId() != null) {
                        incident.setUtilisateurAssigneId(request.getUtilisateurAssigneId());
                    }
                    if (request.getDateResolution() != null) {
                        incident.setDateResolution(request.getDateResolution());
                    }
                    if (request.getDateEcheance() != null) {
                        incident.setDateEcheance(request.getDateEcheance());
                    }

                    Incident updatedIncident = incidentRepository.save(incident);
                    log.info("Incident mis à jour avec l'ID: {}", updatedIncident.getId());

                    return convertToDTO(updatedIncident);
                });
    }

    public boolean deleteIncident(Long id) {
        if (incidentRepository.existsById(id)) {
            incidentRepository.deleteById(id);
            log.info("Incident supprimé avec l'ID: {}", id);
            return true;
        }
        return false;
    }

    public List<IncidentDTO> getIncidentsEnRetard() {
        return incidentRepository.findIncidentsEnRetard(LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Long countIncidentsByStatut(String statut) {
        return incidentRepository.countByStatut(statut);
    }

    private void calculateEcheance(Incident incident) {
        Optional<GestionnaireSLA> sla = gestionnaireSLARepository.findBySeverite(incident.getSeverite());
        if (sla.isPresent()) {
            LocalDateTime echeance = incident.getDateCreation() != null 
                ? incident.getDateCreation().plusMinutes(sla.get().getTempsResolutionMinutes())
                : LocalDateTime.now().plusMinutes(sla.get().getTempsResolutionMinutes());
            incident.setDateEcheance(echeance);
        }
    }

    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setId(incident.getId());
        dto.setTitre(incident.getTitre());
        dto.setDescription(incident.getDescription());
        dto.setSeverite(incident.getSeverite());
        dto.setStatut(incident.getStatut());
        dto.setPriorite(incident.getPriorite());
        dto.setEquipementId(incident.getEquipementId());
        dto.setUtilisateurAssigneId(incident.getUtilisateurAssigneId());
        dto.setDateCreation(incident.getDateCreation());
        dto.setDateResolution(incident.getDateResolution());
        dto.setDateEcheance(incident.getDateEcheance());


        return dto;
    }
}

