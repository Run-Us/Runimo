package org.runimo.runimo.configs;

import org.runimo.runimo.auth.filters.JwtAuthenticationFilter;
import org.runimo.runimo.config.CustomAuthenticationFailureHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@TestConfiguration
@EnableWebSecurity
@Import(CustomAuthenticationFailureHandler.class)
public class TestSecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationFailureHandler customAuthenticationFailureHandler;

  public TestSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                            AuthenticationFailureHandler customAuthenticationFailureHandler) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .oauth2Login(oAuth2Login -> {
          oAuth2Login
              .loginPage("/api/v1/users/auth/login")
              .failureHandler(customAuthenticationFailureHandler);
        })
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/v1/users/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}

