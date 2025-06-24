package org.runimo.runimo.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class SecurityConfigDevTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("개발 환경에서 Swagger 접근 가능")
    void swaggerAccessibleInDev() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
            .andExpect(status().isFound()); // 302 redirect to swagger-ui/index.html

        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("개발 환경에서도 기본 보안 규칙 적용")
    void basicSecurityRulesStillApply() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/admin/users"))
            .andExpect(status().isUnauthorized());
    }
}
