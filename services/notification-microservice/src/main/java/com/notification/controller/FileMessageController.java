package com.notification.controller;

import com.notification.dto.FileMessagedto;
import com.notification.service.FileMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/file-messages")


@CrossOrigin(origins = "*")
public class FileMessageController {



    private final FileMessageService fileMessageService;
    public FileMessageController(FileMessageService fileMessageService) {
        this.fileMessageService = fileMessageService;
    }
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<FileMessagedto>> obtenirMessagesParStatut(@PathVariable String statut) {
        try {
            List<FileMessagedto> messages = fileMessageService.obtenirMessagesParStatut(statut);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<FileMessagedto> obtenirMessageParId(@PathVariable Long messageId) {
        try {
            Optional<FileMessagedto> message = fileMessageService.obtenirMessageParId(messageId);
            return message.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{messageId}/relancer")
    public ResponseEntity<Void> relancerMessage(@PathVariable Long messageId) {
        try {
            fileMessageService.relancerMessage(messageId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/en-attente")
    public ResponseEntity<List<FileMessagedto>> obtenirMessagesEnAttente() {
        try {
            List<FileMessagedto> messages = fileMessageService.obtenirMessagesParStatut("EN_ATTENTE");
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/traites")
    public ResponseEntity<List<FileMessagedto>> obtenirMessagesTraites() {
        try {
            List<FileMessagedto> messages = fileMessageService.obtenirMessagesParStatut("TRAITE");
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/echec")
    public ResponseEntity<List<FileMessagedto>> obtenirMessagesEnEchec() {
        try {
            List<FileMessagedto> messages = fileMessageService.obtenirMessagesParStatut("ECHEC");
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/traiter-en-attente")
    public ResponseEntity<Void> traiterMessagesEnAttente() {
        try {
            fileMessageService.traiterMessagesEnAttente();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/nettoyer-anciens")
    public ResponseEntity<Void> nettoyerAnciennesMessages() {
        try {
            fileMessageService.nettoyerAnciennesMessages();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

