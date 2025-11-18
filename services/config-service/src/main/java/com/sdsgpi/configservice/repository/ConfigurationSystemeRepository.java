package com.sdsgpi.configservice.repository;

import com.sdsgpi.configservice.entity.ConfigurationSysteme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationSystemeRepository extends JpaRepository<ConfigurationSysteme, Long> {
}


