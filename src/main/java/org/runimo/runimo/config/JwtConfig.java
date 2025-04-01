package org.runimo.runimo.config;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;
  @Value("${jwt.expiration}")
  private long jwtExpiration;
  @Value("${jwt.refresh.expiration}")
  private long jwtRefreshExpiration;

  @Bean
  public JwtTokenFactory jwtTokenFactory() {
    return new JwtTokenFactory(jwtSecret, jwtExpiration, jwtRefreshExpiration);
  }

  @Bean
  public JwtResolver jwtResolver() {
    return new JwtResolver(jwtSecret);
  }

}
