package com.badr.equipement_service.service;

import com.badr.equipement_service.dto.Equipmentdto;
import com.badr.equipement_service.entity.Equipment;
import com.badr.equipement_service.repository.EquipmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * Méthode pour retourner tous les équipements
     * @return Liste des DTOs d'équipements
     */
    @Transactional(readOnly = true)
    public List<Equipmentdto> getAllEquipments() {
        try {
            return equipmentRepository.findAll()
                    .stream()
                    .map(Equipmentdto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des équipements", e);
        }
    }

    /**
     * Trouver un équipement par ID
     * @param id L'identifiant de l'équipement
     * @return Un Optional contenant le DTO de l'équipement
     */
    @Transactional(readOnly = true)
    public Optional<Equipmentdto> getEquipmentById(Long id) {
        try {
            if (id == null) {
                return Optional.empty();
            }
            return equipmentRepository.findById(id)
                    .map(Equipmentdto::new);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de l'équipement", e);
        }
    }

    /**
     * Méthode pour créer un équipement
     * @param equipmentdto Le DTO de l'équipement à créer
     * @return L'entité sauvegardée
     */
    public Equipment createEquipment(Equipmentdto equipmentdto) {
        try {
            if (equipmentdto == null) {
                throw new IllegalArgumentException("Le DTO de l'équipement ne peut pas être null");
            }

            Equipment equipment = new Equipment(equipmentdto);
            equipment.setDerniereConnexion(LocalDateTime.now());

            return equipmentRepository.save(equipment);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'équipement", e);
        }
    }

    /**
     * Méthode pour modifier un équipement
     * @param equipmentDetailsdto Le DTO contenant les nouvelles données
     * @return L'entité mise à jour
     */
    public Equipment updateEquipment(Equipmentdto equipmentDetailsdto) {
        try {
            if (equipmentDetailsdto == null) {
                throw new IllegalArgumentException("Le DTO de l'équipement ne peut pas être null");
            }

            Long id = equipmentDetailsdto.getIdentifiant();
            if (id == null) {
                throw new IllegalArgumentException("L'ID de l'équipement ne peut pas être null pour une mise à jour");
            }


            return equipmentRepository.findById(id)
                    .map(equipment -> {
                        // Mise à jour des champs
                        if (equipmentDetailsdto.getNom() != null) {
                            equipment.setNom(equipmentDetailsdto.getNom());
                        }
                        if (equipmentDetailsdto.getTypeEquipment() != null) {
                            equipment.setTypeEquipment(equipmentDetailsdto.getTypeEquipment());
                        }
                        if (equipmentDetailsdto.getEmplacement() != null) {
                            equipment.setEmplacement(equipmentDetailsdto.getEmplacement());
                        }
                        if (equipmentDetailsdto.getStatut() != null) {
                            equipment.setStatut(equipmentDetailsdto.getStatut());
                        }
                        if (equipmentDetailsdto.getDerniereMaintenance() != null) {
                            equipment.setDerniereMaintenance(equipmentDetailsdto.getDerniereMaintenance());
                        }
                        if (equipmentDetailsdto.getSpecifications() != null) {
                            equipment.setSpecifications(equipmentDetailsdto.getSpecifications());
                        }
                        if (equipmentDetailsdto.getTypeConnexion() != null) {
                            equipment.setTypeConnexion(equipmentDetailsdto.getTypeConnexion());
                        }
                        if (equipmentDetailsdto.getParametresConnexion() != null) {
                            equipment.setParametresConnexion(equipmentDetailsdto.getParametresConnexion());
                        }
                        if (equipmentDetailsdto.getSimulationActive() != null) {
                            equipment.setSimulationActive(equipmentDetailsdto.getSimulationActive());
                        }
                        if (equipmentDetailsdto.getAdresseReseau() != null) {
                            equipment.setAdresseReseau(equipmentDetailsdto.getAdresseReseau());
                        }
                        if (equipmentDetailsdto.getPortEcoute() != null) {
                            equipment.setPortEcoute(equipmentDetailsdto.getPortEcoute());
                        }
                        if (equipmentDetailsdto.getEstActif() != null) {
                            equipment.setEstActif(equipmentDetailsdto.getEstActif());
                        }

                        return equipmentRepository.save(equipment);
                    })
                    .orElseThrow(() -> new RuntimeException("Équipement non trouvé avec l'ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'équipement", e);
        }
    }

    /**
     * Méthode pour supprimer un équipement par son ID
     * @param id L'identifiant de l'équipement à supprimer
     */
    public void deleteEquipment(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("L'ID ne peut pas être null");
            }

            if (!equipmentRepository.existsById(id)) {
                throw new RuntimeException("Équipement non trouvé avec l'ID: " + id);
            }

            equipmentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'équipement", e);
        }
    }

    /**
     * Méthode pour trouver des équipements par statut
     * @param statut Le statut recherché
     * @return Une liste des DTOs des équipements
     */
    @Transactional(readOnly = true)
    public List<Equipmentdto> getEquipmentsByStatut(String statut) {
        try {
            if (statut == null || statut.trim().isEmpty()) {
                throw new IllegalArgumentException("Le statut ne peut pas être null ou vide");
            }

            return equipmentRepository.findByStatut(statut)
                    .stream()
                    .map(Equipmentdto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des équipements par statut", e);
        }
    }

    /**
     * Trouver par type d'équipement
     * @param typeEquipment Le type d'équipement
     * @return Une liste des DTOs des équipements
     */
    @Transactional(readOnly = true)
    public List<Equipmentdto> getEquipmentsByType(String typeEquipment) {
        try {
            if (typeEquipment == null || typeEquipment.trim().isEmpty()) {
                throw new IllegalArgumentException("Le type d'équipement ne peut pas être null ou vide");
            }

            return equipmentRepository.findByTypeEquipment(typeEquipment)
                    .stream()
                    .map(Equipmentdto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des équipements par type", e);
        }
    }

    /**
     * Récupérer les équipements actifs
     * @return Liste des équipements actifs
     */
    @Transactional(readOnly = true)
    public List<Equipmentdto> getActiveEquipments() {
        try {
            return equipmentRepository.findActiveAndUnlockedEquipments()
                    .stream()
                    .map(Equipmentdto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des équipements actifs", e);
        }
    }

    /**
     * Mettre à jour le statut de connexion d'un équipement
     * @param id ID de l'équipement
     * @param derniereConnexion Dernière connexion
     * @param tentatives Nombre de tentatives
     * @return L'équipement mis à jour
     */
    public Equipment updateConnectionStatus(Long id, LocalDateTime derniereConnexion, Integer tentatives) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("L'ID ne peut pas être null");
            }

            return equipmentRepository.findById(id)
                    .map(equipment -> {
                        equipment.setDerniereConnexion(derniereConnexion);
                        equipment.setTentativesConnexion(tentatives);

                        // Verrouiller le compte si trop de tentatives
                        if (tentatives != null && tentatives > 5) {
                            equipment.setCompteVerrouille(true);
                        }

                        return equipmentRepository.save(equipment);
                    })
                    .orElseThrow(() -> new RuntimeException("Équipement non trouvé avec l'ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du statut de connexion", e);
        }
    }

    /**
     * Déverrouiller un équipement
     * @param id ID de l'équipement
     */
    public void unlockEquipment(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("L'ID ne peut pas être null");
            }

            equipmentRepository.findById(id)
                    .ifPresentOrElse(equipment -> {
                        equipment.setCompteVerrouille(false);
                        equipment.setTentativesConnexion(0);
                        equipmentRepository.save(equipment);
                    }, () -> {
                        throw new RuntimeException("Équipement non trouvé avec l'ID: " + id);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du déverrouillage de l'équipement", e);
        }
    }

    /**
     * Compter les équipements par statut
     * @param statut Le statut
     * @return Le nombre d'équipements
     */
    @Transactional(readOnly = true)
    public Long getEquipmentCountByStatut(String statut) {
        try {
            if (statut == null || statut.trim().isEmpty()) {
                throw new IllegalArgumentException("Le statut ne peut pas être null ou vide");
            }
            return equipmentRepository.countByStatut(statut);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du comptage des équipements", e);
        }
    }

    /**
     * Récupérer un équipement (alias pour getEquipmentById)
     * @param id ID de l'équipement
     * @return Le DTO de l'équipement
     */
    @Transactional(readOnly = true)
    public Optional<Equipmentdto> getEquipment(Long id) {
        return getEquipmentById(id);
    }
}