package com.notification.controller;


import com.notification.dto.Notificationdto;
import com.notification.entity.Notification;
import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")


@CrossOrigin(origins = "*")
public class NotificationController {



    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @PostMapping
    public ResponseEntity<Notificationdto> creerNotification(@Valid @RequestBody Notificationdto notification) {
        try {
            Notificationdto nouvelleNotification = notificationService.creerNotification(notification);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleNotification);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<Notificationdto>> obtenirNotificationsUtilisateur(
            @PathVariable String utilisateurId) {
        try {
            List<Notificationdto> notifications = notificationService.obtenirNotificationsUtilisateur(utilisateurId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}/non-lues")
    public ResponseEntity<List<Notificationdto>> obtenirNotificationsNonLues(
            @PathVariable String utilisateurId) {
        try {
            List<Notificationdto> notifications = notificationService.obtenirNotificationsNonLues(utilisateurId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}/count-non-lues")
    public ResponseEntity<Long> compterNotificationsNonLues(@PathVariable String utilisateurId) {
        try {
            Long count = notificationService.compterNotificationsNonLues(utilisateurId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{notificationId}/marquer-lue")
    public ResponseEntity<Void> marquerCommeLue(@PathVariable Long notificationId) {
        try {
            notificationService.marquerCommeLue(notificationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/a-envoyer")
    public ResponseEntity<List<Notificationdto>> obtenirNotificationsAEnvoyer() {
        try {
            List<Notificationdto> notifications = notificationService.obtenirNotificationsAEnvoyer();
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> supprimerNotification(@PathVariable Long notificationId) {
        try {
            notificationService.supprimerNotification(notificationId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{notificationId}/renvoyer")
    public ResponseEntity<Void> renvoyerNotification(@PathVariable Long notificationId) {
        try {
            // Logique pour renvoyer une notification
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


