package org.runimo.runimo.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.TokenUtils;
import org.runimo.runimo.configs.ControllerTest;
import org.runimo.runimo.runimo.service.dto.MainRunimoStat;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.dto.UserMainViewInfo;
import org.runimo.runimo.user.service.dto.response.MainViewResponse;
import org.runimo.runimo.user.service.usecases.query.MainViewQueryUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ControllerTest(controllers = {MainViewController.class})
class MainViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserFinder userFinder;
    @MockitoBean
    private MainViewQueryUsecase mainViewUsecase;

    private String token;

    @BeforeEach
    void setUp() {
        token = TokenUtils.createTestOnlyToken(TEST_USER_UUID);
    }

    @Test
    void 메인_화면_조회_성공() throws Exception {
        // given
        MainViewResponse response = new MainViewResponse(
            new MainRunimoStat(
                "example",
                "https://example.com/images/user1.png",
                100L,
                3000L
            ),
            new UserMainViewInfo(
                2L,
                3L
            )
        );
        given(mainViewUsecase.execute(any())).willReturn(response);
        given(userFinder.findUserByPublicId(any())).willReturn(
            Optional.ofNullable(UserFixtures.getDefaultUser()));
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/main")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("MAIN_PAGE_DATA_FETCHED"))
            .andExpect(jsonPath("$.payload.main_runimo_stat_nullable.name").value("example"))
            .andExpect(jsonPath("$.payload.main_runimo_stat_nullable.image_url").value(
                "https://example.com/images/user1.png"))
            .andExpect(
                jsonPath("$.payload.main_runimo_stat_nullable.total_running_count").value(100))
            .andExpect(
                jsonPath("$.payload.main_runimo_stat_nullable.total_distance_in_meters").value(
                    3000))
            .andExpect(jsonPath("$.payload.user_info.love_point").value(2))
            .andExpect(jsonPath("$.payload.user_info.total_egg_count").value(3));
    }
}
