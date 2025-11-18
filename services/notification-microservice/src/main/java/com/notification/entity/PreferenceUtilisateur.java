package com.notification.entity;

import com.notification.dto.PreferenceUtilisateurdto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "preference_utilisateur")
public class PreferenceUtilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;
    
    @Column(name = "canal_id", nullable = false)
    private Long canalId;
    
    @Column(name = "est_active", nullable = false)
    private Boolean estActive = true;
    
    @Column(name = "type_notification", nullable = false)
    private String typeNotification;
    
    @Column(name = "frequence")
    private String frequence;
    
    @ElementCollection
    @CollectionTable(name = "preference_parametres", 
                    joinColumns = @JoinColumn(name = "preference_id"))
    @MapKeyColumn(name = "parametre_key")
    @Column(name = "parametre_value", columnDefinition = "TEXT")
    private Map<String, Object> parametres;
    
    @Column(name = "date_mise_a_jour")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateMiseAJour;
    
    @ElementCollection
    @CollectionTable(name = "preference_topics_mqtt",
                    joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "topic")
    private Set<String> topicsMQTT;
    
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {

        this.dateMiseAJour = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getCanalId() {
        return canalId;
    }

    public void setCanalId(Long canalId) {
        this.canalId = canalId;
    }

    public Boolean getEstActive() {
        return estActive;
    }

    public void setEstActive(Boolean estActive) {
        this.estActive = estActive;
    }

    public String getTypeNotification() {
        return typeNotification;
    }

    public void setTypeNotification(String typeNotification) {
        this.typeNotification = typeNotification;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public Map<String, Object> getParametres() {
        return parametres;
    }

    public void setParametres(Map<String, Object> parametres) {
        this.parametres = parametres;
    }

    public LocalDateTime getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(LocalDateTime dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public Set<String> getTopicsMQTT() {
        return topicsMQTT;
    }

    public void setTopicsMQTT(Set<String> topicsMQTT) {
        this.topicsMQTT = topicsMQTT;
    }

    public PreferenceUtilisateur(Long id, Long utilisateurId, Long canalId, Boolean estActive, String typeNotification, String frequence, Map<String, Object> parametres, LocalDateTime dateMiseAJour, Set<String> topicsMQTT) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.canalId = canalId;
        this.estActive = estActive;
        this.typeNotification = typeNotification;
        this.frequence = frequence;
        this.parametres = parametres;
        this.dateMiseAJour = dateMiseAJour;
        this.topicsMQTT = topicsMQTT;
    }

    public PreferenceUtilisateur() {
    }

    public PreferenceUtilisateur(PreferenceUtilisateurdto preferenceUtilisateur) {
        setId(preferenceUtilisateur.getId());
        setUtilisateurId(preferenceUtilisateur.getUtilisateurId());
        setCanalId(preferenceUtilisateur.getCanalId());
        setEstActive(preferenceUtilisateur.getEstActive());
        setTypeNotification(preferenceUtilisateur.getTypeNotification());
        setFrequence(preferenceUtilisateur.getFrequence());
        setParametres(preferenceUtilisateur.getParametres());
        setTopicsMQTT(preferenceUtilisateur.getTopicsMQTT());
        setParametres(preferenceUtilisateur.getParametres());
        setDateMiseAJour(preferenceUtilisateur.getDateMiseAJour());
    }
}

