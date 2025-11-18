package com.sdsgpi.configservice.service;

import com.sdsgpi.configservice.entity.HistoriqueConfiguration;
import com.sdsgpi.configservice.repository.HistoriqueConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueConfigurationService {

    @Autowired
    private HistoriqueConfigurationRepository historiqueConfigurationRepository;

    public List<HistoriqueConfiguration> findAllHistoriques() {
        return historiqueConfigurationRepository.findAll();
    }

    public Optional<HistoriqueConfiguration> findHistoriqueById(Long id) {
        return historiqueConfigurationRepository.findById(id);
    }

    public HistoriqueConfiguration saveHistorique(HistoriqueConfiguration historique) {
        return historiqueConfigurationRepository.save(historique);
    }

    public void deleteHistorique(Long id) {
        historiqueConfigurationRepository.deleteById(id);
    }
}


