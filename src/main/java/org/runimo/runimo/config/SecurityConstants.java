package org.runimo.runimo.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SecurityConstants {


    static final String[] COMMON_PUBLIC_ENDPOINTS = {
        "/api/v1/auth/**",
        "/checker/**",
        "/actuator/**",
        "/error"
    };

    static final String[] DEV_PUBLIC_ENDPOINTS = {
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**"
    };

    static final String ADMIN_ENDPOINT_PATTERN = "/api/v1/admin/**";

    static final String USER_ENDPOINT_PATTERN = "/api/v1/users/**";

    static final String USER_ROLE = "USER";
    static final String ADMIN_ROLE = "ADMIN";
}
