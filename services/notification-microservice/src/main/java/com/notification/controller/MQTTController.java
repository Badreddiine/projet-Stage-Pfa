package com.notification.controller;


import com.notification.entity.SessionMQTT;
import com.notification.service.MQTTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/mqtt")


@CrossOrigin(origins = "*")
public class MQTTController {



    private final MQTTService mqttService;
    public MQTTController(MQTTService mqttService) {
        this.mqttService = mqttService;
    }
    @PostMapping("/sessions")
    public ResponseEntity<SessionMQTT> creerSession(
            @RequestParam String utilisateurId,
            @RequestParam(required = false) String roleUtilisateur,
            HttpServletRequest request) {
        try {
            String adresseIP = obtenirAdresseIPClient(request);
            SessionMQTT session = mqttService.creerSession(utilisateurId, roleUtilisateur, adresseIP);
            return ResponseEntity.status(HttpStatus.CREATED).body(session);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> fermerSession(@PathVariable String sessionId) {
        try {
            mqttService.fermerSession(sessionId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sessions/actives")
    public ResponseEntity<List<SessionMQTT>> obtenirSessionsActives() {
        try {
            List<SessionMQTT> sessions = mqttService.obtenirSessionsActives();
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sessions/topic/{topic}")
    public ResponseEntity<List<SessionMQTT>> obtenirSessionsParTopic(@PathVariable String topic) {
        try {
            List<SessionMQTT> sessions = mqttService.obtenirSessionsParTopic(topic);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/topics/{topic}/subscribe")
    public ResponseEntity<Void> souscrireAuTopic(@PathVariable String topic) {
        try {
            mqttService.souscrireAuTopic(topic);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/topics/{topic}/unsubscribe")
    public ResponseEntity<Void> desouscrireDuTopic(@PathVariable String topic) {
        try {
            mqttService.desouscrireDuTopic(topic);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String obtenirAdresseIPClient(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}


