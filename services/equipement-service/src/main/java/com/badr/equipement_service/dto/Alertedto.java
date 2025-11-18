package com.badr.equipement_service.dto;

import com.badr.equipement_service.entity.Alerte;
import java.time.LocalDateTime;

public class Alertedto {
    private Long id;
    private Long equipementId;
    private String typeAlerte;
    private String severite;
    private String message;
    private LocalDateTime dateCreation;
    private LocalDateTime dateAccusee;
    private String statut;
    private Equipmentdto equipmentdto; // Cette propriété est optionnelle

    public Alertedto() {}

    // Constructeur depuis l'entité (CORRIGÉ)
    public Alertedto(Alerte alerte) {
        this.id = alerte.getId();
        this.equipementId = alerte.getEquipementId();
        this.typeAlerte = alerte.getTypeAlerte();
        this.severite = alerte.getSeverite();
        this.message = alerte.getMessage();
        this.dateCreation = alerte.getDateCreation();
        this.dateAccusee = alerte.getDateAccusee();
        this.statut = alerte.getStatut();

        // CORRECTION : Ne pas créer un nouvel Equipmentdto ici pour casser la boucle.
        // Si vous avez absolument besoin des infos de l'équipement,
        // créez un constructeur spécifique pour un DTO "léger".
        // Pour la plupart des cas (comme lister les alertes), c'est inutile.
        this.equipmentdto = null;
    }

    // --- GETTERS ET SETTERS ---
    // (Aucun changement nécessaire ici)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEquipementId() { return equipementId; }
    public void setEquipementId(Long equipementId) { this.equipementId = equipementId; }
    public String getTypeAlerte() { return typeAlerte; }
    public void setTypeAlerte(String typeAlerte) { this.typeAlerte = typeAlerte; }
    public String getSeverite() { return severite; }
    public void setSeverite(String severite) { this.severite = severite; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDateAccusee() { return dateAccusee; }
    public void setDateAccusee(LocalDateTime dateAccusee) { this.dateAccusee = dateAccusee; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Equipmentdto getEquipmentdto() { return equipmentdto; }
    public void setEquipmentdto(Equipmentdto equipmentdto) { this.equipmentdto = equipmentdto; }
}
