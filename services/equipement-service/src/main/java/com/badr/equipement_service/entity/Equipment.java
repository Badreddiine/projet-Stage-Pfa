package com.badr.equipement_service.entity;

import com.badr.equipement_service.dto.Equipmentdto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    @Column(nullable = false)
    private String nom;

    @Column(name = "type_equipment")
    private String typeEquipment;

    @Column(nullable = false)
    private String emplacement;

    @Column(nullable = false)
    private String statut;

    @Column(name = "derniere_maintenance")
    private LocalDateTime derniereMaintenance;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "equipment_specifications", joinColumns = @JoinColumn(name = "equipment_id"))
    @MapKeyColumn(name = "spec_key")
    @Column(name = "spec_value")
    private Map<String, String> specifications = new HashMap<>(); // CORRECTION: Object -> String

    @Column(name = "type_connexion")
    private String typeConnexion;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "equipment_parametres_connexion", joinColumns = @JoinColumn(name = "equipment_id"))
    @MapKeyColumn(name = "param_key")
    @Column(name = "param_value")
    private Map<String, String> parametresConnexion = new HashMap<>(); // CORRECTION: Object -> String

    @Column(name = "simulation_active")
    private Boolean simulationActive = false;

    @Column(name = "adresse_reseau")
    private String adresseReseau;

    @Column(name = "port_ecoute")
    private Integer portEcoute;

    @Column(name = "derniere_connexion")
    private LocalDateTime derniereConnexion;

    @Column(name = "tentatives_connexion")
    private Integer tentativesConnexion = 0;

    @Column(name = "compte_verrouille")
    private Boolean compteVerrouille = false;

    @Column(name = "est_actif")
    private Boolean estActif = true;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Alerte> alertes = new ArrayList<>();

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DonneesCapteur> donneesCapteurs = new ArrayList<>();

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<RegleSeuil> reglesSeuil = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (derniereMaintenance == null) {
            derniereMaintenance = LocalDateTime.now();
        }
        if (derniereConnexion == null) {
            derniereConnexion = LocalDateTime.now();
        }
    }

    // Constructeur par défaut (requis par JPA)
    public Equipment() {
    }

    public Equipment(Long id) {
        this.identifiant = id;
    }

    // Constructeur depuis DTO (corrigé pour gérer la conversion de Map<String, Object> en Map<String, String>)
    public Equipment(Equipmentdto equipmentdto) {
        this.identifiant = equipmentdto.getIdentifiant();
        this.nom = equipmentdto.getNom();
        this.typeEquipment = equipmentdto.getTypeEquipment();
        this.emplacement = equipmentdto.getEmplacement();
        this.statut = equipmentdto.getStatut();
        this.derniereMaintenance = equipmentdto.getDerniereMaintenance();
        this.typeConnexion = equipmentdto.getTypeConnexion();
        this.simulationActive = equipmentdto.getSimulationActive();
        this.adresseReseau = equipmentdto.getAdresseReseau();
        this.compteVerrouille = equipmentdto.getCompteVerrouille();
        this.estActif = equipmentdto.getEstActif();
        this.tentativesConnexion = 0;
        this.alertes = new ArrayList<>();
        this.donneesCapteurs = new ArrayList<>();
        this.reglesSeuil = new ArrayList<>();

        // Conversion sécurisée des Maps
        if (equipmentdto.getSpecifications() != null) {
            this.specifications = equipmentdto.getSpecifications().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
        }
        if (equipmentdto.getParametresConnexion() != null) {
            this.parametresConnexion = equipmentdto.getParametresConnexion().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
        }
    }

    // Getters et Setters (pas de changement de signature, mais le type interne a changé)

    public Long getIdentifiant() { return identifiant; }
    public void setIdentifiant(Long identifiant) { this.identifiant = identifiant; }
    public String getNom() { return nom; }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getTypeEquipment() {
        return typeEquipment;
    }
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
    public List<Alerte> getAlertes() { return alertes; }
    public void setAlertes(List<Alerte> alertes) { this.alertes = alertes; }
    public List<DonneesCapteur> getDonneesCapteurs() { return donneesCapteurs; }
    public void setDonneesCapteurs(List<DonneesCapteur> donneesCapteurs) { this.donneesCapteurs = donneesCapteurs; }
    public List<RegleSeuil> getReglesSeuil() { return reglesSeuil; }
    public void setReglesSeuil(List<RegleSeuil> reglesSeuil) { this.reglesSeuil = reglesSeuil; }
}
