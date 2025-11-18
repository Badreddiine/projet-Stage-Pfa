package com.sdsgpi.configservice.controller;

import com.sdsgpi.configservice.entity.ServiceRegistry;
import com.sdsgpi.configservice.service.ServiceRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-registries")
public class ServiceRegistryController {

    @Autowired
    private ServiceRegistryService serviceRegistryService;

    @GetMapping
    public List<ServiceRegistry> getAllServiceRegistries() {
        return serviceRegistryService.findAllServiceRegistries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRegistry> getServiceRegistryById(@PathVariable Long id) {
        return serviceRegistryService.findServiceRegistryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ServiceRegistry createServiceRegistry(@RequestBody ServiceRegistry serviceRegistry) {
        return serviceRegistryService.saveServiceRegistry(serviceRegistry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRegistry> updateServiceRegistry(@PathVariable Long id, @RequestBody ServiceRegistry serviceRegistry) {
        return serviceRegistryService.findServiceRegistryById(id)
                .map(existingServiceRegistry -> {
                    serviceRegistry.setId(id);
                    return ResponseEntity.ok(serviceRegistryService.saveServiceRegistry(serviceRegistry));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceRegistry(@PathVariable Long id) {
        serviceRegistryService.deleteServiceRegistry(id);
        return ResponseEntity.noContent().build();
    }
}


