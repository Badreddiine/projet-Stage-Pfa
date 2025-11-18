package com.badr.equipement_service.service;

import com.badr.equipement_service.dto.RegleSeuildto;
import com.badr.equipement_service.entity.RegleSeuil;
import com.badr.equipement_service.repository.RegleSeuilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@Transactional
public class RegleSeuilService {

    //    @Transactional(readOnly = true)
//    public List<CalendrierDTO> findAllCalendrier() {
//        return calendrierRepository.findAll()
//                .stream()
//                .map(CalendrierDTO::new)
//                .collect(Collectors.toList());
//    }
    private final RegleSeuilRepository regleSeuilRepository;
    public RegleSeuilService(RegleSeuilRepository regleSeuilRepository) {
        this.regleSeuilRepository = regleSeuilRepository;
    }

    /**
     * recuperer tout les regles seuil enregestrer
     * @return
     */
    @Transactional(readOnly = true)
    public List<RegleSeuildto> getAllReglesSeui() {
        return regleSeuilRepository.findAll()
                .stream()
                .map(RegleSeuildto::new)
                .collect(Collectors.toList());
    }


    /**
     * recuperer les regeles seuil par id
     * @param id
     * @return
     */
    public Optional<RegleSeuildto> getRegleSeuilById(Long id) {
        RegleSeuil regleSeuil = regleSeuilRepository.findById(id).orElse(null);
        RegleSeuildto regle = new RegleSeuildto(regleSeuil) ;
        return Optional.of(regle);
    }

    /**
     * methode pour creer des regles seuil
     * @param regleSeuil a construires a partir du dto
     * @return
     */
    public RegleSeuil createRegleSeuil(RegleSeuildto regleSeuil) {
        return regleSeuilRepository.save(new RegleSeuil(regleSeuil));
    }

    /**
     * update des regles seuil
     * @param regleDetails
     * @return
     */
    public RegleSeuil updateRegleSeuil( RegleSeuildto regleDetails) {
        Long id =regleDetails.getIdentifiant();

        return regleSeuilRepository.findById(id)
                .map(regle -> {
                    regle.setParametre(regleDetails.getParametre());
                    regle.setValeurMin(regleDetails.getValeurMin());
                    regle.setValeurMax(regleDetails.getValeurMax());
                    regle.setNiveauAlerte(regleDetails.getNiveauAlerte());
                    regle.setActif(regleDetails.getActif());

                    return regleSeuilRepository.save(regle);
                })
                .orElseThrow(() -> new RuntimeException("Règle de seuil non trouvée avec l'ID: " + id));
    }

    /**
     * suprimer des regles seuil par son id
     * @param id
     */
    public void deleteRegleSeuil(Long id) {
        regleSeuilRepository.deleteById(id);
    }

    /**
     * recuperer les regles seuil associer a un equipement
     * @param equipementId
     * @return
     */
    @Transactional(readOnly = true)
    public List<RegleSeuildto> getReglesByEquipement(Long equipementId) {
        return regleSeuilRepository.findByIdentifiantEquipement(equipementId)
                .stream()
                .map(RegleSeuildto::new)
                .collect(Collectors.toList());
    }

    /**
     * avoire les regles des equipement
     * @param equipementId
     * @return
     */
    @Transactional(readOnly = true)
    public List<RegleSeuildto> getActiveReglesByEquipement(Long equipementId) {
        return regleSeuilRepository.findActiveRulesByEquipement(equipementId)
                .stream()
                .map(RegleSeuildto::new)
                .collect(Collectors.toList());
    }

    /**
     * selon les paramteres
     * @param parametre
     * @return
     */
    @Transactional(readOnly = true)
    public List<RegleSeuildto> getReglesByParametre(String parametre) {
        return regleSeuilRepository.findByParametre(parametre)
                .stream()
                .map(RegleSeuildto::new)
                .collect(Collectors.toList());
    }

    /**
     * get regles selon leur niveau d urgence
     * @param niveauAlerte
     * @return
     */
    @Transactional(readOnly = true)
    public List<RegleSeuildto> getReglesByNiveauAlerte(String niveauAlerte) {
        return regleSeuilRepository.findByNiveauAlerte(niveauAlerte)
                .stream()
                .map(RegleSeuildto::new)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<RegleSeuildto> getActiveReglesByEquipementAndParametre(Long equipementId, String parametre) {
        return regleSeuilRepository.findActiveRulesByEquipementAndParametre(equipementId, parametre)
                .stream()
                .map(RegleSeuildto::new)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<RegleSeuildto> checkViolatedRules(Double valeur) {
        return regleSeuilRepository.findRulesViolatedByValue(valeur)
                .stream()
                .map(RegleSeuildto::new)
                .collect(Collectors.toList());
    }

    public RegleSeuil activateRegle(Long id) {

        return regleSeuilRepository.findById(id)
                .map(regle -> {
                    regle.setActif(true);
                    return regleSeuilRepository.save(regle);
                })
                .orElseThrow(() -> new RuntimeException("Règle de seuil non trouvée avec l'ID: " + id));
    }

    public RegleSeuil deactivateRegle(Long id) {

        return regleSeuilRepository.findById(id)
                .map(regle -> {
                    regle.setActif(false);
                    return regleSeuilRepository.save(regle);
                })
                .orElseThrow(() -> new RuntimeException("Règle de seuil non trouvée avec l'ID: " + id));
    }

    public Long getActiveRulesCountByEquipement(Long equipementId) {
        return regleSeuilRepository.countActiveRulesByEquipement(equipementId);
    }

    public boolean isValueWithinThresholds(Long equipementId, String parametre, Double valeur) {
        List<RegleSeuildto> regles = getActiveReglesByEquipementAndParametre(equipementId, parametre);

        for (RegleSeuildto regle : regles) {
            if (valeur < regle.getValeurMin() || valeur > regle.getValeurMax()) {
                return false;
            }
        }

        return true;
    }
}

