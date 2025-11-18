package com.incident.service.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.incident.service.dto.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

@Component
public class UserInfoInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public UserInfoInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
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

            }
        } catch (Exception e) {
            // Ne pas bloquer la requête
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Nettoyer le ThreadLocal pour éviter les fuites de mémoire
        UserContextHolder.clear();
    }
}
