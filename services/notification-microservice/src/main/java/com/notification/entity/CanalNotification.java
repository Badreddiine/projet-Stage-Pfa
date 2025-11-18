package com.notification.entity;

import com.notification.dto.CanalNotificationdto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name = "canal_notification")

public class CanalNotification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "type_canal", nullable = false)
    private String typeCanal;
    
    @Column(name = "nom_canal", nullable = false)
    private String nomCanal;
    
    @Column(name = "est_actif", nullable = false)
    private Boolean estActif = true;
    
    @Column(name = "configuration", columnDefinition = "TEXT")
    private String configuration;
    
    @Column(name = "priorite")
    private Integer priorite = 1;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "canal_parametres",
                    joinColumns = @JoinColumn(name = "canal_id"))
    @MapKeyColumn(name = "parametre_key")
    @Column(name = "parametre_value", columnDefinition = "TEXT")
    private Map<String, Object> parametres;
    
    @Column(name = "support_mqtt", nullable = false)
    private Boolean supportMQTT = false;
    
    @Column(name = "topic_mqtt")
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

    public CanalNotification(Long id, String typeCanal, String nomCanal, Boolean estActif, String configuration, Integer priorite, String description, Map<String, Object> parametres, Boolean supportMQTT, String topicMQTT) {
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

    public CanalNotification() {
    }

    public CanalNotification(CanalNotificationdto notificationdto) {
        setId(notificationdto.getId());
        setTypeCanal(notificationdto.getTypeCanal());
        setNomCanal(notificationdto.getNomCanal());
        setEstActif(notificationdto.getEstActif());
        setConfiguration(notificationdto.getConfiguration());
        setPriorite(notificationdto.getPriorite());
        setDescription(notificationdto.getDescription());
        setParametres(notificationdto.getParametres());
        setSupportMQTT(notificationdto.getSupportMQTT());
        setTopicMQTT(notificationdto.getTopicMQTT());
    }
}

