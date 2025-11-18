package com.badr.equipement_service.entity;

import com.badr.equipement_service.dto.DonneesCapteurdto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "donnees_capteur")
public class DonneesCapteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    @Column(name = "identifiant_equipment", nullable = false)
    private Long identifiantEquipement;

    @Column(name = "type_capteur", nullable = false)
    private String typeCapteur;

    @Column(nullable = false)
    private Double valeur;

    @Column(nullable = false)
    private String unite;

    @Column(nullable = false)
    private Timestamp horodatage;

    @Column(nullable = false)
    private String qualite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identifiant_equipment", insertable = false, updatable = false)
    @JsonBackReference
    private Equipment equipment;

    @PrePersist
    protected void onCreate() {
        if (horodatage == null) {
            horodatage = new Timestamp(System.currentTimeMillis());
        }
        if (qualite == null) {
            qualite = "GOOD";
        }
    }

    public DonneesCapteur() {
    }

    public DonneesCapteur(DonneesCapteurdto capteurDto) {
        this.identifiant = capteurDto.getIdentifiant();
        this.identifiantEquipement = capteurDto.getIdentifiantEquipement();
        this.typeCapteur = capteurDto.getTypeCapteur();
        this.valeur = capteurDto.getValeur();
        this.unite = capteurDto.getUnite();
        this.horodatage = capteurDto.getHorodatage();
        this.qualite = capteurDto.getQualite();
        if (capteurDto.getEquipmentdto() != null) {
            this.equipment = new Equipment(capteurDto.getEquipmentdto());
        }
    }

    // Getters et Setters (inchangés)
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
    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }
}