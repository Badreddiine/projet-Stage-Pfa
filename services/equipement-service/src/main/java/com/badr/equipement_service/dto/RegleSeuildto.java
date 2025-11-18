package com.badr.equipement_service.dto;

import com.badr.equipement_service.entity.RegleSeuil;

public class RegleSeuildto {
    private Long identifiant;
    private Long identifiantEquipement;
    private String parametre;
    private Double valeurMin;
    private Double valeurMax;
    private String niveauAlerte;
    private Boolean actif;
    private Equipmentdto equipement;

    public RegleSeuildto() {}

    // Constructeur depuis l'entité (CORRIGÉ)
    public RegleSeuildto(RegleSeuil regleSeuil) {
        this.identifiant = regleSeuil.getIdentifiant();
        this.identifiantEquipement = regleSeuil.getIdentifiantEquipement();
        this.parametre = regleSeuil.getParametre();
        this.valeurMin = regleSeuil.getValeurMin();
        this.valeurMax = regleSeuil.getValeurMax();
        this.niveauAlerte = regleSeuil.getNiveauAlerte();
        this.actif = regleSeuil.getActif();

        // CORRECTION : Casser la boucle
        this.equipement = null;
    }

    // --- GETTERS ET SETTERS ---
    // (Aucun changement nécessaire ici)
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
    public Equipmentdto getEquipement() { return equipement; }
    public void setEquipement(Equipmentdto equipement) { this.equipement = equipement; }
}
