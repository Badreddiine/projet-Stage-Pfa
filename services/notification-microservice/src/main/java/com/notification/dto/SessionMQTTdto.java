package com.notification.dto;


import com.notification.entity.SessionMQTT;



import java.time.LocalDateTime;
import java.util.Set;


public class SessionMQTTdto {

    private String idSession;

    private String utilisateurId;

    private String roleUtilisateur;

    private LocalDateTime dateConnexion;

    private LocalDateTime dateDeconnexion;

    private String statutConnexion = "ACTIVE";

    private Set<String> topicsAbonnes;

    private String adresseIP;

    private String clientMQTT;

    private Integer qualiteService = 1;

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

    public SessionMQTTdto(String idSession, String utilisateurId, String roleUtilisateur, LocalDateTime dateConnexion, LocalDateTime dateDeconnexion, String statutConnexion, Set<String> topicsAbonnes, String adresseIP, String clientMQTT, Integer qualiteService) {
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

    public SessionMQTTdto() {
    }

    public SessionMQTTdto (SessionMQTT session) {
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
