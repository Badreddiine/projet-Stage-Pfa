package com.notification.dto;

import com.notification.entity.CanalNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

public class CanalNotificationdto {
    private Long id;

    private String typeCanal;

    private String nomCanal;

    private Boolean estActif = true;

    private String configuration;

    private Integer priorite = 1;

    private String description;

    private Map<String, Object> parametres;

    private Boolean supportMQTT = false;

    private String topicMQTT;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCanal() {
        return typeCanal;
    }

    public void setTypeCanal(String typeCanal) {
        this.typeCanal = typeCanal;
    }

    public String getNomCanal() {
        return nomCanal;
    }

    public void setNomCanal(String nomCanal) {
        this.nomCanal = nomCanal;
    }

    public Boolean getEstActif() {
        return estActif;
    }

    public void setEstActif(Boolean estActif) {
        this.estActif = estActif;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Integer getPriorite() {
        return priorite;
    }

    public void setPriorite(Integer priorite) {
        this.priorite = priorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getParametres() {
        return parametres;
    }

    public void setParametres(Map<String, Object> parametres) {
        this.parametres = parametres;
    }

    public Boolean getSupportMQTT() {
        return supportMQTT;
    }

    public void setSupportMQTT(Boolean supportMQTT) {
        this.supportMQTT = supportMQTT;
    }

    public String getTopicMQTT() {
        return topicMQTT;
    }

    public void setTopicMQTT(String topicMQTT) {
        this.topicMQTT = topicMQTT;
    }

    public CanalNotificationdto(Long id, String typeCanal, String nomCanal, Boolean estActif, String configuration, Integer priorite, String description, Map<String, Object> parametres, Boolean supportMQTT, String topicMQTT) {
        this.id = id;
        this.typeCanal = typeCanal;
        this.nomCanal = nomCanal;
        this.estActif = estActif;
        this.configuration = configuration;
        this.priorite = priorite;
        this.description = description;
        this.parametres = parametres;
        this.supportMQTT = supportMQTT;
        this.topicMQTT = topicMQTT;
    }

    public CanalNotificationdto() {
    }

    public CanalNotificationdto(CanalNotification notification) {
        setId(notification.getId());
        setTypeCanal(notification.getTypeCanal());
        setNomCanal(notification.getNomCanal());
        setEstActif(notification.getEstActif());
        setConfiguration(notification.getConfiguration());
        setPriorite(notification.getPriorite());
        setDescription(notification.getDescription());
        setParametres(notification.getParametres());
        setSupportMQTT(notification.getSupportMQTT());
        setTopicMQTT(notification.getTopicMQTT());
    }

}
