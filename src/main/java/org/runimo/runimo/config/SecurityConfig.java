package org.runimo.runimo.config;

import static org.runimo.runimo.config.SecurityConstants.ADMIN_ENDPOINT_PATTERN;
import static org.runimo.runimo.config.SecurityConstants.ADMIN_ROLE;
import static org.runimo.runimo.config.SecurityConstants.COMMON_PUBLIC_ENDPOINTS;
import static org.runimo.runimo.config.SecurityConstants.DEV_PUBLIC_ENDPOINTS;
import static org.runimo.runimo.config.SecurityConstants.USER_ENDPOINT_PATTERN;
import static org.runimo.runimo.config.SecurityConstants.USER_ROLE;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.filters.JwtAuthenticationFilter;
import org.runimo.runimo.exceptions.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    @Profile({"prod", "test"})
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return buildSecurityFilterChain(http)
            .authorizeHttpRequests(this::configureProdAuthorization)
            .build();
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        return buildSecurityFilterChain(http)
            .authorizeHttpRequests(this::configureDevAuthorization)
            .build();
    }

    private HttpSecurity buildSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(customAccessDeniedHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private void configureProdAuthorization(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize
            .requestMatchers(COMMON_PUBLIC_ENDPOINTS).permitAll()
            .requestMatchers(ADMIN_ENDPOINT_PATTERN).hasRole(ADMIN_ROLE)
            .requestMatchers(USER_ENDPOINT_PATTERN).hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .anyRequest().authenticated();
    }

    private void configureDevAuthorization(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize
            .requestMatchers(COMMON_PUBLIC_ENDPOINTS).permitAll()
            .requestMatchers(DEV_PUBLIC_ENDPOINTS).permitAll()
            .requestMatchers(ADMIN_ENDPOINT_PATTERN).hasRole(ADMIN_ROLE)
            .requestMatchers(USER_ENDPOINT_PATTERN).hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .anyRequest().authenticated();
    }
}
