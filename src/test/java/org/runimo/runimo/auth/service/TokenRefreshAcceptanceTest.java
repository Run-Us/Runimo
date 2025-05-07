package org.runimo.runimo.auth.service;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.controller.request.KakaoLoginRequest;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.apple.KakaoUserInfo;
import org.runimo.runimo.auth.service.kakao.KakaoTokenVerifier;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TokenRefreshAcceptanceTest {

  private static final String TEST_PROVIDER_ID = "test-provider-id";
  private static final String TEST_PUBLIC_ID = "test-public-id";

  @LocalServerPort
  private int port;

  @Autowired
  private CleanUpUtil cleanUpUtil;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private OAuthInfoRepository oAuthInfoRepository;

  @MockitoBean
  private KakaoTokenVerifier kakaoTokenVerifier;

  @Autowired
  private JwtTokenFactory jwtTokenFactory;

  @BeforeEach
  void setup() {
    RestAssured.port = port;
    cleanUpUtil.cleanUpUserInfos();

    User user = UserFixtures.getDefaultUser();
    userRepository.save(user);
    OAuthInfo oAuthInfo = OAuthInfo.builder()
        .user(user)
        .providerId(TEST_PROVIDER_ID)
        .provider(SocialProvider.KAKAO)
        .build();
    oAuthInfoRepository.save(oAuthInfo);
  }



  @Test
  void 로그인_후_토큰_갱신_성공_200() throws JsonProcessingException {

    String testOidcToken = jwtTokenFactory.generateAccessToken(TEST_PUBLIC_ID);
    KakaoLoginRequest request = new KakaoLoginRequest(testOidcToken);

    Mockito.when(kakaoTokenVerifier.verifyToken(any(DecodedJWT.class)))
        .thenReturn(new KakaoUserInfo(TEST_PROVIDER_ID));

    // login
    String refreshToken = given()
        .header("Authorization", "Bearer test-access-token")
        .contentType("application/json")
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/auth/kakao")
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body().path("payload.refresh_token");


    given()
        .contentType("application/json")
        .header("Authorization", refreshToken)
        .when()
        .post("/api/v1/auth/refresh")
        .then()
        .statusCode(HttpStatus.OK.value());

  }

  @Test
  void 로그인_후_RefreshToken_오류_시_갱신_실패_403() throws JsonProcessingException {

    String testOidcToken = jwtTokenFactory.generateAccessToken(TEST_PUBLIC_ID);
    KakaoLoginRequest request = new KakaoLoginRequest(testOidcToken);

    Mockito.when(kakaoTokenVerifier.verifyToken(any(DecodedJWT.class)))
        .thenReturn(new KakaoUserInfo(TEST_PROVIDER_ID));

    // login
    String accessToken = given()
        .header("Authorization", "Bearer test-access-token")
        .contentType("application/json")
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/auth/kakao")
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body().path("payload.access_token");

    // 리프레쉬가 아니라 액세스토큰을 넘겨서 오류 상황을 재현
    given()
        .contentType("application/json")
        .header("Authorization", accessToken)
        .when()
        .post("/api/v1/auth/refresh")
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());

  }

}
