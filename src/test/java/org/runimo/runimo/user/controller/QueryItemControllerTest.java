package org.runimo.runimo.user.controller;

import org.junit.jupiter.api.Test;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.configs.ControllerTest;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.dtos.ItemQueryResponse;
import org.runimo.runimo.user.service.usecases.items.UseItemUsecase;
import org.runimo.runimo.user.service.usecases.query.MyItemQueryUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = {UserItemController.class})
class QueryItemControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtTokenFactory jwtTokenFactory;
  @MockitoBean
  private UserFinder userFinder;
  @MockitoBean
  private MyItemQueryUsecase myItemQueryUsecase;
  @MockitoBean
  private UseItemUsecase useItemUsecase;

  @Test
  void 보유한_아이템_조회_성공() throws Exception {
    String accessToken = jwtTokenFactory.generateAccessToken("test-user-uuid-1");
    given(myItemQueryUsecase.execute(any()))
        .willReturn(new ItemQueryResponse(new ArrayList<>()));
    given(userFinder.findUserByPublicId(any()))
        .willReturn(Optional.of(UserFixtures.getDefaultUser()));
    mockMvc.perform(get("/api/v1/users/me/items")
            .header("Authorization", "Bearer " + accessToken))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
