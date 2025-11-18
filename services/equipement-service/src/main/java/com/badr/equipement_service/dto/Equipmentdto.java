package com.badr.equipement_service.dto;

import com.badr.equipement_service.entity.Equipment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Equipmentdto {

    private Long identifiant;
    private String nom;
    private String typeEquipment;
    private String emplacement;
    private String statut;
    private LocalDateTime derniereMaintenance;
    private Map<String, String> specifications; // Le type doit être Map<String, String> pour correspondre à l'entité
    private String typeConnexion;
    private Map<String, String> parametresConnexion; // Le type doit être Map<String, String>
    private Boolean simulationActive;
    private String adresseReseau;
    private Integer portEcoute;
    private LocalDateTime derniereConnexion;
    private Integer tentativesConnexion;
    private Boolean compteVerrouille;
    private Boolean estActif;
    private List<Alertedto> alertes;
    private List<DonneesCapteurdto> donneesCapteurs;
    private List<RegleSeuildto> reglesSeui;

    // Constructeur par défaut robuste
    public Equipmentdto() {
        this.specifications = new HashMap<>();
        this.parametresConnexion = new HashMap<>();
        this.alertes = new ArrayList<>();
        this.donneesCapteurs = new ArrayList<>();
        this.reglesSeui = new ArrayList<>();
    }

    // Constructeur depuis l'entité (entièrement corrigé)
    public Equipmentdto(Equipment equipment) {
        this.identifiant = equipment.getIdentifiant();
        this.nom = equipment.getNom();
        this.typeEquipment = equipment.getTypeEquipment();
        this.emplacement = equipment.getEmplacement();
        this.statut = equipment.getStatut();
        this.derniereMaintenance = equipment.getDerniereMaintenance();
        this.typeConnexion = equipment.getTypeConnexion();
        this.simulationActive = equipment.getSimulationActive();
        this.adresseReseau = equipment.getAdresseReseau();
        this.portEcoute = equipment.getPortEcoute();
        this.derniereConnexion = equipment.getDerniereConnexion();
        this.tentativesConnexion = equipment.getTentativesConnexion();
        this.compteVerrouille = equipment.getCompteVerrouille();
        this.estActif = equipment.getEstActif();

        // CORRECTION : Copie sécurisée des maps
        this.specifications = equipment.getSpecifications() != null ? new HashMap<>(equipment.getSpecifications()) : new HashMap<>();
        this.parametresConnexion = equipment.getParametresConnexion() != null ? new HashMap<>(equipment.getParametresConnexion()) : new HashMap<>();
        // Mapping des listes d'enfants si nécessaire (peut nécessiter des DTOs pour Alerte, DonneesCapteur, RegleSeuil)
        this.alertes = equipment.getAlertes() != null ? equipment.getAlertes().stream().map(Alertedto::new).collect(Collectors.toList()) : new ArrayList<>();
        this.donneesCapteurs = equipment.getDonneesCapteurs() != null ? equipment.getDonneesCapteurs().stream().map(DonneesCapteurdto::new).collect(Collectors.toList()) : new ArrayList<>();
        this.reglesSeui = equipment.getReglesSeuil() != null ? equipment.getReglesSeuil().stream().map(RegleSeuildto::new).collect(Collectors.toList()) : new ArrayList<>();
    }

    // --- GETTERS ET SETTERS ---
    // Assurez-vous que les getters/setters pour les maps utilisent Map<String, String>
    public Long getIdentifiant() { return identifiant; }
    public void setIdentifiant(Long identifiant) { this.identifiant = identifiant; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getTypeEquipment() { return typeEquipment; }
    public void setTypeEquipment(String typeEquipment) { this.typeEquipment = typeEquipment; }
    public String getEmplacement() { return emplacement; }
    public void setEmplacement(String emplacement) { this.emplacement = emplacement; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDerniereMaintenance() { return derniereMaintenance; }
    public void setDerniereMaintenance(LocalDateTime derniereMaintenance) { this.derniereMaintenance = derniereMaintenance; }
    public Map<String, String> getSpecifications() { return specifications; }
    public void setSpecifications(Map<String, String> specifications) { this.specifications = specifications; }
    public String getTypeConnexion() { return typeConnexion; }
    public void setTypeConnexion(String typeConnexion) { this.typeConnexion = typeConnexion; }
    public Map<String, String> getParametresConnexion() { return parametresConnexion; }
    public void setParametresConnexion(Map<String, String> parametresConnexion) { this.parametresConnexion = parametresConnexion; }
    public Boolean getSimulationActive() { return simulationActive; }
    public void setSimulationActive(Boolean simulationActive) { this.simulationActive = simulationActive; }
    public String getAdresseReseau() { return adresseReseau; }
    public void setAdresseReseau(String adresseReseau) { this.adresseReseau = adresseReseau; }
    public Integer getPortEcoute() { return portEcoute; }
    public void setPortEcoute(Integer portEcoute) { this.portEcoute = portEcoute; }
    public LocalDateTime getDerniereConnexion() { return derniereConnexion; }
    public void setDerniereConnexion(LocalDateTime derniereConnexion) { this.derniereConnexion = derniereConnexion; }
    public Integer getTentativesConnexion() { return tentativesConnexion; }
    public void setTentativesConnexion(Integer tentativesConnexion) { this.tentativesConnexion = tentativesConnexion; }
    public Boolean getCompteVerrouille() { return compteVerrouille; }
    public void setCompteVerrouille(Boolean compteVerrouille) { this.compteVerrouille = compteVerrouille; }
    public Boolean getEstActif() { return estActif; }
    public void setEstActif(Boolean estActif) { this.estActif = estActif; }
    public List<Alertedto> getAlertes() { return alertes; }
    public void setAlertes(List<Alertedto> alertes) { this.alertes = alertes; }
    public List<DonneesCapteurdto> getDonneesCapteurs() { return donneesCapteurs; }
    public void setDonneesCapteurs(List<DonneesCapteurdto> donneesCapteurs) { this.donneesCapteurs = donneesCapteurs; }
    public List<RegleSeuildto> getReglesSeui() { return reglesSeui; }
    public void setReglesSeui(List<RegleSeuildto> reglesSeui) { this.reglesSeui = reglesSeui; }
}
