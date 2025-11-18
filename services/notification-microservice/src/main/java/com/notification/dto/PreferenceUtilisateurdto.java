package com.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.notification.entity.PreferenceUtilisateur;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class PreferenceUtilisateurdto {
    private Long id;

    private Long utilisateurId;

    private Long canalId;

    private Boolean estActive = true;

    private String typeNotification;

    private String frequence;

    private Map<String, Object> parametres;

    private LocalDateTime dateMiseAJour;

    private Set<String> topicsMQTT;

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

    public PreferenceUtilisateurdto(Long id, Long utilisateurId, Long canalId, Boolean estActive, String typeNotification, String frequence, Map<String, Object> parametres, LocalDateTime dateMiseAJour, Set<String> topicsMQTT) {
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

    public PreferenceUtilisateurdto() {
    }

    public PreferenceUtilisateurdto(PreferenceUtilisateur preferenceUtilisateur) {
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
