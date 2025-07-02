package org.runimo.runimo.auth.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.runimo.runimo.user.domain.DevicePlatform.APNS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.controller.request.AuthSignupRequest;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.login.apple.AppleTokenVerifier;
import org.runimo.runimo.auth.service.login.kakao.KakaoLoginHandler;
import org.runimo.runimo.auth.service.login.kakao.KakaoTokenVerifier;
import org.runimo.runimo.external.FileStorageService;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.SocialProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthAcceptanceTest {

    @LocalServerPort
    private int port;
    @MockitoBean
    private FileStorageService fileStorageService;
    @MockitoBean
    private KakaoTokenVerifier kakaoTokenVerifier;
    @MockitoBean
    private AppleTokenVerifier appleTokenVerifier;

    @Autowired
    private SignupTokenRepository signupTokenRepository;

    @Autowired
    private KakaoLoginHandler kakaoLoginHandler;

    private String token;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CleanUpUtil cleanUpUtil;
    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        // Save a valid signup token in the database
        SignupToken signupToken = new SignupToken(
            "valid-token",
            "provider-id",
            "refresh-token",
            SocialProvider.KAKAO
        );
        token = jwtTokenFactory.generateSignupTemporalToken("provider-id", SocialProvider.KAKAO,
            "valid-token");
        signupTokenRepository.save(signupToken);
    }

    @AfterEach
    void tearDown() {
        cleanUpUtil.cleanUpUserInfos();
        signupTokenRepository.deleteAll();

    }

    @Test
    void 회원가입_성공_201응답() throws JsonProcessingException {

        AuthSignupRequest request = new AuthSignupRequest(token, "username", Gender.UNKNOWN);

        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .log().all()
            .body("payload.nickname", equalTo("username"))
            .body("payload.token_pair.access_token", notNullValue())
            .body("payload.token_pair.refresh_token", notNullValue());
    }

    @Test
    void 회원가입_시_애정포인트_10_지급() throws JsonProcessingException {
        AuthSignupRequest request = new AuthSignupRequest(token, "username", Gender.UNKNOWN);

        String accessToken = String.valueOf(given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .log().all()
            .body("payload.nickname", equalTo("username"))
            .body("payload.token_pair.access_token", notNullValue())
            .body("payload.token_pair.refresh_token", notNullValue())
            .extract()
            .path("payload.token_pair.access_token").toString());

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + accessToken)
            .when()
            .get("/api/v1/main")
            .then()
            .statusCode(HttpStatus.OK.value())
            .log().all()
            .body("payload.user_info.love_point", equalTo(10));
    }

    @Test
    void 토큰_오류_회원가입_실패_401응답() throws JsonProcessingException {
        AuthSignupRequest request = new AuthSignupRequest(token, "username", Gender.UNKNOWN);

        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .log().all()
            .body("payload.nickname", equalTo("username"))
            .body("payload.token_pair.access_token", notNullValue())
            .body("payload.token_pair.refresh_token", notNullValue());

        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("중복된 유저를 회원가입 시도하면 401응답 - 동일한 토큰")
    void 중복_유저_회원가입_401응답() throws JsonProcessingException {
        AuthSignupRequest request = new AuthSignupRequest(token, "username", Gender.UNKNOWN);

        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .log().all()
            .body("payload.nickname", equalTo("username"))
            .body("payload.token_pair.access_token", notNullValue())
            .body("payload.token_pair.refresh_token", notNullValue());

        SignupToken signupToken = new SignupToken(
            "valid-token",
            "provider-id",
            "refresh-token",
            SocialProvider.KAKAO
        );
        token = jwtTokenFactory.generateSignupTemporalToken("provider-id", SocialProvider.KAKAO,
            "valid-token");
        request = new AuthSignupRequest(token, "username", Gender.UNKNOWN);

        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());

        signupTokenRepository.delete(signupToken);
    }

    @Test
    @DisplayName("중복된 유저를 회원가입 시도하면 409응답 - 동일한 토큰")
    void 중복_유저_회원가입_409응답_다른토큰() throws JsonProcessingException {
        AuthSignupRequest request = new AuthSignupRequest(token, "username", Gender.UNKNOWN);

        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .log().all()
            .body("payload.nickname", equalTo("username"))
            .body("payload.token_pair.access_token", notNullValue())
            .body("payload.token_pair.refresh_token", notNullValue());

        SignupToken signupToken = new SignupToken(
            "valid-token-2",
            "provider-id",
            "refresh-token",
            SocialProvider.KAKAO
        );
        token = jwtTokenFactory.generateSignupTemporalToken("provider-id", SocialProvider.KAKAO,
            "valid-token-2");
        signupTokenRepository.save(signupToken);
        request = new AuthSignupRequest(token, "username", Gender.UNKNOWN);

        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.CONFLICT.value());

        signupTokenRepository.delete(signupToken);
    }

    @Test
    @DisplayName("디바이스 토큰을 포함하여 회원가입 성공 201응답")
    void 회원가입_디바이스_토큰_포함_성공_201응답() throws JsonProcessingException {
        AuthSignupRequest request = buildSignupRequest(token, "username", Gender.UNKNOWN);
        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .log().all()
            .body("payload.nickname", equalTo("username"))
            .body("payload.token_pair.access_token", notNullValue())
            .body("payload.token_pair.refresh_token", notNullValue());
    }

    @Test
    @DisplayName("디바이스 토큰을 포함했지만 디바이스 플랫폼을 미포함하여 400응답")
    void 회원가입_디바이스_토큰_포함_플랫폼_미포함_실패_400응답() throws JsonProcessingException {
        AuthSignupRequest request = new AuthSignupRequest(token, "nickname", null,
            "example_device_token", null);
        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("APNS_플랫폼으로_회원가입_성공_201응답")
    void 회원가입_APNS_플랫폼으로_성공_201응답() throws JsonProcessingException {
        AuthSignupRequest request = buildSignupRequest(token, "username", Gender.UNKNOWN);
        given()
            .contentType(ContentType.MULTIPART)
            .multiPart("request", objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .log().all();
    }

    private AuthSignupRequest buildSignupRequest(String token, String nickname, Gender gender) {
        return new AuthSignupRequest(token, nickname, gender, "example_device_token", APNS.name());
    }
}
