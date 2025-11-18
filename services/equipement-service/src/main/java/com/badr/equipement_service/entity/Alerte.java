package com.badr.equipement_service.entity;

import com.badr.equipement_service.dto.Alertedto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerte")
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_id", nullable = false)
    private Long equipementId;

    @Column(name = "type_alerte", nullable = false)
    private String typeAlerte;

    @Column(nullable = false)
    private String severite;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_accusee")
    private LocalDateTime dateAccusee;

    @Column(nullable = false)
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", insertable = false, updatable = false)
    @JsonBackReference
    private Equipment equipment;

    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
        if (statut == null) {
            statut = "ACTIVE";
        }
    }

    // CORRECTION: Ajout du constructeur par défaut requis par JPA
    public Alerte() {
    }

    public Alerte(Long id) {
        this.id = id;
    }

    public Alerte(Alertedto alerteDto) {
        this.id = alerteDto.getId();
        this.equipementId = alerteDto.getEquipementId();
        this.typeAlerte = alerteDto.getTypeAlerte();
        this.severite = alerteDto.getSeverite();
        this.message = alerteDto.getMessage();
        this.dateCreation = alerteDto.getDateCreation();
        this.dateAccusee = alerteDto.getDateAccusee();
        this.statut = alerteDto.getStatut();
        if (alerteDto.getEquipmentdto() != null) {
            this.equipment = new Equipment(alerteDto.getEquipmentdto());
        }
    }

    // Getters et Setters (inchangés)
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
    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }
}
