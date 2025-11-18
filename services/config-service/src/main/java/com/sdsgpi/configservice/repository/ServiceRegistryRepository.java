package com.sdsgpi.configservice.repository;

import com.sdsgpi.configservice.entity.ServiceRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRegistryRepository extends JpaRepository<ServiceRegistry, Long> {
}


