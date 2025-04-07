package org.runimo.runimo.hatch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class HatchControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Autowired
    private CleanUpUtil cleanUpUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        cleanUpUtil.cleanUpUserInfos();
    }

    @Test
    @Sql(scripts = "/sql/hatch_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자의_알_부화_성공() {
        String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");


        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)

                .when()
                .post("/api/v1/incubating-eggs/"+"1"+"/hatch")

                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())

                .body("code", equalTo("HSH2011"))
                .body("payload.name", equalTo("토끼"))
                .body("payload.img_url", equalTo("http://dummy1"))
                .body("payload.code", equalTo("R-101"))
                .body("payload.is_duplicated", equalTo(false));
    }

    @Test
    @Sql(scripts = "/sql/hatch_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자의_알_부화_실패_부화가능한_상태가_아님() {
        String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
        CustomResponseCode responseCode = HatchHttpResponseCode.HATCH_EGG_NOT_READY;

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)

                .when()
                .post("/api/v1/incubating-eggs/"+"2"+"/hatch")

                .then()
                .log().all()
                .statusCode(responseCode.getHttpStatusCode().value())

                .body("code", equalTo(responseCode.getCode()))
                .body("message", equalTo(responseCode.getClientMessage()));
    }

    @Test
    @Sql(scripts = "/sql/hatch_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자의_알_부화_실패_알_존재하지_않음() {
        String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
        CustomResponseCode responseCode = HatchHttpResponseCode.HATCH_EGG_NOT_FOUND;

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)

                .when()
                .post("/api/v1/incubating-eggs/"+"9999"+"/hatch")

                .then()
                .log().all()
                .statusCode(responseCode.getHttpStatusCode().value())

                .body("code", equalTo(responseCode.getCode()))
                .body("message", equalTo(responseCode.getClientMessage()));
    }
}