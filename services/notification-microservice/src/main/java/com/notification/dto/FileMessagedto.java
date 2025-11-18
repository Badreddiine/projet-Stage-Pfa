package com.notification.dto;

import com.notification.entity.FileMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class FileMessagedto {
    private Long id;

    private String nomFile;

    private String typeMessage;

    private String contenu;

    private String statut = "EN_ATTENTE";

    private LocalDateTime dateCreation;

    private LocalDateTime dateTraitement;

    private Integer compteurTentatives = 0;

    private Integer maxTentatives = 3;

    private String messageErreur;

    private String topicMQTT;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFile() {
        return nomFile;
    }

    public void setNomFile(String nomFile) {
        this.nomFile = nomFile;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(LocalDateTime dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public Integer getCompteurTentatives() {
        return compteurTentatives;
    }

    public void setCompteurTentatives(Integer compteurTentatives) {
        this.compteurTentatives = compteurTentatives;
    }

    public Integer getMaxTentatives() {
        return maxTentatives;
    }

    public void setMaxTentatives(Integer maxTentatives) {
        this.maxTentatives = maxTentatives;
    }

    public String getMessageErreur() {
        return messageErreur;
    }

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    public String getTopicMQTT() {
        return topicMQTT;
    }

    public void setTopicMQTT(String topicMQTT) {
        this.topicMQTT = topicMQTT;
    }

    public FileMessagedto(Long id, String nomFile, String typeMessage, String contenu, String statut, LocalDateTime dateCreation, LocalDateTime dateTraitement, Integer compteurTentatives, Integer maxTentatives, String messageErreur, String topicMQTT) {
        this.id = id;
        this.nomFile = nomFile;
        this.typeMessage = typeMessage;
        this.contenu = contenu;
        this.statut = statut;
        this.dateCreation = dateCreation;
        this.dateTraitement = dateTraitement;
        this.compteurTentatives = compteurTentatives;
        this.maxTentatives = maxTentatives;
        this.messageErreur = messageErreur;
        this.topicMQTT = topicMQTT;
    }

    public FileMessagedto() {
    }

    public FileMessagedto(FileMessage fileMessagedto ) {
        setNomFile(fileMessagedto.getNomFile());
        setTypeMessage(fileMessagedto.getTypeMessage());
        setTypeMessage(fileMessagedto.getTypeMessage());
        setContenu(fileMessagedto.getContenu());
        setDateCreation(fileMessagedto.getDateCreation());
        setDateTraitement(fileMessagedto.getDateTraitement());
        setMessageErreur(fileMessagedto.getMessageErreur());
        setCompteurTentatives(fileMessagedto.getCompteurTentatives());
        setTopicMQTT(fileMessagedto.getTopicMQTT());
    }
}
