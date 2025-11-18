package com.sdsgpi.configservice.repository;

import com.sdsgpi.configservice.entity.HistoriqueConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueConfigurationRepository extends JpaRepository<HistoriqueConfiguration, Long> {
}


