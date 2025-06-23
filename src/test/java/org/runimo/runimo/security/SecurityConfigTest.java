package org.runimo.runimo.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("공개 엔드포인트는 인증 없이 접근 가능")
    void publicEndpointsAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/checker/check"))
            .andExpect(status().isNotFound());

        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 엔드포인트는 인증 없이 접근 시 401")
    void userEndpointsRequireAuth() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @DisplayName("관리자 엔드포인트는 인증 없이 접근 시 401")
    void adminEndpointsRequireAuth() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("USER 역할로 사용자 엔드포인트 접근 가능")
    void userCanAccessUserEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
            .andExpect(status().isNotFound()); // 404는 정상 (실제 컨트롤러 메서드 없어서)
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("USER 역할로 관리자 엔드포인트 접근 시 403")
    void userCannotAccessAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users"))
            .andExpect(status().isForbidden())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("ADMIN 역할로 모든 엔드포인트 접근 가능")
    void adminCanAccessAllEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("인증되지 않은 요청의 에러 응답 형태 확인")
    void unauthorizedResponseFormat() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.code").exists())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("권한 부족 시 에러 응답 형태 확인")
    void forbiddenResponseFormat() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users"))
            .andExpect(status().isForbidden())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.code").exists())
            .andExpect(jsonPath("$.message").exists());
    }
}
