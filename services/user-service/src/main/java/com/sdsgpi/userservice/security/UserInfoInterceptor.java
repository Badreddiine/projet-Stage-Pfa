package com.sdsgpi.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdsgpi.userservice.dto.UserInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;
import java.util.List;

/**
 * Intercepteur pour extraire les informations utilisateur du token JWT ou du header X-User-Info
 * et les stocker dans le UserContextHolder
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // Essayer d'abord de récupérer depuis le header X-User-Info (venant de la Gateway)
            String userInfoHeader = request.getHeader("X-User-Info");
            if (userInfoHeader != null && !userInfoHeader.isEmpty()) {
                extractUserInfoFromHeader(userInfoHeader);
                return true;
            }

            // Sinon, extraire depuis le token JWT directement
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
                extractUserInfoFromJwt(jwtAuthToken.getToken());
            }

        } catch (Exception e) {
            log.warn("Erreur lors de l'extraction des informations utilisateur: {}", e.getMessage());
            // Ne pas bloquer la requête, continuer sans informations utilisateur
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Nettoyer le ThreadLocal pour éviter les fuites de mémoire
        UserContextHolder.clear();
    }

    private void extractUserInfoFromHeader(String userInfoHeader) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(userInfoHeader);
            String userInfoJson = new String(decodedBytes);
            UserInfoDto userInfo = objectMapper.readValue(userInfoJson, UserInfoDto.class);
            UserContextHolder.setUserInfo(userInfo);
            log.debug("Informations utilisateur extraites du header: {}", userInfo.getUsername());
        } catch (Exception e) {
            log.warn("Erreur lors du décodage du header X-User-Info: {}", e.getMessage());
        }
    }

    private void extractUserInfoFromJwt(Jwt jwt) {
        try {
            String userId = jwt.getSubject();
            String username = jwt.getClaimAsString("preferred_username");
            String email = jwt.getClaimAsString("email");
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");

            // Extraire les rôles du realm
            List<String> roles = jwt.getClaimAsStringList("realm_access.roles");

            // Construire le nom complet
            String fullName = buildFullName(firstName, lastName, username);

            UserInfoDto userInfo = UserInfoDto.builder()
                    .userId(userId)
                    .username(username)
                    .email(email)
                    .fullName(fullName)
                    .isActive(true)
                    .preferredLanguage("fr") // Valeur par défaut
                    .roles(roles)
                    .build();

            UserContextHolder.setUserInfo(userInfo);
            log.debug("Informations utilisateur extraites du JWT: {}", userInfo.getUsername());
        } catch (Exception e) {
            log.warn("Erreur lors de l'extraction des informations du JWT: {}", e.getMessage());
        }
    }

    private String buildFullName(String firstName, String lastName, String username) {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return username;
    }
}

