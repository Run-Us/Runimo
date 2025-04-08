package org.runimo.runimo.user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.controller.request.AuthSignupRequest;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.SignUpUsecaseImpl;
import org.runimo.runimo.auth.service.dtos.SignupUserResponse;
import org.runimo.runimo.auth.service.dtos.TokenPair;
import org.runimo.runimo.user.controller.request.UseItemRequest;
import org.runimo.runimo.user.domain.SocialProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserItemAcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @MockitoBean
    private SignUpUsecaseImpl signUpUsecaseImpl;


    @Autowired
    private CleanUpUtil cleanUpUtil;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach()
    void tearDown() {
        cleanUpUtil.cleanUpUserInfos();
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 아이템_조회_성공() {
        // given
        String jwt = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");

        // when + then
        given()
            .header("Authorization", jwt)
            .when()
            .get("/api/v1/users/eggs")
            .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("success", equalTo(true))
            .body("payload", notNullValue())
            .body("payload.size()", greaterThan(0))
            .body("payload.items[0].item_id", not(emptyOrNullString()))
            .body("payload.items[0].amount", greaterThanOrEqualTo(0));
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 아이템_사용시_보유량감소() throws JsonProcessingException {
        String jwt = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");

        UseItemRequest request = new UseItemRequest(1L, 2L);

        given()
            .header("Authorization", jwt)
            .body(objectMapper.writeValueAsString(request))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/users/me/items/use")
            .then()
            .log().ifError()
            .statusCode(HttpStatus.OK.value());

        given()
            .header("Authorization", jwt)
            .when()
            .get("/api/v1/users/eggs")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("payload", notNullValue())
            .body("payload.size()", greaterThan(0))
            .body("payload.items[0].item_id", not(emptyOrNullString()))
            .body("payload.items[0].amount", equalTo(0));
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 보유한_수량보다_더_많은_요청_시_에러() throws JsonProcessingException {
        String jwt = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
        UseItemRequest request = new UseItemRequest(1L, 10L);

        given()
            .header("Authorization", jwt)
            .body(objectMapper.writeValueAsString(request))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/users/me/items/use")
            .then()
            .log().ifError()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 카카오_회원가입후_알_지급_성공() throws JsonProcessingException {
        String registerToken = jwtTokenFactory.generateRegisterTemporalToken("test-pid",
            SocialProvider.KAKAO);
        String token = jwtTokenFactory.generateAccessToken("test-user-uuid-1");
        when(signUpUsecaseImpl.register(any()))
            .thenReturn(new SignupUserResponse(
                1L,
                "test-user",
                "https://test-image.com",
                new TokenPair(token, "token2")
            ));

        AuthSignupRequest request = new AuthSignupRequest(
            registerToken,
            "test-user",
            "https://test-image.com"
        );

        ValidatableResponse res = given()
            .body(objectMapper.writeValueAsString(request))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/auth/signup")
            .then()
            .log().ifError()
            .statusCode(HttpStatus.CREATED.value());

        String accessToken = res.extract().body().jsonPath()
            .getString("payload.token_pair.access_token");

        given()
            .header("Authorization", "Bearer " + accessToken)
            .when()
            .get("/api/v1/users/eggs")
            .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("success", equalTo(true))
            .body("payload", notNullValue())
            .body("payload.size()", greaterThan(0))
            .body("payload.items[0].item_id", not(emptyOrNullString()))
            .body("payload.items[0].amount", greaterThanOrEqualTo(0));
    }
}
