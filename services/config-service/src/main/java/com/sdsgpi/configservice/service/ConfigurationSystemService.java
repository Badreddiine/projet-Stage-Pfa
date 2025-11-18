package com.sdsgpi.configservice.service;

import com.sdsgpi.configservice.entity.ConfigurationSysteme;
import com.sdsgpi.configservice.repository.ConfigurationSystemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationSystemService {

    @Autowired
    private ConfigurationSystemeRepository configurationSystemeRepository;

    public List<ConfigurationSysteme> findAllConfigurations() {
        return configurationSystemeRepository.findAll();
    }

    public Optional<ConfigurationSysteme> findConfigurationById(Long id) {
        return configurationSystemeRepository.findById(id);
    }

    public ConfigurationSysteme saveConfiguration(ConfigurationSysteme config) {
        if (config.getId() == null) {
            config.setDateCreation(LocalDateTime.now());
        }
        config.setDateModification(LocalDateTime.now());
        return configurationSystemeRepository.save(config);
    }

    public void deleteConfiguration(Long id) {
        configurationSystemeRepository.deleteById(id);
    }
}


