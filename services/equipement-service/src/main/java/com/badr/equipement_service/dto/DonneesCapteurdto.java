package com.badr.equipement_service.dto;

import com.badr.equipement_service.entity.DonneesCapteur;
import java.sql.Timestamp;

public class DonneesCapteurdto {
    private Long identifiant;
    private Long identifiantEquipement;
    private String typeCapteur;
    private Double valeur;
    private String unite;
    private Timestamp horodatage;
    private String qualite;
    private Equipmentdto equipmentdto;

    public DonneesCapteurdto() {}

    // Constructeur depuis l'entité (CORRIGÉ)
    public DonneesCapteurdto(DonneesCapteur donneesCapteur) {
        this.identifiant = donneesCapteur.getIdentifiant();
        this.identifiantEquipement = donneesCapteur.getIdentifiantEquipement();
        this.typeCapteur = donneesCapteur.getTypeCapteur();
        this.valeur = donneesCapteur.getValeur();
        this.unite = donneesCapteur.getUnite();
        this.horodatage = donneesCapteur.getHorodatage();
        this.qualite = donneesCapteur.getQualite();

        // CORRECTION : Casser la boucle
        this.equipmentdto = null;
    }

    // --- GETTERS ET SETTERS ---
    // (Aucun changement nécessaire ici)
    public Long getIdentifiant() { return identifiant; }
    public void setIdentifiant(Long identifiant) { this.identifiant = identifiant; }
    public Long getIdentifiantEquipement() { return identifiantEquipement; }
    public void setIdentifiantEquipement(Long identifiantEquipement) { this.identifiantEquipement = identifiantEquipement; }
    public String getTypeCapteur() { return typeCapteur; }
    public void setTypeCapteur(String typeCapteur) { this.typeCapteur = typeCapteur; }
    public Double getValeur() { return valeur; }
    public void setValeur(Double valeur) { this.valeur = valeur; }
    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }
    public Timestamp getHorodatage() { return horodatage; }
    public void setHorodatage(Timestamp horodatage) { this.horodatage = horodatage; }
    public String getQualite() { return qualite; }
    public void setQualite(String qualite) { this.qualite = qualite; }
    public Equipmentdto getEquipmentdto() { return equipmentdto; }
    public void setEquipmentdto(Equipmentdto equipmentdto) { this.equipmentdto = equipmentdto; }
}
