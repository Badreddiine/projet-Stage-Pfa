package com.sdsgpi.configservice.controller;

import com.sdsgpi.configservice.entity.ConfigurationSysteme;
import com.sdsgpi.configservice.service.ConfigurationSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationSystemeController {

    @Autowired
    private ConfigurationSystemService configurationSystemService;

    @GetMapping
    public List<ConfigurationSysteme> getAllConfigurations() {
        return configurationSystemService.findAllConfigurations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfigurationSysteme> getConfigurationById(@PathVariable Long id) {
        return configurationSystemService.findConfigurationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ConfigurationSysteme createConfiguration(@RequestBody ConfigurationSysteme config) {
        return configurationSystemService.saveConfiguration(config);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfigurationSysteme> updateConfiguration(@PathVariable Long id, @RequestBody ConfigurationSysteme config) {
        return configurationSystemService.findConfigurationById(id)
                .map(existingConfig -> {
                    config.setId(id);
                    return ResponseEntity.ok(configurationSystemService.saveConfiguration(config));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable Long id) {
        configurationSystemService.deleteConfiguration(id);
        return ResponseEntity.noContent().build();
    }
}


