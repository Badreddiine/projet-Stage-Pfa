package com.notification.service;

import com.notification.entity.CanalNotification;
import com.notification.repository.CanalNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service

public class CanalNotificationService {



    private final CanalNotificationRepository canalRepository;
    public CanalNotificationService(CanalNotificationRepository canalRepository) {
        this.canalRepository = canalRepository;
    }
    @Transactional
    public CanalNotification creerCanal(CanalNotification canal) {
        return canalRepository.save(canal);
    }

    public List<CanalNotification> obtenirTousLesCanaux() {
        return canalRepository.findAll();
    }

    public List<CanalNotification> obtenirCanauxActifs() {
        return canalRepository.findByEstActif(true);
    }

    public List<CanalNotification> obtenirCanauxParType(String typeCanal) {
        return canalRepository.findByTypeCanal(typeCanal);
    }

    public List<CanalNotification> obtenirCanauxActifsParType(String typeCanal) {
        return canalRepository.findByTypeCanalAndEstActif(typeCanal, true);
    }

    public Optional<CanalNotification> obtenirCanalParId(Long id) {
        return canalRepository.findById(id);
    }

    @Transactional
    public CanalNotification modifierCanal(Long id, CanalNotification canalModifie) {
        return canalRepository.findById(id)
                .map(canal -> {
                    canal.setTypeCanal(canalModifie.getTypeCanal());
                    canal.setNomCanal(canalModifie.getNomCanal());
                    canal.setEstActif(canalModifie.getEstActif());
                    canal.setConfiguration(canalModifie.getConfiguration());
                    canal.setPriorite(canalModifie.getPriorite());
                    canal.setDescription(canalModifie.getDescription());
                    canal.setParametres(canalModifie.getParametres());
                    canal.setSupportMQTT(canalModifie.getSupportMQTT());
                    canal.setTopicMQTT(canalModifie.getTopicMQTT());
                    return canalRepository.save(canal);
                })
                .orElseThrow(() -> new RuntimeException("Canal non trouvé avec l'ID: " + id));
    }

    @Transactional
    public void supprimerCanal(Long id) {
        canalRepository.deleteById(id);
    }

    @Transactional
    public CanalNotification activerCanal(Long id) {
        return canalRepository.findById(id)
                .map(canal -> {
                    canal.setEstActif(true);
                    return canalRepository.save(canal);
                })
                .orElseThrow(() -> new RuntimeException("Canal non trouvé avec l'ID: " + id));
    }

    @Transactional
    public CanalNotification desactiverCanal(Long id) {
        return canalRepository.findById(id)
                .map(canal -> {
                    canal.setEstActif(false);
                    return canalRepository.save(canal);
                })
                .orElseThrow(() -> new RuntimeException("Canal non trouvé avec l'ID: " + id));
    }

    public List<CanalNotification> obtenirCanauxMQTT() {
        return canalRepository.findBySupportMQTT(true);
    }


}

