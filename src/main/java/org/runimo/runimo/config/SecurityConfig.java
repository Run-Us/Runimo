package org.runimo.runimo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())  // API 기반 서비스의 경우 CSRF 비활성화 (필요에 따라 조정)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/login**", "/error**", "/auth/**", "/oauth2/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs.yaml", "/v3/api-docs.html").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/login")
        )
        .formLogin(form -> form
            .loginPage("/login")
            .permitAll()
        );
    return http.build();
  }

}
