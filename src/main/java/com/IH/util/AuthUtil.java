package com.IH.util;

import com.IH.model.dto.UserRole;
import com.IH.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthUtil {

    @Autowired
    private SecurityService securityService;

    public boolean isAdmin(Long userId) {
        if (userId == null) return false;
        return securityService.getUserRole(userId)
                .map(role -> role == UserRole.ADMIN)
                .orElse(false);
    }

    public boolean isModerator(Long userId) {
        if (userId == null) return false;
        return securityService.getUserRole(userId)
                .map(role -> role == UserRole.MODERATOR || role == UserRole.ADMIN)
                .orElse(false);
    }

    public boolean isOwner(Long userId, Long ownerId) {
        if (userId == null) return false;
        return userId.equals(ownerId) || isModerator(userId);
    }
}
