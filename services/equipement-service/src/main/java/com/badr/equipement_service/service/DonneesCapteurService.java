package com.badr.equipement_service.service;
import com.badr.equipement_service.dto.DonneesCapteurdto;
import com.badr.equipement_service.entity.DonneesCapteur;
import com.badr.equipement_service.repository.DonneesCapteurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@Transactional
public class DonneesCapteurService {



    private final DonneesCapteurRepository donneesCapteurRepository;
    public DonneesCapteurService(DonneesCapteurRepository donneesCapteurRepository) {
        this.donneesCapteurRepository = donneesCapteurRepository;
    }
    /**
     * methode pour recuperer tout les donnees des capteur
     * @return
     */
    @Transactional(readOnly = true)
    public List<DonneesCapteurdto> getAllDonneesCapteur() {
        return donneesCapteurRepository.findAll()
                .stream()
                .map(DonneesCapteurdto::new)
                .collect(Collectors.toList());
    }

    /**
     * get les donnes d un capteur par son id
     * @param id recuperer du front end
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<DonneesCapteurdto> getDonneesCapteurById(Long id) {
        return donneesCapteurRepository.findById(id)
                .map(DonneesCapteurdto::new);
    }

    /**
     * creer les donnes d un capteur
     * @param donneesCapteur  recuprer de puis le dto dans le controller
     * @return
     */
    public DonneesCapteur createDonneesCapteur(DonneesCapteurdto donneesCapteur) {
        donneesCapteur.setHorodatage(new Timestamp(System.currentTimeMillis()));
        DonneesCapteur donneesCapteur1=new DonneesCapteur(donneesCapteur);
        return donneesCapteurRepository.save(donneesCapteur1);
    }

    /**
     *methode pour modifier les donnes d un capteur
     * @param donneesDetails les donnes a recuperer a partir d un dto dans controller
     * @return
     */
    public DonneesCapteur updateDonneesCapteur( DonneesCapteurdto donneesDetails) {
        Long id =donneesDetails.getIdentifiant();

        return donneesCapteurRepository.findById(id)
                .map(donnees -> {
                    donnees.setTypeCapteur(donneesDetails.getTypeCapteur());
                    donnees.setValeur(donneesDetails.getValeur());
                    donnees.setUnite(donneesDetails.getUnite());
                    donnees.setQualite(donneesDetails.getQualite());

                    return donneesCapteurRepository.save(donnees);
                })
                .orElseThrow(() -> new RuntimeException("Données de capteur non trouvées avec l'ID: " + id));
    }

    /**
     * sup les donnes d un capteur
     * @param id a recuperer dans front end ou dans un autre sservice
     */
    public void deleteDonneesCapteur(Long id) {
        donneesCapteurRepository.deleteById(id);
    }

    /**
     * recuperer les donnes liee a un capteur
     * @param equipementId  a recuperer das front end
     * @return
     */
    @Transactional(readOnly = true)
    public List<DonneesCapteurdto> getDonneesByEquipement(Long equipementId) {
        return donneesCapteurRepository.findByIdentifiantEquipement(equipementId)
                .stream()
                .map(DonneesCapteurdto::new)
                .collect(Collectors.toList());
    }

    /**
     *recuuperer les donnes selon le types des capteur
     * @param typeCapteur  a reecuperer dans depuis le dto  dans le controller ou bien dans front end
     * @return
     */
    @Transactional(readOnly = true)
    public List<DonneesCapteurdto> getDonneesByTypeCapteur(String typeCapteur) {
        return donneesCapteurRepository.findByTypeCapteur(typeCapteur)
                .stream()
                .map(DonneesCapteurdto::new)
                .collect(Collectors.toList());
    }

    /**
     * recuperer les donnes des capteurs selon equioement et le types
     * @param equipementId
     * @param typeCapteur
     * @return
     */
    @Transactional(readOnly = true)
    public List<DonneesCapteurdto> getDonneesByEquipementAndType(Long equipementId, String typeCapteur) {
        return donneesCapteurRepository.findByEquipementAndTypeCapteurOrderByHorodatageDesc(equipementId, typeCapteur)
                .stream()
                .map(DonneesCapteurdto::new)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<DonneesCapteurdto> getDonneesByDateRange(Timestamp dateDebut, Timestamp dateFin) {
        return donneesCapteurRepository.findByHorodatageBetween(dateDebut, dateFin)
                .stream()
                .map(DonneesCapteurdto::new)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<DonneesCapteurdto> getRecentDataByEquipement(Long equipementId, int heures) {
        Timestamp depuis = Timestamp.valueOf(LocalDateTime.now().minusHours(heures));
        return donneesCapteurRepository.findRecentDataByEquipement(equipementId, depuis)
                .stream()
                .map(DonneesCapteurdto::new)
                .collect(Collectors.toList());
    }

    public Double getAverageValueByEquipementAndType(Long equipementId, String typeCapteur, int heures) {
        Timestamp depuis = Timestamp.valueOf(LocalDateTime.now().minusHours(heures));
        return donneesCapteurRepository.findAverageValueByEquipementAndTypeCapteur(equipementId, typeCapteur, depuis);
    }

    public DonneesCapteurdto getLatestDataByEquipement(Long equipementId) {
        DonneesCapteur donneesCapteur= donneesCapteurRepository.findLatestByEquipement(equipementId);
        return new DonneesCapteurdto(donneesCapteur);
    }

    public List<DonneesCapteurdto> getDonneesByQualite(String qualite) {
        return donneesCapteurRepository.findByQualite(qualite)
                .stream()
                .map(DonneesCapteurdto::new)
                .collect(Collectors.toList());
    }
}