package org.runimo.runimo.user.api;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.TokenUtils;
import org.runimo.runimo.auth.controller.request.AuthSignupRequest;
import org.runimo.runimo.auth.controller.request.KakaoLoginRequest;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dto.AuthResult;
import org.runimo.runimo.auth.service.dto.AuthStatus;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.auth.service.login.kakao.KakaoLoginHandler;
import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LogOutAcceptanceTest {

    private static final Logger log = LoggerFactory.getLogger(LogOutAcceptanceTest.class);
    @LocalServerPort
    int port;

    @MockitoBean
    private KakaoLoginHandler kakaoLoginHandler;

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
    void 로그아웃_성공() {
        CustomResponseCode responseCode = UserHttpResponseCode.LOG_OUT_SUCCESS;

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
        // 카카오 토큰 처리 mocking
        User user = userRepository.findById(1L).orElseThrow();
        TokenPair tokenPair = jwtTokenFactory.generateTokenPair(user);
        tokenRefreshService.putRefreshToken(user.getPublicId(), tokenPair.refreshToken());

        Mockito.when(kakaoLoginHandler.validateAndLogin(any()))
            .thenReturn(AuthResult.success(AuthStatus.LOGIN_SUCCESS, user, tokenPair));

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
            .header("Authorization", accessToken) //"Bearer " +
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/auth/log-out")

            .then()
            .log().all()
            .statusCode(logOutSuccessCode.getHttpStatusCode().value())

            .body("code", equalTo(logOutSuccessCode.getCode()))
            .body("message", equalTo(logOutSuccessCode.getClientMessage()));
    }
}