package com.badr.equipement_service.entity;

import com.badr.equipement_service.dto.RegleSeuildto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "regle_seuil")
public class RegleSeuil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    @Column(name = "identifiant_equipment", nullable = false)
    private Long identifiantEquipement;

    @Column(nullable = false)
    private String parametre;

    @Column(name = "valeur_min", nullable = false)
    private Double valeurMin;

    @Column(name = "valeur_max", nullable = false)
    private Double valeurMax;

    @Column(name = "niveau_alerte", nullable = false)
    private String niveauAlerte;

    @Column(nullable = false)
    private Boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identifiant_equipment", insertable = false, updatable = false)
    @JsonBackReference
    private Equipment equipment;

    public RegleSeuil() {
    }

    public RegleSeuil(RegleSeuildto regleSeuildto) {
        this.identifiant = regleSeuildto.getIdentifiant();
        this.identifiantEquipement = regleSeuildto.getIdentifiantEquipement();
        this.parametre = regleSeuildto.getParametre();
        this.valeurMin = regleSeuildto.getValeurMin();
        this.valeurMax = regleSeuildto.getValeurMax();
        this.niveauAlerte = regleSeuildto.getNiveauAlerte();
        this.actif = regleSeuildto.getActif();
        if (regleSeuildto.getEquipement() != null) {
            this.equipment = new Equipment(regleSeuildto.getEquipement());
        }
    }

    // Getters et Setters (inchangés)
    public Long getIdentifiant() { return identifiant; }
    public void setIdentifiant(Long identifiant) { this.identifiant = identifiant; }
    public Long getIdentifiantEquipement() { return identifiantEquipement; }
    public void setIdentifiantEquipement(Long identifiantEquipement) { this.identifiantEquipement = identifiantEquipement; }
    public String getParametre() { return parametre; }
    public void setParametre(String parametre) { this.parametre = parametre; }
    public Double getValeurMin() { return valeurMin; }
    public void setValeurMin(Double valeurMin) { this.valeurMin = valeurMin; }
    public Double getValeurMax() { return valeurMax; }
    public void setValeurMax(Double valeurMax) { this.valeurMax = valeurMax; }
    public String getNiveauAlerte() { return niveauAlerte; }
    public void setNiveauAlerte(String niveauAlerte) { this.niveauAlerte = niveauAlerte; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }
}
