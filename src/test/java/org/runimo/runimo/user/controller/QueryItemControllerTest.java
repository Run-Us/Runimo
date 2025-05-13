package org.runimo.runimo.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.TokenUtils;
import org.runimo.runimo.configs.ControllerTest;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.dto.response.ItemQueryResponse;
import org.runimo.runimo.user.service.usecases.items.UseItemUsecase;
import org.runimo.runimo.user.service.usecases.query.MyItemQueryUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = {UserItemController.class})
class QueryItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserFinder userFinder;
    @MockitoBean
    private MyItemQueryUsecase myItemQueryUsecase;
    @MockitoBean
    private UseItemUsecase useItemUsecase;

    private String token;

    @BeforeEach
    void setUp() {
        token = TokenUtils.createTestOnlyToken(TEST_USER_UUID);
    }

    @Test
    void 보유한_아이템_조회_성공() throws Exception {
        given(myItemQueryUsecase.queryMyAllItems(any()))
            .willReturn(new ItemQueryResponse(new ArrayList<>()));
        given(userFinder.findUserByPublicId(any()))
            .willReturn(Optional.of(UserFixtures.getDefaultUser()));
        mockMvc.perform(get("/api/v1/users/me/items")
                .header("Authorization", token)
                .param("itemType", "EGG"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
