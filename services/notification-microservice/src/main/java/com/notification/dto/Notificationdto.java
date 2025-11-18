package com.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.notification.entity.FileMessage;
import com.notification.entity.Notification;


import java.time.LocalDateTime;
import java.util.Map;

public class Notificationdto {
    private Long id;

    private String titre;

    private String message;

    private String type;

    private String canal;

    private String destinataireId;

    private String typeDestinataire;

    private LocalDateTime dateCreation;

    private LocalDateTime dateEnvoi;

    private LocalDateTime dateLecture;
// c'est string il faut que sont lister dans la front end
    private String statut = "CREE";

    private String priorite = "NORMALE";

    private Map<String, Object> metadonnees;

    private FileMessage fileMessage;

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

    public Notificationdto(Long id, String titre, String message, String type, String canal, String destinataireId, String typeDestinataire, LocalDateTime dateCreation, LocalDateTime dateEnvoi, LocalDateTime dateLecture, String statut, String priorite, Map<String, Object> metadonnees, FileMessage fileMessage) {
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

    public Notificationdto() {
    }

    public Notificationdto(Notification notification) {
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
