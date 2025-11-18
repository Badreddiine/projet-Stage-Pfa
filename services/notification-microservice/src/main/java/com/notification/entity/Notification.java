package com.notification.entity;

import com.notification.dto.Notificationdto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "notification")

public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    
    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "canal", nullable = false)
    private String canal;
    
    @Column(name = "destinataire_id", nullable = false)
    private String destinataireId;
    
    @Column(name = "type_destinataire", nullable = false)
    private String typeDestinataire;
    
    @Column(name = "date_creation")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreation;
    
    @Column(name = "date_envoi")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateEnvoi;
    
    @Column(name = "date_lecture")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateLecture;
    
    @Column(name = "statut", nullable = false)
    private String statut = "CREE";
    
    @Column(name = "priorite", nullable = false)
    private String priorite = "NORMALE";
    
    @ElementCollection
    @CollectionTable(name = "notification_metadonnees",
                    joinColumns = @JoinColumn(name = "notification_id"))
    @MapKeyColumn(name = "meta_key")
    @Column(name = "meta_value", columnDefinition = "TEXT")
    private Map<String, Object> metadonnees;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_message_id")
    private FileMessage fileMessage;
    
    @PrePersist
    public void onCreate() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDateTime.now();
        }
    }

    public Notification(Long id, String titre, String message, String type, String canal, String destinataireId, String typeDestinataire, LocalDateTime dateCreation, LocalDateTime dateEnvoi, LocalDateTime dateLecture, String statut, String priorite, Map<String, Object> metadonnees, FileMessage fileMessage) {
        this.id = id;
        this.titre = titre;
        this.message = message;
        this.type = type;
        this.canal = canal;
        this.destinataireId = destinataireId;
        this.typeDestinataire = typeDestinataire;
        this.dateCreation = dateCreation;
        this.dateEnvoi = dateEnvoi;
        this.dateLecture = dateLecture;
        this.statut = statut;
        this.priorite = priorite;
        this.metadonnees = metadonnees;
        this.fileMessage = fileMessage;
    }

    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getDestinataireId() {
        return destinataireId;
    }

    public void setDestinataireId(String destinataireId) {
        this.destinataireId = destinataireId;
    }

    public String getTypeDestinataire() {
        return typeDestinataire;
    }

    public void setTypeDestinataire(String typeDestinataire) {
        this.typeDestinataire = typeDestinataire;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public LocalDateTime getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(LocalDateTime dateLecture) {
        this.dateLecture = dateLecture;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public Map<String, Object> getMetadonnees() {
        return metadonnees;
    }

    public void setMetadonnees(Map<String, Object> metadonnees) {
        this.metadonnees = metadonnees;
    }

    public FileMessage getFileMessage() {
        return fileMessage;
    }

    public void setFileMessage(FileMessage fileMessage) {
        this.fileMessage = fileMessage;
    }

    public Notification(Notificationdto notification) {
        setId(notification.getId());
        setTitre(notification.getTitre());
        setMessage(notification.getMessage());
        setType(notification.getType());
        setCanal(notification.getCanal());
        setDestinataireId(notification.getDestinataireId());
        setTypeDestinataire(notification.getTypeDestinataire());
        setDateCreation(notification.getDateCreation());
        setDateEnvoi(notification.getDateEnvoi());
        setDateLecture(notification.getDateLecture());
        setStatut(notification.getStatut());
        setPriorite(notification.getPriorite());
        setMetadonnees(notification.getMetadonnees());
        setFileMessage(notification.getFileMessage());
    }
}

