package org.runimo.runimo.hatch.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.TokenUtils;
import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class HatchControllerTest {

    @LocalServerPort
    int port;
    @Autowired
    private CleanUpUtil cleanUpUtil;
    @Autowired
    private TokenUtils tokenUtils;
    private String token;
    @Autowired
    private ObjectMapper objectMapper;

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
    @Sql(scripts = "/sql/hatch_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자의_알_부화_성공_8회() {

        for (int i = 0; i < 8; i++) {
            int incubationEggId = i + 3;

            given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)

                .when()
                .post("/api/v1/incubating-eggs/" + incubationEggId + "/hatch")

                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())

                .body("code", equalTo("HSH2011"))
                .body("payload.name", notNullValue())
                .body("payload.img_url", startsWith("http://"))
                .body("payload.code", startsWith("R-10"))
                .body("payload.egg_code", notNullValue())
                .body("payload.is_duplicated", anyOf(equalTo(true), equalTo(false)));
        }
    }

    @Test
    @Sql(scripts = "/sql/hatch_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자의_알_부화_실패_부화가능한_상태가_아님() {
        CustomResponseCode responseCode = HatchHttpResponseCode.HATCH_EGG_NOT_READY;

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/incubating-eggs/" + "2" + "/hatch")

            .then()
            .log().all()
            .statusCode(responseCode.getHttpStatusCode().value())

            .body("code", equalTo(responseCode.getCode()))
            .body("message", equalTo(responseCode.getClientMessage()));
    }

    @Test
    @Sql(scripts = "/sql/hatch_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자의_알_부화_실패_알_존재하지_않음() {
        CustomResponseCode responseCode = HatchHttpResponseCode.HATCH_EGG_NOT_FOUND;

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/incubating-eggs/" + "9999" + "/hatch")

            .then()
            .log().all()
            .statusCode(responseCode.getHttpStatusCode().value())

            .body("code", equalTo(responseCode.getCode()))
            .body("message", equalTo(responseCode.getClientMessage()));
    }
}