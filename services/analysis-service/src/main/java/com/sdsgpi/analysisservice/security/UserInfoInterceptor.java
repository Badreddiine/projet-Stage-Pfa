package com.sdsgpi.analysisservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdsgpi.analysisservice.dto.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

/**
 * Intercepteur pour extraire les informations utilisateur du header X-User-Info
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String userInfoHeader = request.getHeader("X-User-Info");
            
            if (userInfoHeader != null && !userInfoHeader.isEmpty()) {
                // Décoder le header Base64
                byte[] decodedBytes = Base64.getDecoder().decode(userInfoHeader);
                String userInfoJson = new String(decodedBytes);
                
                // Convertir en objet UserInfo
                UserInfo userInfo = objectMapper.readValue(userInfoJson, UserInfo.class);
                
                // Stocker dans le ThreadLocal
                UserContextHolder.setUserInfo(userInfo);
                
                log.debug("Informations utilisateur extraites pour: {}", userInfo.getUsername());
            } else {
                log.debug("Aucune information utilisateur trouvée dans les headers");
            }
        } catch (Exception e) {
            log.warn("Erreur lors de l'extraction des informations utilisateur: {}", e.getMessage());
            // Ne pas bloquer la requête en cas d'erreur
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Nettoyer le ThreadLocal pour éviter les fuites de mémoire
        UserContextHolder.clear();
    }
}

