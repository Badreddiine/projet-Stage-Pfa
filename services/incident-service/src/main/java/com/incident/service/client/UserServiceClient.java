package com.incident.service.client;


import com.incident.service.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

    // Exemple pour récupérer les infos de l'utilisateur courant (via l'ID passé par la Gateway)
    @GetMapping("/me")
    UserInfo getCurrentUserInfo(@RequestHeader("X-User-Id") String userId);

    // Exemple pour récupérer les infos d'un utilisateur par son ID Keycloak
    @GetMapping("/{keycloakId}")
    UserInfo getUserById(@PathVariable String keycloakId);
}
