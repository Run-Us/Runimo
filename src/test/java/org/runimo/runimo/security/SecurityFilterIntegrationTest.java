package org.runimo.runimo.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Authorization 헤더 없이 요청 시 JWT_NOT_FOUND 에러")
    void requestWithoutAuthHeader() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("UEH4012"));
    }

    @Test
    @DisplayName("잘못된 Bearer 토큰 형식으로 요청 시 에러")
    void requestWithInvalidBearerFormat() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")
                .header(HttpHeaders.AUTHORIZATION, "InvalidFormat token"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("UEH4012"));
    }

    @Test
    @DisplayName("Bearer 없는 토큰으로 요청 시 에러")
    void requestWithoutBearerPrefix() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")
                .header(HttpHeaders.AUTHORIZATION, "some-token"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("UEH4012"));
    }

    @Test
    @DisplayName("손상된 JWT 토큰으로 요청 시 JWT_BROKEN 에러")
    void requestWithBrokenJwt() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.jwt.token"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("UEH4014"));
    }

    @Test
    @DisplayName("White list 엔드포인트는 JWT 필터 통과")
    void whiteListEndpointsSkipJwtFilter() throws Exception {
        mockMvc.perform(get("/api/v1/auth/login"))
            .andExpect(status().isNotFound()); // JWT 에러가 아닌 404

        mockMvc.perform(get("/checker/health"))
            .andExpect(status().isNotFound()); // JWT 에러가 아닌 404
    }
}
