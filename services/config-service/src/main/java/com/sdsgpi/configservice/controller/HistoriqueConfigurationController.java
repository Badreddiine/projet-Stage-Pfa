package com.sdsgpi.configservice.controller;

import com.sdsgpi.configservice.entity.HistoriqueConfiguration;
import com.sdsgpi.configservice.service.HistoriqueConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historique-configurations")
public class HistoriqueConfigurationController {

    @Autowired
    private HistoriqueConfigurationService historiqueConfigurationService;

    @GetMapping
    public List<HistoriqueConfiguration> getAllHistoriques() {
        return historiqueConfigurationService.findAllHistoriques();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueConfiguration> getHistoriqueById(@PathVariable Long id) {
        return historiqueConfigurationService.findHistoriqueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoriqueConfiguration createHistorique(@RequestBody HistoriqueConfiguration historique) {
        return historiqueConfigurationService.saveHistorique(historique);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorique(@PathVariable Long id) {
        historiqueConfigurationService.deleteHistorique(id);
        return ResponseEntity.noContent().build();
    }
}


