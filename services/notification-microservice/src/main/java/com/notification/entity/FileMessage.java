package com.notification.entity;

import com.notification.dto.FileMessagedto;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_message")
@Getter
@Setter
public class FileMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom_file", nullable = false)
    private String nomFile;
    
    @Column(name = "type_message", nullable = false)
    private String typeMessage;
    
    @Column(name = "contenu", columnDefinition = "LONGTEXT")
    private String contenu;
    
    @Column(name = "statut", nullable = false)
    private String statut = "EN_ATTENTE";
    
    @Column(name = "date_creation")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreation;
    
    @Column(name = "date_traitement")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTraitement;
    
    @Column(name = "compteur_tentatives")
    private Integer compteurTentatives = 0;
    
    @Column(name = "max_tentatives")
    private Integer maxTentatives = 3;
    
    @Column(name = "message_erreur", columnDefinition = "TEXT")
    private String messageErreur;
    
    @Column(name = "topic_mqtt")
    private String topicMQTT;
    
    @PrePersist
    public void onCreate() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDateTime.now();
        }
    }

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

    public FileMessage(Long id, String nomFile, String typeMessage, String contenu, String statut, LocalDateTime dateCreation, LocalDateTime dateTraitement, Integer compteurTentatives, Integer maxTentatives, String messageErreur, String topicMQTT) {
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

    public FileMessage() {
    }

    public FileMessage(FileMessagedto fileMessagedto ) {
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

