package org.runimo.runimo.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import javax.naming.NoPermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.TokenUtils;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.UserDetail;
import org.runimo.runimo.configs.ControllerTest;
import org.runimo.runimo.user.service.dto.LatestRunningRecord;
import org.runimo.runimo.user.service.dto.response.MyPageViewResponse;
import org.runimo.runimo.user.service.usecases.UpdateUserDetailUsecase;
import org.runimo.runimo.user.service.usecases.query.MyPageQueryUsecase;
import org.runimo.runimo.user.service.usecases.query.UserInfoQueryUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ControllerTest(controllers = {MyPageController.class})
class MyPageControllerTest {

    private static final String TEST_USER_UUID = "test-user-uuid-1";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MyPageQueryUsecase myPageQueryUsecase;
    @MockitoBean
    private UserInfoQueryUsecase userInfoQueryUsecase;
    @MockitoBean
    private UpdateUserDetailUsecase updateUserDetailUsecase;
    @MockitoBean
    private JwtResolver jwtResolver;
    @MockitoBean
    private UserIdResolver userIdResolver;

    private String token;

    @BeforeEach
    void setUp() {
        token = TokenUtils.createTestOnlyToken(TEST_USER_UUID);
    }

    @Test
    @WithMockUser(username = "test-user-uuid-1")
    void 마이_페이지_조회_성공() throws Exception {
        // given

        MyPageViewResponse response = new MyPageViewResponse(
            "Daniel",
            "https://example.com/images/user1.png",
            2L,
            5L,
            new LatestRunningRecord(
                "활기차 모닝런",
                LocalDateTime.of(2025, 3, 24, 10, 11),
                3000L,
                100L,
                6700L
            )
        );

        when(myPageQueryUsecase.execute(any()))
            .thenReturn(response);
        when(jwtResolver.getUserDetailFromJwtToken(any()))
            .thenReturn(new UserDetail("test-user-uuid-1", "USER"));
        when(userIdResolver.resolveArgument(any(), any(), any(), any()))
            .thenReturn(1L);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.payload.nickname").value("Daniel"))
            .andExpect(jsonPath("$.payload.profile_image_url").value(
                "https://example.com/images/user1.png"))
            .andExpect(jsonPath("$.payload.total_distance_in_meters").value(2L))
            .andExpect(jsonPath("$.payload.latest_run_date_before").value(5L))
            .andExpect(jsonPath("$.payload.latest_running_record_nullable.title").value("활기차 모닝런"))
            .andExpect(
                jsonPath("$.payload.latest_running_record_nullable.start_date_time").exists())
            .andExpect(
                jsonPath("$.payload.latest_running_record_nullable.distance_in_meters").value(3000))
            .andExpect(
                jsonPath("$.payload.latest_running_record_nullable.duration_in_seconds").value(100))
            .andExpect(jsonPath(
                "$.payload.latest_running_record_nullable.average_pace_in_miliseconds").value(
                6700));
    }

    @Test
    @WithAnonymousUser
    void 마이_페이지_조회_실패_인증되지_않은_사용자() throws Exception {
        // when & then
        when(userIdResolver.resolveArgument(any(), any(), any(), any()))
            .thenThrow(NoPermissionException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test-user-uuid-3")
    void 마이_페이지_조회_실패_사용자정보_없음() throws Exception {
        // given
        String accessToken = TokenUtils.createTestOnlyToken("non-existing-user-uuid");

        when(myPageQueryUsecase.execute(any()))
            .thenThrow(new NoSuchElementException("User not found"));
        when(jwtResolver.getUserDetailFromJwtToken(any()))
            .thenReturn(new UserDetail("non-existing-user-uuid", "USER"));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
}