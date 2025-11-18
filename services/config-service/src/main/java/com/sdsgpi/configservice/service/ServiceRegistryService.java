package com.sdsgpi.configservice.service;

import com.sdsgpi.configservice.entity.ServiceRegistry;
import com.sdsgpi.configservice.repository.ServiceRegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceRegistryService {

    @Autowired
    private ServiceRegistryRepository serviceRegistryRepository;

    public List<ServiceRegistry> findAllServiceRegistries() {
        return serviceRegistryRepository.findAll();
    }

    public Optional<ServiceRegistry> findServiceRegistryById(Long id) {
        return serviceRegistryRepository.findById(id);
    }

    public ServiceRegistry saveServiceRegistry(ServiceRegistry serviceRegistry) {
        serviceRegistry.setDerniereVerification(LocalDateTime.now());
        return serviceRegistryRepository.save(serviceRegistry);
    }

    public void deleteServiceRegistry(Long id) {
        serviceRegistryRepository.deleteById(id);
    }
}


