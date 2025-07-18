package org.runimo.runimo.runimo.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
import org.runimo.runimo.runimo.exception.RunimoHttpResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RunimoControllerTest {

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
    @Sql(scripts = "/sql/get_my_runimo_list_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 보유_러니모_목록_조회_성공() {

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .get("/api/v1/users/me/runimos")

            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())

            .body("code", equalTo("MSH2001"))
            .body("payload.runimos", hasSize(3))
            .body("payload.total_distance_in_meters", equalTo(10000))

            .body("payload.runimos[0].id", equalTo(1))
            .body("payload.runimos[0].code", equalTo("R-101"))
            .body("payload.runimos[0].total_run_count", equalTo(3))
            .body("payload.runimos[0].total_distance_in_meters", equalTo(1000))
            .body("payload.runimos[0].is_main_runimo", equalTo(true))
            .body("payload.runimos[1].is_main_runimo", equalTo(false));
    }

    @Test
    @Sql(scripts = "/sql/get_all_runimo_type_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 전체_러니모_종류_조회_성공() {

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .get("/api/v1/runimos/types/all")

            .then()
            .log().all()
             .statusCode(HttpStatus.OK.value())

            .body("code", equalTo("MSH2003"))
            .body("payload.runimo_groups", hasSize(2))

            .body("payload.runimo_groups[0].egg_type", equalTo("마당"))
            .body("payload.runimo_groups[0].runimo_types", hasSize(4))
            .body("payload.runimo_groups[0].egg_required_distance_in_meters", equalTo(0))
            .body("payload.runimo_groups[1].egg_type", equalTo("숲"))
            .body("payload.runimo_groups[1].runimo_types", hasSize(4))
            .body("payload.runimo_groups[1].egg_required_distance_in_meters", equalTo(30000))


            .body("payload.runimo_groups[0].runimo_types[0].name", equalTo("강아지"))
            .body("payload.runimo_groups[0].runimo_types[0].img_url", equalTo("http://dummy1"))
            .body("payload.runimo_groups[0].runimo_types[0].code", equalTo("R-101"))
            .body("payload.runimo_groups[0].runimo_types[0].description", equalTo("마당-강아지예여"))
        ;
    }

    @Test
    @Sql(scripts = "/sql/set_main_runimo_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 대표_러니모_설정_성공() {

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .patch("/api/v1/runimos/" + "1" + "/main")

            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())

            .body("code", equalTo("MSH2002"))
            .body("payload.main_runimo_id", equalTo(1));
    }

    @Test
    @Sql(scripts = "/sql/set_main_runimo_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 대표_러니모_설정_실패_러니모의_소유자_아님() {
        CustomResponseCode responseCode = RunimoHttpResponseCode.USER_DO_NOT_OWN_RUNIMO;

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .patch("/api/v1/runimos/" + "4" + "/main")

            .then()
            .log().all()
            .statusCode(responseCode.getHttpStatusCode().value())

            .body("code", equalTo(responseCode.getCode()))
            .body("message", equalTo(responseCode.getClientMessage()));
    }

    @Test
    @Sql(scripts = "/sql/set_main_runimo_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 대표_러니모_설정_실패_러니모_존재하지않음() {
        CustomResponseCode responseCode = RunimoHttpResponseCode.RUNIMO_NOT_FOUND;

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .patch("/api/v1/runimos/" + "9999" + "/main")

            .then()
            .log().all()
            .statusCode(responseCode.getHttpStatusCode().value())

            .body("code", equalTo(responseCode.getCode()))
            .body("message", equalTo(responseCode.getClientMessage()));
    }
}