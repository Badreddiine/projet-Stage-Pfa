package com.notification.controller;


import com.notification.dto.SessionMQTTdto;
import com.notification.entity.SessionMQTT;
import com.notification.repository.SessionMQTTRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions-mqtt")


@CrossOrigin(origins = "*")
public class SessionMQTTController {



    private final SessionMQTTRepository sessionRepository;
    public SessionMQTTController(SessionMQTTRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    @GetMapping
    public ResponseEntity<List<SessionMQTT>> obtenirToutesSessions() {
        try {
            List<SessionMQTT> sessions = sessionRepository.findAll();
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/actives")
    public ResponseEntity<List<SessionMQTT>> obtenirSessionsActives() {
        try {
            List<SessionMQTT> sessions = sessionRepository.findByStatutConnexion("ACTIVE");
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<SessionMQTT>> obtenirSessionsUtilisateur(@PathVariable String utilisateurId) {
        try {
            List<SessionMQTT> sessions = sessionRepository.findByUtilisateurId(utilisateurId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}/actives")
    public ResponseEntity<List<SessionMQTTdto>> obtenirSessionsActivesUtilisateur(@PathVariable String utilisateurId) {
        try {
            List<SessionMQTTdto> sessions = sessionRepository.findByUtilisateurIdAndStatutConnexion(utilisateurId, "ACTIVE")
                    .stream()
                    .map(SessionMQTTdto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionMQTT> obtenirSessionParId(@PathVariable String sessionId) {
        try {
            Optional<SessionMQTT> session = sessionRepository.findById(sessionId);
            return session.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/topic/{topic}")
    public ResponseEntity<List<SessionMQTT>> obtenirSessionsParTopic(@PathVariable String topic) {
        try {
            List<SessionMQTT> sessions = sessionRepository.findActiveSessionsByTopic(topic);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<SessionMQTT>> obtenirSessionsParStatut(@PathVariable String statut) {
        try {
            List<SessionMQTT> sessions = sessionRepository.findByStatutConnexion(statut);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{sessionId}/fermer")
    public ResponseEntity<Void> fermerSession(@PathVariable String sessionId) {
        try {
            Optional<SessionMQTT> sessionOpt = sessionRepository.findById(sessionId);
            if (sessionOpt.isPresent()) {
                SessionMQTT session = sessionOpt.get();
                session.setStatutConnexion("FERME");
                session.setDateDeconnexion(LocalDateTime.now());
                sessionRepository.save(session);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> supprimerSession(@PathVariable String sessionId) {
        try {
            if (sessionRepository.existsById(sessionId)) {
                sessionRepository.deleteById(sessionId);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

