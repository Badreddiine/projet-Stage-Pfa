package com.incident.service.service;

import com.incident.service.entity.Incident;
import com.incident.service.entity.RegleEscalade;
import com.incident.service.repository.IncidentRepository;
import com.incident.service.repository.RegleEscaladeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EscaladeService {

    private final RegleEscaladeRepository regleEscaladeRepository;
    private final IncidentRepository incidentRepository;

    @Async
    public void verifierEscalades() {
        log.info("Vérification des escalades en cours...");
        
        List<Incident> incidentsActifs = incidentRepository.findByStatut("EN_COURS");
        
        for (Incident incident : incidentsActifs) {
            verifierEscaladePourIncident(incident);
        }
    }

    private void verifierEscaladePourIncident(Incident incident) {
        List<RegleEscalade> regles = regleEscaladeRepository
                .findBySeveriteAndEstActiveOrderByNiveauEscalade(incident.getSeverite(), true);

        for (RegleEscalade regle : regles) {
            if (doitEscalader(incident, regle)) {
                executerEscalade(incident, regle);
            }
        }
    }

    private boolean doitEscalader(Incident incident, RegleEscalade regle) {
        LocalDateTime limiteEscalade = incident.getDateCreation()
                .plusMinutes(regle.getDelaiMinutes());
        
        return LocalDateTime.now().isAfter(limiteEscalade);
    }

    private void executerEscalade(Incident incident, RegleEscalade regle) {
        log.info("Escalade de l'incident {} vers le niveau {} pour le rôle {}", 
                incident.getId(), regle.getNiveauEscalade(), regle.getRoleCible());

        // Ici, vous pourriez intégrer avec le service de notification
        // pour envoyer des notifications selon la méthode spécifiée
        envoyerNotificationEscalade(incident, regle);
    }

    private void envoyerNotificationEscalade(Incident incident, RegleEscalade regle) {
        // Logique d'envoi de notification
        // Cela pourrait être intégré avec le service de notification existant
        log.info("Notification d'escalade envoyée pour l'incident {} via {}", 
                incident.getId(), regle.getMethodeNotification());
    }

    public List<RegleEscalade> getReglesEscaladeBySeverite(String severite) {
        return regleEscaladeRepository.findBySeveriteAndEstActiveOrderByNiveauEscalade(severite, true);
    }

    public RegleEscalade createRegleEscalade(RegleEscalade regle) {
        return regleEscaladeRepository.save(regle);
    }

    public void deleteRegleEscalade(Long id) {
        regleEscaladeRepository.deleteById(id);
    }
}

