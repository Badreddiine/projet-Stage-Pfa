package com.notification.entity;

import com.notification.dto.SessionMQTTdto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.lang.Boolean;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "session_mqtt")

public class SessionMQTT {
    
    @Id
    @Column(name = "id_session", nullable = false)
    private String idSession;
    
    @Column(name = "utilisateur_id", nullable = false)
    private String utilisateurId;
    
    @Column(name = "role_utilisateur")
    private String roleUtilisateur;
    
    @Column(name = "date_connexion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateConnexion;
    
    @Column(name = "date_deconnexion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateDeconnexion;
    
    @Column(name = "statut_connexion", nullable = false)
    private String statutConnexion = "ACTIVE";
    
    @ElementCollection
    @CollectionTable(name = "session_topics_abonnes",
                    joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "topic")
    private Set<String> topicsAbonnes;
    
    @Column(name = "adresse_ip")
    private String adresseIP;
    
    @Column(name = "client_mqtt")
    private String clientMQTT;
    
    @Column(name = "qualite_service")
    private Integer qualiteService = 1;
    
    @PrePersist
    public void onCreate() {
        if (this.dateConnexion == null) {
            this.dateConnexion = LocalDateTime.now();
        }
    }


    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public String getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getRoleUtilisateur() {
        return roleUtilisateur;
    }

    public void setRoleUtilisateur(String roleUtilisateur) {
        this.roleUtilisateur = roleUtilisateur;
    }

    public LocalDateTime getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(LocalDateTime dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public LocalDateTime getDateDeconnexion() {
        return dateDeconnexion;
    }

    public void setDateDeconnexion(LocalDateTime dateDeconnexion) {
        this.dateDeconnexion = dateDeconnexion;
    }

    public String getStatutConnexion() {
        return statutConnexion;
    }

    public void setStatutConnexion(String statutConnexion) {
        this.statutConnexion = statutConnexion;
    }

    public Set<String> getTopicsAbonnes() {
        return topicsAbonnes;
    }

    public void setTopicsAbonnes(Set<String> topicsAbonnes) {
        this.topicsAbonnes = topicsAbonnes;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void setAdresseIP(String adresseIP) {
        this.adresseIP = adresseIP;
    }

    public String getClientMQTT() {
        return clientMQTT;
    }

    public void setClientMQTT(String clientMQTT) {
        this.clientMQTT = clientMQTT;
    }

    public Integer getQualiteService() {
        return qualiteService;
    }

    public void setQualiteService(Integer qualiteService) {
        this.qualiteService = qualiteService;
    }

    public SessionMQTT(String idSession, String utilisateurId, String roleUtilisateur, LocalDateTime dateConnexion, LocalDateTime dateDeconnexion, String statutConnexion, Set<String> topicsAbonnes, String adresseIP, String clientMQTT, Integer qualiteService) {
        this.idSession = idSession;
        this.utilisateurId = utilisateurId;
        this.roleUtilisateur = roleUtilisateur;
        this.dateConnexion = dateConnexion;
        this.dateDeconnexion = dateDeconnexion;
        this.statutConnexion = statutConnexion;
        this.topicsAbonnes = topicsAbonnes;
        this.adresseIP = adresseIP;
        this.clientMQTT = clientMQTT;
        this.qualiteService = qualiteService;
    }

    public SessionMQTT() {
    }

    public SessionMQTT (SessionMQTTdto session) {
        setIdSession(session.getIdSession());
        setUtilisateurId(session.getUtilisateurId());
        setRoleUtilisateur(session.getRoleUtilisateur());
        setDateConnexion(session.getDateConnexion());
        setStatutConnexion(session.getStatutConnexion());
        setTopicsAbonnes(session.getTopicsAbonnes());
        setAdresseIP(session.getAdresseIP());
        setClientMQTT(session.getClientMQTT());
        setQualiteService(session.getQualiteService());
    }
}

