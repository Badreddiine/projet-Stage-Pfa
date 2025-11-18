package com.badr.equipement_service.service;

import com.badr.equipement_service.dto.Alertedto;
import com.badr.equipement_service.entity.Alerte;
import com.badr.equipement_service.repository.AlerteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@Transactional
public class AlerteService {



    private final AlerteRepository alerteRepository;
    public AlerteService(AlerteRepository alerteRepository) {
        this.alerteRepository = alerteRepository;
    }


    /**
     * methode pour trouver tout les alertes
     * @return List des dto des alert
     */
    @Transactional(readOnly = true)
    public List<Alertedto> getAllAlertes() {
        return alerteRepository.findAll()
                .stream()
                .map(Alertedto::new)
                .collect(Collectors.toList());
    }

    /**
     * trouver une alert par sons id
     * @param id  a recuperer apertir de dto dans controller
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Alertedto> getAlerteById(Long id) {
        return alerteRepository.findById(id)
                .map(Alertedto::new);
    }

    /**
     * methode pour creer une alert
     * @param alertedto a recuperer a partir de dto dans controller
     * @return
     */
    public Alerte createAlerte(Alertedto alertedto) {

        alertedto.setDateCreation(LocalDateTime.now());
        alertedto.setStatut("ACTIVE");
        Alerte alerte =new Alerte(alertedto);
        return alerteRepository.save(alerte);
    }

    /**
     *methode pour modifier une alerte
     * @param alerteDetails a recuperer de puis le dto envoyer
     * @return
     */
    public Alerte updateAlerte( Alertedto alerteDetails) {
        Long id = alerteDetails.getId();

        return alerteRepository.findById(id)
                .map(alerte -> {
                    alerte.setTypeAlerte(alerteDetails.getTypeAlerte());
                    alerte.setSeverite(alerteDetails.getSeverite());
                    alerte.setMessage(alerteDetails.getMessage());
                    alerte.setStatut(alerteDetails.getStatut());
                    alerte.setDateAccusee(alerteDetails.getDateAccusee());

                    return alerteRepository.save(alerte);
                })
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée avec l'ID: " + id));
    }

    /**
     * methode pour sup une alert
     * @param id a recuperer soit en front ou bien dans controller
     */
    public void deleteAlerte(Long id) {
        alerteRepository.deleteById(id);
    }

    /**
     * trouver les alertes liee par une equipement
     * @param equipementId a recuperer a partir du front end
     * @return
     */
    @Transactional(readOnly = true)
    public List<Alertedto> getAlertesByEquipement(Long equipementId) {
        return alerteRepository.findByEquipementId(equipementId)
                .stream()
                .map(Alertedto::new)
                .collect(Collectors.toList());
    }

    /**
     * recuperer les alert par leur status pour s avoir son importance et le traiter
     * @param statut a recuperer dans front end
     * @return
     */
    @Transactional(readOnly = true)
    public List<Alertedto> getAlertesByStatut(String statut) {
        return alerteRepository.findByStatut(statut)
                .stream()
                .map(Alertedto::new)
                .collect(Collectors.toList());
    }

    /**
     * s'avoire les alert liee pour chaque service
     * @param severite
     * @return
     */
    @Transactional(readOnly = true)
    public List<Alertedto> getAlertesBySeverite(String severite) {
        return alerteRepository.findBySeverite(severite)
                .stream()
                .map(Alertedto::new)
                .collect(Collectors.toList());
    }

    /**
     * les alertes actives de cette instant
     * @return
     */
    @Transactional(readOnly = true)
    public List<Alertedto> getActiveAlertes() {
        return alerteRepository.findActiveAlertesOrderedBySeverityAndDate()
                .stream()
                .map(Alertedto::new)
                .collect(Collectors.toList());
    }

    public Alerte acknowledgeAlerte(Long id) {

        return alerteRepository.findById(id)
                .map(alerte -> {
                    alerte.setDateAccusee(LocalDateTime.now());
                    alerte.setStatut("ACKNOWLEDGED");
                    return alerteRepository.save(alerte);
                })
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée avec l'ID: " + id));
    }

    /**
     * les alertes qui  sont deja resolus
     * @param id
     * @return
     */
    public Alerte resolveAlerte(Long id) {

        return alerteRepository.findById(id)
                .map(alerte -> {
                    alerte.setStatut("RESOLVED");
                    if (alerte.getDateAccusee() == null) {
                        alerte.setDateAccusee(LocalDateTime.now());
                    }
                    return alerteRepository.save(alerte);
                })
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée avec l'ID: " + id));
    }

    /**
     * trouver une alert par sa date de debut et date de fin a fin d analyser la derus pour la resolution des pannes
     * @param dateDebut
     * @param dateFin
     * @return
     */
    @Transactional(readOnly = true)
    public List<Alertedto> getAlertesByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return alerteRepository.findByDateCreationBetween(dateDebut, dateFin)
                .stream()
                .map(Alertedto::new)
                .collect(Collectors.toList());
    }

    public Long getActiveAlertesCountByEquipement(Long equipementId) {
        return alerteRepository.countActiveAlertesByEquipement(equipementId);
    }
}

