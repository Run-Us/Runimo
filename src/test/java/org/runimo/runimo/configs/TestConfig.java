package org.runimo.runimo.configs;

import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public JwtTokenFactory jwtTokenFactory() {
        return new JwtTokenFactory("testSecret", 100000L, 360000L, 60000L);
    }

    @Bean
    public JwtResolver jwtResolver() {
        return new JwtResolver("testSecret");
    }
}
