package org.runimo.runimo.configs;

import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    private static final String TEST_SECRET = "testSecret";

    @Primary
    public JwtTokenFactory jwtTokenFactory() {
        return new JwtTokenFactory(TEST_SECRET, 100000L, 360000L, 60000L);
    }

    @Bean
    @Primary
    public JwtResolver jwtResolver() {
        return new JwtResolver(TEST_SECRET);
    }
}
