package com.IH.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TokenBlacklistService {

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public void addToBlacklist(String token) {
        blacklist.add(token);
        log.debug("Token added to blacklist: " + token.substring(0, Math.min(token.length(), 20)) + "...");
    }

    public boolean isBlacklisted(String token) {
        boolean isBlacklisted = blacklist.contains(token);
        if (isBlacklisted) {
            log.warn("Token is blacklisted!");
        }
        return isBlacklisted;
    }
}
//TODO доделать время хранения токена
