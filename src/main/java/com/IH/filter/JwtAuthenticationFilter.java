package com.IH.filter;

import com.IH.service.JwtService;
import com.IH.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        log.debug("=== JWT FILTER START ===");
        log.debug("URI: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        log.debug("Auth header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (blacklistService.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is invalidated");
                return;
            }
            if (jwtService.validateToken(token)) {
                String login = jwtService.extractLogin(token);
                Long userId = jwtService.extractUserId(token);
                log.debug("Login: " + login + ", userId: " + userId);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(login
                        , null
                        , null);
                SecurityContextHolder.getContext().setAuthentication(auth);
                request.setAttribute("userId", userId);
            }
        }else {
            log.debug("No Bearer token found in header");
        }
        log.debug("=== JWT FILTER END ===");
        chain.doFilter(request, response);
    }
}

