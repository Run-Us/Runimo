package org.runimo.runimo.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest
@ActiveProfiles("dev")
public class TokenRefreshDevTest {

  @Autowired
  private TokenRefreshService tokenRefreshService;

  @MockitoBean
  private UserFinder userFinder;
  @Autowired
  private JwtTokenFactory jwtTokenFactory;

  @Test
  void 배포_환경_리프레시_토큰_저장_성공() {
    //given
    String userPublicId = "test-user-public-id";
    when(userFinder.findUserByPublicId(userPublicId))
        .thenReturn(Optional.ofNullable(UserFixtures.getUserWithId(1L)));
    String refreshToken = "test-refresh-token";
    tokenRefreshService.putRefreshToken(userPublicId, refreshToken);
  }

  @Test
  void 배포_환경_리프레시_토큰_검증_실패_시_예외() {
    //given
    when(userFinder.findUserByPublicId(any()))
        .thenReturn(Optional.ofNullable(UserFixtures.getUserWithId(1L)));
    String refreshToken = "test-refresh-token";

    //when & then
    assertThrows(
        UserJwtException.class,
        () -> tokenRefreshService.refreshAccessToken(refreshToken),
        "토큰 검증 실패 시 예외가 발생해야 합니다."
    );
  }

  @Test
  void 배포_환경_리프레시_토큰_탐색_실패_예외() {
    String testRefreshToken = jwtTokenFactory.generateRefreshToken("test-user-public-id-2");

    when(userFinder.findUserByPublicId(any()))
        .thenReturn(Optional.ofNullable(UserFixtures.getUserWithId(1L)));

    //when & then
    assertThrows(
        UserJwtException.class,
        () -> tokenRefreshService.refreshAccessToken(testRefreshToken),
        "리프레시 토큰 탐색 실패 시 예외가 발생해야 합니다."
    );
  }

  @Test
  void 배포_환경_리프레시_토큰_저장_후_조회_성공() {
    //given
    String userPublicId = "test-user-public-id";
    when(userFinder.findUserByPublicId(userPublicId))
        .thenReturn(Optional.ofNullable(UserFixtures.getUserWithId(1L)));
    String refreshToken = jwtTokenFactory.generateRefreshToken(userPublicId);
    tokenRefreshService.putRefreshToken(userPublicId, refreshToken);

    TokenPair tokenPair = tokenRefreshService.refreshAccessToken(refreshToken);
    assertEquals(refreshToken, tokenPair.refreshToken());
  }
}
