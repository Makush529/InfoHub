package com.IH.config;

import com.IH.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/posts").permitAll()           // ← просмотр постов всем
                        .requestMatchers("/posts/{id}").permitAll()     // ← просмотр одного поста всем
                        .requestMatchers("/posts/tag/{tagName}").permitAll()
                        .requestMatchers("/comments/post/{postId}").permitAll()  // ← просмотр комментариев всем
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // ← Swagger

                        // 🔒 ЗАЩИЩЕННЫЕ ЭНДПОИНТЫ (требуют авторизации)
                        .requestMatchers("/posts/create").authenticated()      // ← создание поста
                        .requestMatchers("/posts/{id}/like").authenticated()   // ← лайки
                        .requestMatchers("/posts/{id}/dislike").authenticated()
                        .requestMatchers("/comments").authenticated()          // ← создание комментария
                        .requestMatchers("/auth/me").authenticated()           // ← профиль
                        .requestMatchers("/users/{id}").permitAll()            // ← публичный профиль (можно всем)

                        // Все остальные требуют авторизации
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
