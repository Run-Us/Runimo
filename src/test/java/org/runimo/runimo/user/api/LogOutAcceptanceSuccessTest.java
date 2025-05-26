package org.runimo.runimo.user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.TokenUtils;
import org.runimo.runimo.auth.controller.request.AppleLoginRequest;
import org.runimo.runimo.auth.controller.request.KakaoLoginRequest;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dto.AuthResult;
import org.runimo.runimo.auth.service.dto.AuthStatus;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.auth.service.login.apple.AppleLoginHandler;
import org.runimo.runimo.auth.service.login.kakao.KakaoLoginHandler;
import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LogOutAcceptanceSuccessTest {

    @LocalServerPort
    int port;

    @MockitoBean
    private KakaoLoginHandler kakaoLoginHandler;
    @MockitoBean
    private AppleLoginHandler appleLoginHandler;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRefreshService tokenRefreshService;

    @Autowired
    private CleanUpUtil cleanUpUtil;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = tokenUtils.createTokenByUserPublicId(TEST_USER_UUID);
    }

    @AfterEach
    void tearDown() {
        cleanUpUtil.cleanUpUserInfos();
    }

    @Test
    @Sql(scripts = "/sql/log_out_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 로그아웃_성공_이미_로그아웃된_사용자_200() {
        CustomResponseCode responseCode = UserHttpResponseCode.ALREADY_LOG_OUT_SUCCESS;

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/auth/log-out")

            .then()
            .log().all()
            .statusCode(responseCode.getHttpStatusCode().value())

            .body("code", equalTo(responseCode.getCode()))
            .body("message", equalTo(responseCode.getClientMessage()));
    }


    @Test
    @Sql(scripts = "/sql/log_out_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 카카오_로그인_후_로그아웃_성공_200() throws JsonProcessingException {
        // OIDC 토큰 처리 mocking
        AuthResult authResult = createAuthResultOfTestUser(1L);

        Mockito.when(kakaoLoginHandler.validateAndLogin(any()))
            .thenReturn(authResult);

        // 로그인
        KakaoLoginRequest loginReq = new KakaoLoginRequest("test-oidc-token-1");

        UserHttpResponseCode loginSuccessCode = UserHttpResponseCode.LOGIN_SUCCESS;
        ValidatableResponse loginRes = given()
            .body(objectMapper.writeValueAsString(loginReq))
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/auth/kakao")

            .then()
            .log().all()
            .statusCode(loginSuccessCode.getHttpStatusCode().value())

            .body("code", equalTo(loginSuccessCode.getCode()))
            .body("message", equalTo(loginSuccessCode.getClientMessage()))
            .body("payload.access_token", notNullValue())
            .body("payload.refresh_token", notNullValue())
            .body("payload.img_url", notNullValue());

        String accessToken = loginRes.extract().body().path("payload.access_token");

        // 로그아웃
        CustomResponseCode logOutSuccessCode = UserHttpResponseCode.LOG_OUT_SUCCESS;
        given()
            .header("Authorization", accessToken)
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/auth/log-out")

            .then()
            .log().all()
            .statusCode(logOutSuccessCode.getHttpStatusCode().value())

            .body("code", equalTo(logOutSuccessCode.getCode()))
            .body("message", equalTo(logOutSuccessCode.getClientMessage()));
    }

    @Test
    @Sql(scripts = "/sql/log_out_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 애플_로그인_후_로그아웃_성공_200() throws JsonProcessingException {
        // OIDC 토큰 처리 mocking
        AuthResult authResult = createAuthResultOfTestUser(2L);

        Mockito.when(appleLoginHandler.validateAndLogin(any(), any()))
            .thenReturn(authResult);

        // 로그인
        AppleLoginRequest loginReq = new AppleLoginRequest("test-auth-code-1",
            "test-auth-verifier-1");

        UserHttpResponseCode loginSuccessCode = UserHttpResponseCode.LOGIN_SUCCESS;
        ValidatableResponse loginRes = given()
            .body(objectMapper.writeValueAsString(loginReq))
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/auth/apple")

            .then()
            .log().all()
            .statusCode(loginSuccessCode.getHttpStatusCode().value())

            .body("code", equalTo(loginSuccessCode.getCode()))
            .body("message", equalTo(loginSuccessCode.getClientMessage()))
            .body("payload.access_token", notNullValue())
            .body("payload.refresh_token", notNullValue())
            .body("payload.img_url", notNullValue());

        String accessToken = loginRes.extract().body().path("payload.access_token");

        // 로그아웃
        CustomResponseCode logOutSuccessCode = UserHttpResponseCode.LOG_OUT_SUCCESS;
        given()
            .header("Authorization", accessToken)
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/auth/log-out")

            .then()
            .log().all()
            .statusCode(logOutSuccessCode.getHttpStatusCode().value())

            .body("code", equalTo(logOutSuccessCode.getCode()))
            .body("message", equalTo(logOutSuccessCode.getClientMessage()));
    }

    private AuthResult createAuthResultOfTestUser(Long userId) {
        User user = getTestUser(userId);
        TokenPair tokenPair = getTokenPair(user);
        return AuthResult.success(AuthStatus.LOGIN_SUCCESS, user, tokenPair);
    }

    private TokenPair getTokenPair(User user) {
        TokenPair tokenPair = jwtTokenFactory.generateTokenPair(user);
        tokenRefreshService.putRefreshToken(user.getPublicId(), tokenPair.refreshToken());
        return tokenPair;
    }

    private User getTestUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}